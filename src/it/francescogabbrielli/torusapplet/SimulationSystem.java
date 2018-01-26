package it.francescogabbrielli.torusapplet;

import Jama.Matrix;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author Francesco Gabbrielli
 */
public class SimulationSystem {

    public final static int X = 0;
    public final static int Y = 1;
    public final static int Z = 2;

	private int atomsNumber ;
	public Vector<Atom> atoms ;
	public Matrix transform, inverse ;
	public double temp;

	private Random rand ;

	public double pe;
	public double ke;
	public double te;
	private long ts = 1;
	private double mass = 39.948;
	// Configurable parameters (by user)
	private double density;
	private boolean scaling = true;
	private double applTemp;
	private double epsilon = 398.4;
	private double sigma = 3.41;
	private double cutoff = 8.5;
	static final double AVOGAD = 6.0221367e23;
	static final double MASSUNIT = 1.6605402e-27;
	static final double LENGTHUNIT = 1.0e-10;
	static final double TIMEUNIT = 1.0e-12;
	static final double ENERGYUNIT = (MASSUNIT * (LENGTHUNIT / TIMEUNIT)
			* (LENGTHUNIT / TIMEUNIT));
	static final double KB = 1.380658e-23 / ENERGYUNIT;
	static final double TIMESTEP = 0.01;
	private double[] keArray = new double[10];
	private int kePointer = 0;

    public SimulationSystem(int nAtoms, double t, double d) {
		atomsNumber = nAtoms ;
        atoms = new Vector<Atom>() ;
        setTransform(Matrix.identity(3, 3).times(d)) ;
		this.applTemp = t ;
		rand = new Random();
		createAtoms();
		resetDensity();
    }

	private void createAtoms() {
		Atom a;
		double x, y, z ;
		double vx, vy, vz;

		int atomd = (int) Math.pow((double) atomsNumber-.5d, 1/3d) + 1 ;
		int cube = atomd * atomd * atomd ;
		
	out:
		for (int i = 0; i < atomd; i++) {
			for (int j = 0; j < atomd; j++) {
				for (int k = 0; k < atomd; k++) {
					x = ((2*i + 1d) / atomd) - 1d ;
					y = ((2*j + 1d) / atomd) - 1d ;
					z = ((2*k + 1d) / atomd) - 1d ;
					vx = Math.sqrt((KB * applTemp) / mass) * rand.nextGaussian();
					vy = Math.sqrt((KB * applTemp) / mass) * rand.nextGaussian();
					vz = Math.sqrt((KB * applTemp) / mass) * rand.nextGaussian();
					a = new Atom(0, 0, 0, vx, vy, vz);
					a.add(new PointND(x,y,z).times(transform)) ;
					atoms.add(a);
					if(atoms.size()==atomsNumber)
						break out;
				}
			}
		}

	}
	
	public void calcForces() {
		double r2, ri2, ri12, ri6;
		double cutoff2 = cutoff * cutoff;
		double force;

		pe = 0.0;
		//System.out.println("CALC-FORCES") ;

		int i = atomsNumber ;
		int j ;
		PointND r = new PointND(3) ;
		for(ListIterator<Atom> li = atoms.listIterator(i) ; li.hasPrevious() ;) {
			Atom ai = li.previous() ;
			--i ;
			j = atomsNumber ;
			//System.out.println("ATOM "+i) ;

			for(ListIterator<Atom> lj = atoms.listIterator(j) ; lj.hasPrevious() ;) {
				Atom aj = lj.previous() ;
				j-- ;
				if(i!=j) {
					//System.out.println("- ATOM "+j) ;
					for(int kx=-2 ; kx<=2 ; kx+=2)
						for(int ky=-2 ; ky<=2 ; ky+=2)
							for(int kz=-2 ; kz<=2 ; kz+=2) {
								r.setTo(ai) ;
								r.add(aj, -1) ;
								r.add(transform, X, kx) ;
								r.add(transform, Y, ky) ;
								r.add(transform, Z, kz) ;
								r2 = r.squaredDistance() ;
								//System.out.println(kx+"|"+ky+"|"+kz+": "+r2) ;
								if (r2 < cutoff2) {
									ri2 = 1d / r2;
									ri6 = sigma * sigma * ri2;
									ri6 = ri6 * ri6 * ri6;
									ri12 = ri6 * ri6;
									pe += epsilon * (ri12 - ri6);
									force = ri2 * 6.0 * epsilon * ((2.0 * ri12) - ri6);
									ai.f.add(r, force) ;
								}
							}
				}
			}
		}
	}

