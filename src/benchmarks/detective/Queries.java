package detective;

import io.IO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Queries {

	private Map<String, List<String>> groups;
	
	public Queries (String... varargs)
	throws FileNotFoundException, IOException {
		this.groups = new HashMap<String, List<String>>();
		for (String fname : varargs) {
			groups.put(fname, IO.load(fname, 30));
		}
	}
	
	public List<String> getAll () {
		List<String> queries = new ArrayList<>();
		for (List<String> group : groups.values()) queries.addAll(group);
		return queries;
	}
	
	public List<String> getGroup (String name) {
		return groups.get(name);
	}
	
	public String composition (Iterable<String> iterable) {
		
		// count names from each group
		int sz = 0;
		Map<String, Integer> counts = new HashMap<>();
		counts.put("other", 0);
		aggregation: for (String str : iterable) {	
			sz++;
			for (String grpName : groups.keySet()) {				
				List<String> grp = groups.get(grpName);
				if (grp.contains(str)) {
					if (counts.containsKey(grpName)) counts.put(grpName, counts.get(grpName) + 1);
					else counts.put(grpName, 1);
					continue aggregation;
				}
			}
			counts.put("other", counts.get("other") + 1);
		}
		
		// sort groups before building string
		List<String> groupNames = new ArrayList<>();
		for (String gp : groups.keySet()) groupNames.add(gp);
		Collections.sort(groupNames);
		
		// build string and return
		StringBuilder sb = new StringBuilder();
		for (String gp : groupNames) {
			if (counts.containsKey(gp)){
				sb.append(gp + ": " + asPct(counts.get(gp), sz) + "\n");
			}
		}
		if (counts.get("other") > 0) sb.append("other :" + asPct(counts.get("other"), sz) + "\n");
		
		return sb.toString();
	}
	
	private String asPct (int numer, int denom) {
		double d = 100.0 * numer / denom;
		return "" + Math.round(d*10)/10.0 + "%";
	}
	
}
