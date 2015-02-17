package clustering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dns.Host;

public class FeatureClusterer {


	// Constants and fields.
	// -------------------------------------------------------------------------
	
	private static final int SUBSET_SIZE = 10; // this many queries are aggregated into one feature vector for clustering
	private static final int NUM_CLUSTERS = 5; // number of clusters to output from k-means
	private static final int MAX_ITERATIONS = 40; // number of iterations to perform when clustering
	
	
	// Constructors.
	// -------------------------------------------------------------------------
	
	private FeatureClusterer(){}
	
	
	// Class methods.
	// -------------------------------------------------------------------------
	
	public static List<FeatureCluster> cluster (List<Host> hosts) {
		
		// for each host partition their queries into subsets and make a feature vector per subset
		List<Vector> vectors = makeFeatureVectors(hosts);
		
		return null;
	}
	
	// Methods related to clustering vectors.
	// -------------------------------------------------------------------------
	
	/**
	 * Perform KMeans clustering on the given list of vectors.
	 * @param vectors: the vectors to be clustered
	 * @return a list of clusters
	 */
	private static List<FeatureCluster> KMeans (List<Vector> vectors) {
		int iterations = 0;
		List<FeatureCluster> clusters = null;
		do {
			clusters = assignCentroids(vectors, clusters);
			for (Vector vector : vectors) assignToBestCluster(vector, clusters);
		} while (iterations < MAX_ITERATIONS);
		return clusters;
	}
	
	/**
	 * Check all the clusters and put vector into the closest one. The distance to each cluster is determined by the
	 * distance from its centroid.
	 * @param vector: vector to be assigned to a cluster
	 * @param clusters: list of all clusters
	 */
	private static void assignToBestCluster (Vector vector, List<FeatureCluster> clusters) {
		double bestDistance = 0.0;
		FeatureCluster best = null;
		for (FeatureCluster cluster : clusters){
			Vector centroid = cluster.getCentroid();
			if (best == null || Vectors.distance(centroid, vector) < bestDistance) {
				best = cluster;
			}
		}
		best.add(vector);
	}
	
	/**
	 * Create a new list of clusters. If there are already clusters, the centroids for the new clusters will be the
	 * means of the old clusters. If there are no clusters the centroids are randomly chosen from among the vectors.
	 * @param vectors: the vectors being clustered
	 * @param clusters: the clusters so far, or null if there are none so far.
	 * @return list of clusters.
	 */
	private static List<FeatureCluster> assignCentroids (List<Vector> vectors, List<FeatureCluster> clusters) {
		Set<Vector> centroids = new HashSet<>();

		// choose random centroids if there are no clusters yet
		if (clusters == null){
			if (NUM_CLUSTERS > vectors.size()) throw new UnsupportedOperationException("Clustering with more centroids than vectors.");
			while (centroids.size() < NUM_CLUSTERS) {
				int rand = (int) (Math.random() * vectors.size());
				centroids.add(vectors.get(rand));
			}
		}
		
		// new centroids are the mean of the old centroids' clusters
		else {
			for (FeatureCluster cluster : clusters) {
				Vector centroid = Vectors.mean(cluster.getVectors());
				centroids.add(centroid);
			}			
		}
		
		// make and return the new clusters
		clusters = new ArrayList<>();
		for (Vector centroid : centroids) {
			clusters.add(new FeatureCluster(centroid));
		}
		return clusters;	
	}
	
	// Methods related to turning the queries into vectors.
	// -------------------------------------------------------------------------
	
	/**
	 * Create and return a list of feature vectors representing domains queried by the hosts.
	 * @param hosts: the hosts that sent queries. We want to cluster their querying behaviour.
	 * @return list of vectors, whose components represent features about the queries made.
	 */
	private static List<Vector> makeFeatureVectors (List<Host> hosts) {
		List<Vector> vectors = new ArrayList<>();
		for (Host host : hosts){
			for (List<String> subset : partitionQueries(host)) {
				Vector featureVector = Vectors.featureVector(host, subset);
				vectors.add(featureVector);
			}
		}
		return vectors;
	}
	
	/**
	 * Partition the queries sent by a host into lists of strings and return them.
	 * @param host: host that sent some queries we're interested in.
	 * @return a list of the lists of strings we've grouped together.
	 */
	private static List<List<String>> partitionQueries (Host host) {
		// sort queries
		List<String> queries = host.getQueries();
		Collections.sort(queries);
	
		// current subset
		List<List<String>> subsets = new ArrayList<>();
		List<String> subset = new ArrayList<>();
		int i = 0;
		
		// the partition
		for (String query : queries) {
			if (i % SUBSET_SIZE == 0 && !subset.isEmpty()) {
				subsets.add(subset);
				subset = new ArrayList<String>();
			}
			subset.add(query);
			i++;
		}
		return subsets;
	}
	
}