	public void scaleTemp() {
		if (ts % 10 == 0) {
			if (scaling) {
				double sum = 0.0 ;
				for (int i = 0; i < 10; i++)
					sum += keArray[i];
				sum /= 10;
				double scaleFactor = Math.pow((KB * atomsNumber * applTemp) / sum, 1d/3);
				for(Atom a : atoms)
					a.v.times(scaleFactor) ;
			}
		}
		ts++;
	}

	public void measurements() {
		double vsqr = 0.0;
		ke = 0.0;
		te = 0.0;
		for(Atom a : atoms)
			vsqr += a.v.squaredDistance() ;
		ke = 0.5 * mass * vsqr;
		te = ke + pe;
		temp = ke / (atomsNumber * KB);
		keArray[kePointer] = ke;
		kePointer++;
		kePointer = kePointer % 10;
	}

	public void integrate1() {
		integrate_step1();
		zero_forces();
	}

	public void integrate2() {
		integrate_step2();
		boundary_cond();
	}

	private void zero_forces() {
		for(Atom a : atoms)
			a.f.clear() ;
	}

	private void integrate_step1() {
		double t2m = 0.5d * TIMESTEP * TIMESTEP / mass ;
		double tm = .5d * TIMESTEP / mass ;
		for (Atom a : atoms) {
			a.add(a.v, TIMESTEP).add(a.f, t2m) ;
			a.v.add(a.f, tm) ;
		}
	}

	private void integrate_step2() {
		double tm = .5d * TIMESTEP / mass ;
		for (Atom a : atoms)
			a.v.add(a.f, tm) ;
	}

	private void boundary_cond() {
		for(Atom a : atoms) {
            PointND retro = a.times(inverse) ;
			if(retro.get(X) > 1d)
				a.add(transform, X, -2d) ;
			else if(retro.get(X) <= -1d)
				a.add(transform, X, 2d) ;
			if(retro.get(Y) > 1d)
				a.add(transform, Y, -2d) ;
			else if(retro.get(Y) <= -1d)
				a.add(transform, Y, 2d) ;
			if(retro.get(Z) > 1d)
				a.add(transform, Z, -2d) ;
			else if(retro.get(Z) <= -1d)
				a.add(transform, Z, 2d) ;
		}
	}

	public void reset() {
		Atom a;
		double x, y, z ;
		double vx, vy, vz;

		int atomd = (int) Math.pow((double) atomsNumber-.5d, 1/3d) + 1 ;
		int cube = atomd * atomd * atomd ;

		Iterator<Atom> e = atoms.iterator() ;

	out:
		for (int i = 0; i < atomd; i++) {
			for (int j = 0; j < atomd; j++) {
				for (int k = 0; k < atomd; k++) {
					if(e.hasNext()) {
						a = e.next();
						x = ((2*i + 1d) / atomd) - 1d ;
						y = ((2*j + 1d) / atomd) - 1d ;
						z = ((2*k + 1d) / atomd) - 1d ;
						vx = Math.sqrt((KB * applTemp) / mass) * rand.nextGaussian();
						vy = Math.sqrt((KB * applTemp) / mass) * rand.nextGaussian();
						vz = Math.sqrt((KB * applTemp) / mass) * rand.nextGaussian();
						
						a.clear() ;
						PointND p = new PointND(x,y,z) ;
						a.add(p.times(transform)) ;
						a.v.set(X, vx) ;
						a.v.set(Y, vy) ;
						a.v.set(Z, vz) ;
						a.f.clear() ;
					} else
						break out ;
				}
			}
		}

		fireInit(new SimulationEvent(this)) ;
	}

