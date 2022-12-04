package it.francescogabbrielli.apps.torusapplet;

import java.awt.*;
import java.util.*;

public class RDFPanel extends Panel implements SimulationListener {
	
    private LinePlot plot;
    private int count = 0;
    private Label title;
    private int NAVERAGE = 100;
    private int SIZE = 500;
    private double[] data;

    public RDFPanel() {
		plot = new LinePlot();
		data = new double[SIZE];

		setLayout(new BorderLayout());
		setFont(new Font("TimesRoman",Font.PLAIN,14));
		title = new Label("Radial Distribution Function",Label.CENTER);
		add("North",title);
		add("South",new Label("r",Label.CENTER));
		add("West",new Label("g(r)",Label.RIGHT));
		add("Center",plot);
    }

    private void display(SimulationSystem s, double deltaR) {
		double factor,r;

		//TODO: check if this formula is right for 3D!!!
		double rad = s.getRadius();
		factor = (rad * rad * rad) /
			(2.0 * Math.PI * s.getAtomsNumber() * s.getAtomsNumber() * s.getAtomsNumber() * NAVERAGE * deltaR);
		
		for(int i = 0; i < SIZE; i++) {
			r = ((double)i + 0.5) * deltaR;
			data[i] = (data[i] * factor) / r;
		}
		plot.plotData(data,deltaR,0.0);
    }

    private void setTitle(double n) {
		String s = double2string(n * 100.0);
		title.setText("Radial Distribution Function  (Computing... "+s+"% )");
    }

    private String double2string(double n) {
		return( new Double( Math.rint(n) ).toString() );
    }

	public void update(SimulationEvent evt) {
		if(isEnabled()) {

			SimulationSystem s = evt.getSystem();

			Iterator<Atom> e1,e2;
			Atom a1,a2;
			PointND r = new PointND(3);
			double r2;
			double cutoff = 10.0;
			double cutoff2 = cutoff * cutoff;
			int n;
			double deltaR = cutoff / SIZE;

			count++;
			if(count == 1) {
				for(int i = 0; i < SIZE; i++) {
				data[i] = 0.0;
				}
			}
			if(count == NAVERAGE + 1) {
				display(s, deltaR);
				count = 1;
			}
			setTitle((double)count / NAVERAGE);

			e1 = s.atoms.iterator();
			//e2 = s.atoms.iterator();

			while(e1.hasNext()) {
				a1 = e1.next();
				e2 = s.atoms.iterator();
				while(e2.hasNext()) {
					a2 = e2.next();
					if(a2 != a1)						
						for(int kx=-1; kx<=1; kx++)
							for(int ky=-1; ky<=1; ky++)
								for(int kz=-1; kz<=1; kz++) {
									r.setTo(a1);
									r.add(a2, -1);
									r.add(s.transform, s.X, kx);
									r.add(s.transform, s.Y, ky);
									r.add(s.transform, s.Z, kz);
									r2 = r.squaredDistance();
									if(r2 < cutoff2) {
										n = (int) Math.round(Math.sqrt(r2) / deltaR);
										if(n >= SIZE) n = SIZE -1;
										data[n] = data[n] + 1.0;
									}
								}
				}
			}
		}
	}

	public void init(SimulationEvent evt) {}
	public void changed(SimulationEvent evt) {}


}
