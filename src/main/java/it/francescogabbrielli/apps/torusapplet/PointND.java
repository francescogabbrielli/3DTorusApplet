package it.francescogabbrielli.apps.torusapplet;

import Jama.Matrix;

/**
 * Punto (o vettore) n-dimensionale
 * 
 * @author Francesco Gabbrielli
 */
public class PointND {

	private double[] coords ;

	public PointND(int dimension) {
		coords = new double[dimension] ;
	}

	public PointND(double... coords) {
		if(coords.length==0)
			throw new IllegalArgumentException("Null length array") ;
		this.coords = coords ;
	}

	public int getDimension() {
		return coords.length ;
	}

	public double get(int component) {
		return coords[component] ;
	}

	public void set(int component, double value) {
		coords[component] = value ;
	}

	public void setTo(PointND other) {
		for(int i=0;i<coords.length;i++)
			coords[i] = other.coords[i] ;
	}

	public void add(PointND... others) {
		for(PointND other : others)
			for(int i=0;i<coords.length;i++)
				coords[i] += other.coords[i] ;
	}

	public PointND add(PointND other, double scalar) {
		for(int i=0;i<coords.length;i++)
			coords[i] += other.coords[i] * scalar ;
		return this ;
	}

    public PointND add(Matrix A, int column, double scalar) {
        for(int i=0;i<coords.length;i++)
            coords[i] += A.get(i, column) * scalar ;
		return this ;
    }

	public PointND times(double scalar) {
		for(int i=0;i<coords.length;i++)
			coords[i] *= scalar ;
		return this ;
	}

	public double times(PointND other) {
		double ret = 0d ;
		for(int i=0;i<coords.length;i++)
			ret += coords[i] * other.coords[i] ;
		return ret ;
	}

	public PointND times(Matrix A, PointND buffer) {
		buffer.clear();
		return timesImpl(A, buffer) ;
	}

    public PointND times(Matrix A) {
		return timesImpl(A, new PointND(coords.length)) ;
	}

	private final PointND timesImpl(Matrix A, PointND x) {
        int d = coords.length ;
        for(int i=0;i<d;i++)
            for(int j=0;j<d;j++)
                x.coords[i] += A.get(i, j) * coords[j] ;
        return x ;
    }


	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer("(") ;
		for(double coord : coords)
			ret.append(coord).append(", ") ;
		ret.deleteCharAt(ret.length()-2) ;
		ret.append(")") ;
		return ret.toString() ;
	}
	
	public double norm1() {
		double norm = 0 ;
		for(double coord : coords)
			norm += Math.abs(coord) ;
		return norm ;
	}

	public double norm2() {
		return Math.sqrt(squaredDistance()) ;
	}

	public double squaredDistance() {
		double sd = 0d ;
		for(int i=0;i<coords.length;i++)
			sd += coords[i]*coords[i] ;
		return sd ;
	}

	public double squaredDistance(PointND other) {
		//if(other.getDimension()!=coords.length)
		//	throw new IllegalArgumentException(String.format("Cannot compute distance of points of different dimensions: %d <> %d", coords.length, other.getDimension())) ;
		double sd = 0d ;
		for(int i=0;i<coords.length;i++)
			sd += coords[i]*coords[i] + other.coords[i]*other.coords[i] - 2 * coords[i] * other.coords[i] ;
		return sd ;
	}

	public void clear() {
		for(int i = 0; i<coords.length ; i++)
			coords[i] = 0 ;
	}

    public Matrix asMatrix() {
        return new Matrix(coords, 3) ;
    }

}
