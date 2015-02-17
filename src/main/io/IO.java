package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import clustering.FeatureCluster;
import clustering.Vector;
import dns.Host;

public class IO {

	// Fields.
	// --------------------------------------------------------------------------------
	
	private static final String DIR_OUTPUT = "src/output/".replace('/', File.separatorChar);
	private static final String DIR_RESOURCES = "src/resources/".replace('/', File.separatorChar);
	

	// Class methods.
	// --------------------------------------------------------------------------------
	
	/**
	 * Clear all files in the output folder.
	 */
	private static void clearOutputFolder(){
		File directory = new File(DIR_OUTPUT);
		for (File file : directory.listFiles()){
			file.delete();
		}
	}
	
	
	/**
	 * Load a list of hosts from file.
	 * @param fname: name of file containing the hosts.
	 * @return list of hosts.
	 * @throws FileNotFoundException: if file could not be found.
	 * @throws IOException: if error occurred during reading.
	 */
	public static List<Host> load (String fname)
	throws FileNotFoundException, IOException{
		
		// open file
		File file = new File(DIR_RESOURCES + fname);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		
		// initialise data structures
		List<Host> hosts = new ArrayList<Host>();
		String line = null;
		
		// keep track of current thing being read
		String ip = null;
		List<String> queries = new ArrayList<>();
		
		while ((line = br.readLine()) != null){
			
			if (line.trim().length() == 0) continue;
			
			if (!line.startsWith("\t")){
				if (ip != null) hosts.add(new Host(ip, queries));
				ip = line.trim();
				queries = new ArrayList<>();
			}
			else {
				String query = line.trim();
				queries.add(query);
			}

		}
		
		fr.close();
		br.close();
		
		// done
		return hosts;
	}
	
	/**
	 * Save a list of clusters to several files, one per cluster.
	 * @param clusters: the clusters to save.
	 * @param bareBones: if false, some meta info about each cluster is saved to the head of the file.
	 * @throws FileNotFoundException: if directory to save into could not be found.
	 * @throws IOException: if exception occurred during writing.
	 */
	public static void saveCluster (List<FeatureCluster> clusters, boolean metaInfo)
	throws FileNotFoundException, IOException{
		
		// clear output folder
		clearOutputFolder();
		
		// open file
		int i = 0;
		String fname;
		
		for (FeatureCluster cluster : clusters){
		
			// open file
			String num = i < 10 ? "0" + i : ""+i;
			fname = DIR_OUTPUT + "cluster" + num + ".txt";
			File file = new File(fname);
			PrintStream ps = new PrintStream(file);
			
			// save meta info
			if (metaInfo) {
				Vector centroid = cluster.getCentroid();
				
				ps.println("========== META ");
				ps.println(cluster.numVectors() + " vectors in this cluster.");
				ps.println(cluster.numQueries() + " queries in this cluster.");
				ps.println("Centroid: ");
				ps.println(centroid);
				ps.println("========== META ");
				
			}
			
			// output each domain name
			int count = 0;
			for (Vector vector : cluster){
				List<String> queries = vector.getQueries();
				count += queries.size();
				for (String query : queries){
					ps.println(query);
				}
			}
			System.out.println("Cluster " + i + " has " + count + " entries.");
			
			for (int j = 0; j < 20; j++){
				ps.println();
			}
			
			i++;
			ps.close();
		}
		
	}
	
}
