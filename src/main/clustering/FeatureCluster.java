package clustering;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FeatureCluster implements Iterable<Vector> {

	// Fields.
	// -------------------------------------------------------------------------
	private Vector centroid;
	private List<Vector> vectors;
	
	
	// Constructors.
	// -------------------------------------------------------------------------
	
	public FeatureCluster (Vector centroid) {
		this.centroid = centroid;
		this.vectors = new ArrayList<>();
	}
	
	public FeatureCluster (Vector centroid, List<Vector> vectors) {
		this.centroid = centroid;
		this.vectors = vectors;
	}
	
	
	// Instance methods.
	// -------------------------------------------------------------------------
	
	public void add (Vector vector) {
		this.vectors.add(vector);
	}

	public List<Vector> getVectors () {
		return this.vectors;
	}

	public Vector getCentroid() {
		return this.centroid;
	}
	
	
	// Iterable stuff.
	// -------------------------------------------------------------------------
	
	@Override
	public Iterator<Vector> iterator() {
		return this.vectors.iterator();
	}
	
	
}
