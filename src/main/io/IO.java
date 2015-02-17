package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.hadoop.mapred.ClusterStatus;

import clustering.Vector;
import dns.Host;

public class IO {

	private static void clearOutputFolder(){
		String dir = "pleiades/src/output/";
		dir.replace('/', File.separatorChar);
		File directory = new File(dir);
		for (File file : directory.listFiles()){
			file.delete();
		}
	}
	
	public static List<Host> load(String fname)
	throws FileNotFoundException, IOException{
		
		clearOutputFolder();
		
		// open file
		String dir = "pleiades/src/resources/" + fname;
		dir.replace('/', File.separatorChar);
		File file = new File(dir);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		
		List<Host> hosts = new ArrayList<Host>();
		List<String> queries;
		String line = null;
		while ((line = br.readLine()) != null){
			
			char[] chararray = line.toCharArray();
			
			StringBuilder sb = new StringBuilder();
			int i = 0;
			
			while (chararray[i] != '\t') sb.append(chararray[i++]);
			i++;
			String ip = sb.toString();
			
			queries = new ArrayList<String>();
			StringBuilder query = new StringBuilder();
			for (; i < chararray.length; i++){
				if (chararray[i] == ','){
					i++; // get rid of comma and space
					queries.add(query.toString().trim());
					query = new StringBuilder();
				}
				query.append(chararray[i]);
			}
			
			hosts.add(new Host(ip, queries));

		}
		
		fr.close();
		br.close();
		
		// done
		return hosts;
	}
	
	public static void saveCluster(Collection<List<Vector>> clusters)
	throws FileNotFoundException, IOException{
		
		// open file
		String dir = "pleiades/src/output/";
		dir.replace('/', File.separatorChar);
		
		int i = 0;
		String fname;
		
		for (List<Vector> cluster : clusters){

			// open file
			String num = i < 10 ? "0" + i : ""+i;
			fname = dir + "cluster" + num + ".txt";
			File file = new File(fname);
			PrintStream ps = new PrintStream(file);
			
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