	public int getAtomsNumber() {
		return atomsNumber;
	}

	public void setAtomsNumber(int atomsNumber) {
		int old = this.atomsNumber ;
		if(atomsNumber!=old) {
			this.atomsNumber = atomsNumber ;
			if(atomsNumber>old) {
				for(int i=old;i<atomsNumber;i++)
					atoms.add(new Atom(0,0,0,0,0,0));
			} else
				atoms.subList(atomsNumber, old).clear() ;
			fireChanged(new SimulationEvent(this, SimulationEvent.Property.N_ATOMS));
			resetDensity();
			reset() ;
		}
	}
	
    public Matrix getTransform() {
        return transform;
    }

    public synchronized void setTransform(Matrix A) {
		if(transform!=null) {
			Matrix transition = A.times(inverse) ;
			for(Atom a : atoms)
				a.setTo(a.times(transition)) ;
		}
        transform = A ;
        inverse = A.inverse() ;
		resetDensity();
		fireChanged(new SimulationEvent(this, SimulationEvent.Property.TRANSFORM)) ;
    }

	public double getEpsilon() {
		return epsilon;
	}

	public double getSigma() {
		return sigma;
	}

	public double getCutoff() {
		return cutoff;
	}

	public synchronized void setCutoff(double c) {
		this.cutoff = c;
	}

	public synchronized void setSigma(double n) {
		this.sigma = n;
	}

	public synchronized void setEpsilon(double n) {
		this.epsilon = n;
	}

	public boolean isScaling() {
		return scaling;
	}

	public synchronized void setScaling(boolean state) {
		scaling = state;
	}

	public double getApplTemp() {
		return applTemp;
	}

	public synchronized void setApplTemp(double temp) {
		applTemp = temp;
		fireChanged(new SimulationEvent(this, SimulationEvent.Property.TEMPERATURE));
	}

	public double getDensity() {
		return density;
	}

	public synchronized boolean setDensity(double new_dens) {
		if (new_dens != density) {
			setTransform(transform.times(Math.pow(density / new_dens, 1/3d)));
			return true ;
		}
		return false ;
	}

	private void resetDensity() {
		double new_dens = atomsNumber / Math.abs(transform.det()) / 8 ;
		if (new_dens != density) {
			density = new_dens ;
			fireChanged(new SimulationEvent(this, SimulationEvent.Property.DENSITY));
		}
	}

	public double getRadius() {
		PointND e1 = new PointND(3).add(transform, X, 1d) ;
		PointND e2 = new PointND(3).add(transform, Y, 1d) ;
		PointND e3 = new PointND(3).add(transform, Z, 1d) ;
		double r1 = e1.squaredDistance() ;
		double r2 = e2.squaredDistance() ;
		double r3 = e3.squaredDistance() ;
		return Math.sqrt(Math.max(r1, Math.max(r2, r3))) ;
	}


	private Vector<SimulationListener> listeners = new Vector<SimulationListener>() ;

	public synchronized void addSimulationListener(SimulationListener listener) {
		listeners.add(listener) ;
	}

	public synchronized void removeSimulationListener(SimulationListener listener) {
		listeners.remove(listener) ;
	}

	synchronized void fireUpdate(SimulationEvent evt) {
		for(SimulationListener l : listeners)
			l.update(evt) ;
	}

	synchronized void fireInit(SimulationEvent evt) {
		for(SimulationListener l : listeners)
			l.init(evt) ;
	}

	synchronized void fireChanged(SimulationEvent evt) {
		for(SimulationListener l : listeners)
			l.changed(evt) ;
	}
}
