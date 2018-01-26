package it.francescogabbrielli.torusapplet;

public class Atom extends PointND {

	public PointND v ;
	public PointND f;

	public Atom(double x, double y, double z, double vx, double vy, double vz) {
		super(x, y, z);
		v = new PointND(vx,vy,vz) ;
		f = new PointND(3) ;
	}

	@Override
	public String toString() {
		return super.toString() + v.toString();
	}

}
