package clustering.feature;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vectors.Vector;
import vectors.Vectors;
import clustering.Cluster;
import clustering.ClusterStrategy;
import clustering.feature.aggregation.AggregationStrategy;
import clustering.feature.aggregation.BasicAggregate;
import clustering.feature.aggregation.Options;
import clustering.feature.aggregation.VectorAggregate;
import dns.Host;

public class FeatureClustering implements ClusterStrategy {

	private static int SUBSET_SIZE = 5;
	private static int NUM_CLUSTERS = 4;
	private static int MAX_ITERATIONS = 5;
	
	// Configruation methods.
	// ------------------------------------------------------------
	
	public static void setSubsetSize (int sz) { SUBSET_SIZE = sz; }
	public static void setNumClusters (int cl) { NUM_CLUSTERS = cl; }
	public static void setMaxIterations (int it) { MAX_ITERATIONS = it; }
	private final AggregationStrategy agg;
	
	public FeatureClustering (Options option) {
		switch (option) {
		case BASIC_AGGREGATE:
			this.agg = new BasicAggregate();
			break;
		case VECTOR_AGGREGATE:
			this.agg = new VectorAggregate();
			break;
		default:
			throw new NullPointerException("Unknown aggregate option: " + option);
		}
	}
	

	// Public methods.
	// ------------------------------------------------------------
	
	@Override
	public List<Cluster> cluster(List<Host> hosts) {
		
		// aggregate queries
		List<Vector> vectors = agg.aggregate(hosts,  SUBSET_SIZE);
		
		// assign random centroids
		List<FeatureCluster> clusters = assignRandomCentroids(vectors);
		
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

	
	
	// Helper methods.
	// ------------------------------------------------------------
	
	/**
	 * Choose some random vectors to use as initial centroids.
	 * @param vectors: all the vectors that are being clustered.
	 * @return a list of feature clusters, with centroids, but no vectors in them.
	 */
	private List<FeatureCluster> assignRandomCentroids (List<Vector> vectors) {
		
		// sanity check
		if (NUM_CLUSTERS > vectors.size())
			throw new UnsupportedOperationException("Clustering with more centroids than vectors.");
		
		// assign centroids
		Set<Vector> centroids = new HashSet<>();
		while (centroids.size() < NUM_CLUSTERS) {
			int rand = (int) (Math.random() * vectors.size());
			centroids.add(vectors.get(rand));
		}
		
		// make the clusters, return
		List<FeatureCluster> clusters = new ArrayList<>();
		for (Vector centroid : centroids) clusters.add(new FeatureCluster(centroid));
		return clusters;
		
		
	}
	
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
	private class FeatureCluster {
		
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
