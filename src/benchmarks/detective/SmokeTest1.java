package detective;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.IO;

import javax.swing.text.html.Option;

import vectors.Vector;
import vectors.Vectors;

import clustering.Cluster;
import clustering.feature.FeatureClustering;
import clustering.feature.FeatureClustering.FeatureCluster;
import clustering.feature.aggregation.Options;
import dns.Host;

/**
 * This test produces two 'ideal' centroids from a list of known good and known DGA. It will
 * then take those names, assign to hosts (so that the host has only good or bad DGA) and attempt
 * to cluster.
 * 
 * The things we're watching out for are:
 *  - Make sure they get put into the right cluster.
 *  - Make sure the 'ideal' centroids aren't too far from the centroids in the answer.
 *  
 * @author craigthelinguist
 *
 */
public class SmokeTest1 {

	private SmokeTest1 () {}
	
	public static void main (String[] args)
	throws IOException {
		
		// parameters
		final Options aggregateOption = Options.VECTOR_AGGREGATE;
		final int SUBSET_SIZE = 5;
		FeatureClustering.setSubsetSize(SUBSET_SIZE);
		
		// load queries
		List<String> queriesGood = IO.load("good.txt");
		List<String> queriesConficker = IO.load("conficker.txt");
		
		// take a random selection of conficker queries
		// so we have an equal number of good and conficker
		Set<Integer> indices = new HashSet<>();
		while (indices.size() < queriesGood.size()) {
			int randIndex = (int)(Math.random() * queriesConficker.size());
			indices.add(randIndex);
		}
		List<String> confickerSlice = new ArrayList<>();
		for (Integer intt : indices) {
			confickerSlice.add(queriesConficker.get(intt));
		}
		queriesConficker = confickerSlice;
		
		// make hosts from these domains
		List<Host> hosts = new ArrayList<>();
		hosts.addAll(makeRandomHosts(queriesGood, SUBSET_SIZE));
		hosts.addAll(makeRandomHosts(queriesConficker, SUBSET_SIZE));
		
		// make ideal centroids
		Vector centroidConficker = Vectors.featureVector(null, queriesConficker);
		Vector centroidGood = Vectors.featureVector(null, queriesGood);
		List<Vector> inputCentroids = new ArrayList<>();
		inputCentroids.add(centroidConficker);
		inputCentroids.add(centroidGood);
		
		// do clustering
		FeatureClustering fClustering = new FeatureClustering(aggregateOption);
		List<FeatureCluster> fClusters = fClustering.cluster(hosts, inputCentroids);
		
		// compare centroid distances
		List<Vector> outputCentroids = new ArrayList<>();
		for (FeatureCluster cl : fClusters) outputCentroids.add(cl.getCentroid());
		
		// original distance
		double d = Vectors.distance(centroidConficker, centroidGood);
		System.out.println("===================");
		System.out.println("RELATIVE POSITION OF TWO CENTROIDS\n");
		System.out.println("Distance between the two starting centroids is: ");
		System.out.println(d);
		d = Vectors.distance(outputCentroids.get(0), outputCentroids.get(1));
		System.out.println("Distance between output centroids is: ");
		System.out.println(d + "\n\n");
		
		
		double d1, d2;
		
		// conficker distance
		System.out.println("===================");
		System.out.println("DISTANCE FROM IDEAL CONFICKER CENTROID TO OUTPUT CENTROIDS\n");
		System.out.println("The distances from the ideal conficker to the output centroids are: ");
		d1 = Vectors.distance(centroidConficker, outputCentroids.get(0));
		d2 = Vectors.distance(centroidConficker, outputCentroids.get(1));
		System.out.println("Distance to cluster 1: " + d1);
		System.out.println("Distance to cluster 2: " + d2 + "\n\n");
		
		// good distance
		System.out.println("===================");
		System.out.println("DISTANCE FROM IDEAL GOOD CENTROID TO OUTPUT CENTROIDS\n");
		System.out.println("The distances from the ideal good to the output centroids are: ");
		d1 = Vectors.distance(centroidGood, outputCentroids.get(0));
		d2 = Vectors.distance(centroidGood, outputCentroids.get(1));
		System.out.println("Distance to cluster 1: " + d1);
		System.out.println("Distance to cluster 2: " + d2 + "\n\n");

		int count = 0;
		for (FeatureCluster fc : fClusters) {
			System.out.println("--------------");
			System.out.println("Cluster " + (count++));
			printComposition(fc, queriesGood, queriesConficker);
		}
		
	}

	private static void printComposition (FeatureCluster fc, List<String> good, List<String> conficker) {
		Collection<String> domains = fc.unpack().getDomains();
		int goodCount, confickerCount;
		goodCount = confickerCount = 0;
		for (String str : domains) {
			if (good.contains(str)) goodCount++;
			else if (conficker.contains(str)) confickerCount++;
			else System.out.println("what the fuck happened here?");
		}
		
		System.out.println("GOOD: " + goodCount + ", BAD: " + confickerCount);
		int total = domains.size();
		double compDGA = Math.round((100.0 * confickerCount / total) * 10) / 10;
		double compGood = Math.round((100.0 * goodCount / total) * 10) / 10;
		
		System.out.println("Conficker: " + compDGA + "%\nGood: " + compGood + "%");
		
		
	}
	
	private static List<Host> makeRandomHosts (List<String> queries, final int SUBSET_SIZE) {
		// take the queries and assign them to hosts
		String baseName = "1.1.1.";
		List<Host> hosts = new ArrayList<>();
		List<String> subset = new ArrayList<>();
		
		for (int i = 0; i < queries.size(); i++) {
			if (i % SUBSET_SIZE == 0 && subset.size() > 0){
				String hostName = baseName + hosts.size();
				hosts.add(new Host(hostName, subset));
				subset = new ArrayList<>();
			}
			subset.add(queries.get(i));
		}
		if (!subset.isEmpty()) {
			String hostName = baseName + hosts.size();
			hosts.add(new Host(hostName, subset));
		}
		
		return hosts;
		
	}
	
}
