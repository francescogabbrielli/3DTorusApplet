package it.francescogabbrielli.apps.torusapplet;

import Jama.Matrix;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * N-dimensional point (or vector)
 * 
 * @author Francesco Gabbrielli
 */
public class PointND {

	private double[] coords;

	public PointND(int dimension) {
		coords = new double[dimension];
	}

	public PointND(double... coords) {
		if (coords.length==0)
			throw new IllegalArgumentException("Null length array");
		this.coords = coords;
	}

	public boolean isInTorus() {
		return Arrays.stream(coords).noneMatch(coord -> coord <= -1 || coord > 1);
	}

	public int getDimension() {
		return coords.length;
	}

	public double get(int component) {
		return coords[component];
	}

	public void set(int component, double value) {
		coords[component] = value;
	}

	public void setTo(PointND other) { Arrays.setAll(coords, other::get); }

	public void add(PointND... others) {
		for (PointND other : others)
			for (int i=0; i<coords.length; i++)
				coords[i] += other.coords[i];
	}

	public PointND add(PointND other, double scalar) {
		for (int i=0; i<coords.length; i++)
			coords[i] += other.coords[i] * scalar;
		return this;
	}

    public PointND add(Matrix A, int column, double scalar) {
        for (int i=0; i<coords.length; i++)
            coords[i] += A.get(i, column) * scalar;
		return this;
    }

	public PointND times(double scalar) {
		for (int i=0; i<coords.length; i++)
			coords[i] *= scalar;
		return this;
	}

	public double times(PointND other) {
		return IntStream.range(0, coords.length)
				.mapToDouble(i -> coords[i] * other.coords[i]).sum();
	}

	public PointND times(Matrix A, PointND buffer) {
		buffer.clear();
		return timesImpl(A, buffer);
	}

    public PointND times(Matrix A) {
		return timesImpl(A, new PointND(coords.length));
	}

	private PointND timesImpl(Matrix A, PointND x) {
        int d = coords.length;
        for (int i=0; i<d; i++)
            for (int j=0; j<d; j++)
                x.coords[i] += A.get(i, j) * coords[j];
        return x;
    }


	@Override
	public String toString() {
		return Arrays.stream(coords).mapToObj(String::valueOf).collect(Collectors.joining(", "));
	}
	
	public double norm1() {
		return Arrays.stream(coords).map(Math::abs).sum();
	}

	public double norm2() {
		return Math.sqrt(squaredDistance());
	}

	public double squaredDistance() {
		return Arrays.stream(coords).map(coord -> coord * coord).sum();
	}

	public double squaredDistance(final PointND other) {
		//if(other.getDimension()!=coords.length)
		//	throw new IllegalArgumentException(String.format("Cannot compute distance of points of different dimensions: %d <> %d", coords.length, other.getDimension()));
		return IntStream.range(0, coords.length)
				.mapToDouble(i -> coords[i]*coords[i] + other.coords[i]*other.coords[i] - 2*coords[i]*other.coords[i])
				.sum();
	}

	public void clear() {
		Arrays.fill(coords, 0);
	}

    public Matrix asMatrix() {
        return new Matrix(coords, 3);
    }

}
