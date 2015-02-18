package detective;

import io.IO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import clustering.FeatureCluster;
import clustering.FeatureClusterer;
import dns.Host;

public class ClusteringTrial {
	
	private static final String HOST_FILE = "mix-kebab1.txt";
	
	public static void main (String[] args)
	throws Exception {

		System.out.println("PERFORMING RANDOM CLUSTERING TRIAL.");
		
		// generate some test hosts
		Queries queries = new Queries("good.txt", "conficker.txt", "spam.txt", "enum.txt");
		generateTestSet(queries);
		
		// load hosts into memory
		System.out.println("Loading hosts...");
		List<Host> hosts = IO.loadHosts(HOST_FILE);
		int sz = 0;
		for (Host host : hosts) sz += host.getQueries().size();
		System.out.println(sz + " queries to be clustered.");
		
		// configure clustering
		FeatureClusterer.SET_NUM_CLUSTERS(4);
		FeatureClusterer.SET_SUBSET_SIZE(5);
		
		// cluster
		System.out.println("Finished loading hosts. Clustering...");
		List<FeatureCluster> clusters = FeatureClusterer.cluster(hosts);
		sz = 0;
		for (FeatureCluster cl : clusters) sz += cl.getQueries().size();
		System.out.println(sz + " queries in " + clusters.size() + " clusters.");
		
		// display composition of each cluster
		System.out.println("\n----------RESULTS----------\n\n");
		for (FeatureCluster cl : clusters) {
			String composition = queries.composition(cl.getQueries());
			System.out.println(composition);
		}
		
	}

	public static void generateTestSet (Queries queries)
	throws Exception {
		// parameters
		final int MAX_NUM_QUERIES = 30;
		int NUM_HOSTS = 8;
		
		// make host map
		Map<Integer, ArrayList<String>> hostMap = new HashMap<Integer, ArrayList<String>>();
		for (int i = 0; i < NUM_HOSTS; i++) {
			hostMap.put(i, new ArrayList<String>());
		}
	
		// randomly assign to hosts
		Random rand = new Random();
		rand.setSeed(System.nanoTime());
		for (String str : queries.getAll()){
			int randInt = rand.nextInt(NUM_HOSTS);
			hostMap.get(randInt).add(str);
		}
	
		// create the list of hosts
		List<Host> hosts = new ArrayList<>();
		final String base = "192.168.1.";
		for (int i = 0; i < NUM_HOSTS; i++){
			List<String> querylist = hostMap.get(i);
			String hostName = base + "i";
			Host host = new Host(hostName, querylist);
			hosts.add(host);
		}
	
		// save, display msg
		IO.saveHosts(hosts, HOST_FILE);
		System.out.println("Finished generating test hosts. Saved to " + HOST_FILE);
	}
	
}
