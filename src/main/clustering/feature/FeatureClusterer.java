package clustering.feature;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vectors.Vector;
import vectors.Vectors;
import clustering.Cluster;
import clustering.ClusterStrategy;
import clustering.feature.aggregation.AggregateStrategy;
import clustering.feature.aggregation.BasicAggregate;
import clustering.feature.aggregation.VectorAggregate;
import clustering.feature.assignment.AssignmentStrategy;
import dns.Host;

public class FeatureClusterer implements ClusterStrategy {
	
	// Fields.
	// ------------------------------------------------------------
	
	private int SUBSET_SIZE = 5;
	private int NUM_CLUSTERS = 4;
	private int MAX_ITERATIONS = 5;	
	private final AggregateStrategy aggreg;
	private final AssignmentStrategy assign;
	
	
	
	// Constructors.
	// ------------------------------------------------------------
	
	public FeatureClusterer (AggregateStrategy aggreg, AssignmentStrategy assign) {
		this.aggreg = aggreg;
		this.assign = assign;
	}

	
	
	// Getters and setters.
	// ------------------------------------------------------------
	
	public void setSubsetSize (int sz) { SUBSET_SIZE = sz; }
	public void setNumClusters (int cl) { NUM_CLUSTERS = cl; }
	public void setMaxIterations (int it) { MAX_ITERATIONS = it; }
	
	public int getSubsetSize () { return SUBSET_SIZE; }
	public int getNumClusters () { return NUM_CLUSTERS; }
	public int getMaxIterations () { return MAX_ITERATIONS; }
	public String getAggregationStrategy () { return aggreg.toString(); }
	public String getAssignmentStrategy () { return assign.toString(); }
	
	
	
	// Public methods.
	// ------------------------------------------------------------
	
	/**
	 * Perform clustering.
	 * @param hosts: list of hosts and the queries they sent.
	 * @return a list of clusters
	 */
	@Override
	public List<Cluster> cluster(List<Host> hosts) {
		
		// aggregate queries
		List<Vector> vectors = aggreg.aggregate(hosts);
		
		// assign random centroids
		List<Vector> initialCentroids = assign.assignCentroids(vectors);
		
		// create clusters from those centroids
		List<FeatureCluster> clusters = new ArrayList<>();
		for (Vector centroid : initialCentroids) {
			clusters.add(new FeatureCluster(centroid));
		}
		
		// perform K-Means clustering
		int iterations = 0;
		while (iterations++ < MAX_ITERATIONS) {
			for (Vector vector : vectors) assignToBestCluster(vector, clusters);
			if (iterations != MAX_ITERATIONS){
				for (FeatureCluster cl : clusters) cl.adjustCentroid();
			}
		}
		
		// map each FeatureCluster to Cluster
		List<Cluster> cls = new ArrayList<>();
		for (FeatureCluster cl : clusters) {
			Cluster c = cl.unpack();
			cls.add(c);
		}
		
		return cls;
		
	}

	public List<FeatureCluster> cluster(List<Host> hosts, boolean featureCluster) {
		
		// aggregate queries
		List<Vector> vectors = aggreg.aggregate(hosts);
		
		// assign random centroids
		List<Vector> initialCentroids = assign.assignCentroids(vectors);
		
		// create clusters from those centroids
		List<FeatureCluster> clusters = new ArrayList<>();
		for (Vector centroid : initialCentroids) {
			clusters.add(new FeatureCluster(centroid));
		}
		
		// perform K-Means clustering
		int iterations = 0;
		while (iterations++ < MAX_ITERATIONS) {
			for (Vector vector : vectors) assignToBestCluster(vector, clusters);
			if (iterations != MAX_ITERATIONS){
				for (FeatureCluster cl : clusters) cl.adjustCentroid();
			}
		}
		
		return clusters;
		
		
	}
	
	
	// Helper methods.
	// ------------------------------------------------------------

	/**
	 * Check all the clusters and put vector into the closest one. The distance to each cluster is determined by the
	 * distance from its centroid.
	 * @param vector: vector to be assigned to a cluster
	 * @param clusters: list of all clusters
	 */
	private void assignToBestCluster (Vector vector, List<FeatureCluster> clusters) {
		double bestDistance = 0.0;
		FeatureCluster best = null;
		for (FeatureCluster cluster : clusters){
			Vector centroid = cluster.getCentroid();
			if (best == null || Vectors.distance(centroid, vector) < bestDistance) {
				best = cluster;
				bestDistance = Vectors.distance(centroid, vector);
			}
		}
		best.add(vector);
	}
	
	
	
	// Data structures.
	// ------------------------------------------------------------
	
	/**
	 * An intermediary representation of the final clusters. Stores FeatureVectors.
	 * @author aaroncraig
	 */
	public class FeatureCluster {
		
		private Vector centroid;
		private List<Vector> vectors;
		
		public FeatureCluster (Vector centroid) {
			this.centroid = centroid;
			this.vectors = new ArrayList<>();
		}
		
		/**
		 * Add a vector to this cluster.
		 * @param vector: vector to add.
		 */
		public void add (Vector vector) {
			this.vectors.add(vector);
		}
		
		/**
		 * Get the centroid representing this cluster.
		 * @return Vector
		 */
		public Vector getCentroid () {
			return this.centroid;
		}
		
		/**
		 * Move this FeatureCluster's centroid to the middle of its vectors. Remove all vectors from this cluster.
		 */
		public void adjustCentroid () {
			Vector mean = Vectors.mean(vectors);
			this.centroid = mean;
			this.vectors = new ArrayList<>();
		}
		
		/**
		 * Map this FeatureCluster to its corresponding cluster of domains..
		 * @return: Cluster
		 */
		public Cluster unpack () {
			List<String> domains = new ArrayList<>();
			for (Vector vect : vectors) domains.addAll(vect.getQueries());
			return new Cluster(domains);
		}
		
	}
	
	
}
