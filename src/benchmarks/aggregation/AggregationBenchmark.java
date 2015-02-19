package aggregation;

import io.IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import clustering.feature.aggregation.Aggregator;
import vectors.Vector;
import dns.Host;

public class AggregationBenchmark {
	
	private AggregationBenchmark() {}
	
	private void save (String fname, List<Vector> aggregates)
	throws IOException {
		
		// open writer
		File file = new File(IO.DIR_OUTPUT_AGGREGATION + fname);
		PrintStream ps = new PrintStream(file);
		
		// convert back into a format appropriate for output
		Map<Host, List<List<String>>> map = new HashMap<>();
		for (Vector vect : aggregates) {
			Host host = vect.getHost();
			if (!map.containsKey(host)) map.put(host, new ArrayList<List<String>>());
			List<String> queries = vect.getQueries();
			map.get(host).add(queries);
		}
		
		// save that shit
		for (Host host : map.keySet()) {
			ps.println("===============================");
			ps.println(host + "\n");
			
			for (List<String> queries : map.get(host)) {
				for (String str : queries) {
					ps.println("\t" + str);
				}
			}
			ps.println("\n\n");
		}
		
	}
	
	private List<Host> load (String fname)
	throws IOException {
		
		// open file
		File file = new File(IO.DIR_RESOURCES + fname);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		
		// initialise data structures
		List<Host> hosts = new ArrayList<Host>();
		String line = null;
		
		// keep track of current thing being read
		String ip = null;
		List<String> queries = new ArrayList<>();
		
		while ((line = br.readLine()) != null) {
			
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

	public static void main (String[] args)
	throws IOException {
		AggregationBenchmark benchmark = new AggregationBenchmark();
		List<Host> hosts = benchmark.load("mix-kebab1.txt");
		final int SUBSET_SIZE = 5;
		List<Vector> vectors1 = Aggregator.basicAggregate(hosts, SUBSET_SIZE);
		List<Vector> vectors2 = Aggregator.entropyAggregate(hosts, SUBSET_SIZE);
		String fnameOut1 = "comparison-basic";
		String fnameOut2 = "comparison-entropy";
		benchmark.save(fnameOut1, vectors1);
		benchmark.save(fnameOut2, vectors2);
		System.out.printf("Finished comparison.\nOutput saved to %s in the files:\n\t- %s\n\t- %s\n", IO.DIR_OUTPUT_AGGREGATION, fnameOut1, fnameOut2);
	}
	
}
