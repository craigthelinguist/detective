package clustering.feature.aggregation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import vectors.Vector;

public class Grid {
	
	private final Map<Vector, List<Vector>> map;
	private final double MESH_SIZE;
	private final int DIMENSIONS;
	
	protected Grid(Vector[] vectors, double meshSize, int dimensions) {
		this.map = new HashMap<>();
		this.MESH_SIZE = meshSize;
		this.DIMENSIONS = dimensions;
		for (Vector v : vectors) {
			if (v == null) break;
			insert(v);
		}
	}
	
	private Vector index(Vector vector) {
		double[] index = new double[vector.dimensions()];
		for (int i = 0; i < vector.dimensions(); i++) {
			index[i] = Math.floor(vector.get(i) / MESH_SIZE);
		}
		return new Vector(index);
	}
	
	protected void insert(Vector vector) {
		Vector index = index(vector);
		List<Vector> block = get(index);
		block.add(vector);
	}
	
	private List<Vector> cartesian (List<double[]> listOfDubs) {
		
		// start off with the first double[] by itself
		int i = 0;
		List<double[]> dubs = new ArrayList<>();
		double[] first = listOfDubs.get(0);
		for (double d : first) { 
			double[] dubArray = new double[DIMENSIONS];
			dubArray[0] = d;
			dubs.add(dubArray);
		}
		
		// go through the remaining double[]s
		// append each of their values to each of the arrays in the current List<double[]>
		// copy the new double[] arrays to a new list
		for (i = 1; i < listOfDubs.size(); i++) {
			List<double[]> newDubs = new ArrayList<>();
			double[] iComponents = listOfDubs.get(i);
			for (double component : iComponents)  {
				for (double[] currDubs : dubs) {
					double[] ds = currDubs.clone();
					ds[i] = component;
					newDubs.add(ds);
				}
			}
			dubs = newDubs;
		}
		
		// map the final list of double[] to the corresponding vectors
		return dubs.stream().map(arr -> new Vector(arr))
							.collect(Collectors.toList());
		
	}
	
	protected List<Vector> neighbourhood (Vector v) {
		
		// index of vector whose neighbours we're gathering
		Vector centroid = index(v);
		
		// neighbours are within 1 block of any component of the index vector
		List<double[]> residuals = new ArrayList<>();
		for (int i = 0; i < centroid.dimensions(); i++) {
			double component = centroid.get(i);
			double[] dubArr = { component-1, component, component+1 };
			residuals.add(dubArr);
		}
		
		// cartesian product of the residuals gives us neighbouring index vectors
		// build up a list of the contents of each block
		List<Vector> neighbours = new ArrayList<>();
		for (Vector indxVector : cartesian(residuals)) {
			List<Vector> contents = get(indxVector);
			neighbours.addAll(contents);
		}
		
		// return the contents
		return neighbours;
		
	}
	
	protected List<Vector> get (Vector v) {
		if (!map.containsKey(v)) map.put(v, new ArrayList<>());
		return map.get(v);
	}
	
	protected static void main(String[] args) {
		
		Grid g = new Grid(null, 0, 2);
		double[][] dubArrArr = { {-1.0, 0.0, 1.0}, {2.0, 3.0, 4.0}};
		List<double[]> dubs = Arrays.asList(dubArrArr);
		List<Vector> cart = g.cartesian(dubs);
		for (Vector v : cart) {
			System.out.println(v);
		}
		
	}
	
}
