package detective;

import java.util.Collection;
import java.util.List;

import clustering.FeatureCluster;
import clustering.FeatureClusterer;
import clustering.Vector;
import dns.Host;
import io.IO;

public class Testing {
	
	public static void TestIO ()
	throws Exception {
		System.out.println("Loading hosts...");
		List<Host> hosts = IO.load("hosts-testing.txt");
		System.out.println("Finished loading hosts.");
		for (Host host : hosts) {
			System.out.println("==============");
			System.out.println(host);
			System.out.println("---------");
			for (String query : host.getQueries()) {
				System.out.println(query);
			}
		}

		System.out.println(hosts.size() + " loaded.");
	}
	
	public static void ClusterTestFile ()
	throws Exception {

		FeatureClusterer.SET_NUM_CLUSTERS(4);
		FeatureClusterer.SET_SUBSET_SIZE(5);
		
		System.out.println("Loading hosts...");
		List<Host> hosts = IO.load("hosts-testing.txt");
		System.out.println("Finished loading hosts.");
		System.out.println("Clustering...");
		List<FeatureCluster> clusters = FeatureClusterer.cluster(hosts);
		System.out.println("Finished clustering.");
		System.out.println("Saving clusters...");
		IO.saveCluster(clusters, true);
		System.out.println("All done.");
		
	}
	
	public static void main (String[] args)
	throws Exception {
		ClusterTestFile();
	}

	

}
