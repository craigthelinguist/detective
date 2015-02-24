package wblang.functions.indigenous;

import java.util.HashMap;
import java.util.Map;

import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.ClusterPrim;
import wblang.primitives.Hash;
import wblang.primitives.Seq;
import wblang.primitives.Str;
import wblang.rules.Primitive;

public class FuncComposition extends Function {

	public FuncComposition (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncComposition () {
		super();
	}
	
	@Override
	public Primitive exec() {
		Seq<ClusterPrim> clusters = (Seq<ClusterPrim>)args()[0];
		Hash hash = (Hash) args()[1];
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (ClusterPrim cp : clusters) {
			Map<String,Double> counts = countUp(cp, hash);
			normalise(counts);
			sb.append("Cluster " + i + "\n");
			sb.append(formattedString(counts));
			sb.append("\n");
		}
		return new Str(sb.toString());
	}

	private Map<String, Double> countUp (ClusterPrim cp, Hash hash) {
		Map<String,Double> counts = new HashMap<>();
		domains: for (Str domain : cp.asList()) {
			for (Primitive k : hash) {
				Seq<Str> seq = (Seq<Str>)hash.get((Str)k);
				String key = k.toString();
				if (seq.contains(domain)) {
					if (counts.containsKey(key)) counts.put(key.toString(), counts.get(key) + 1);
					else counts.put(key.toString(), 1.0);
					continue domains;
				}
			}
			if (counts.containsKey("Other")) counts.put("Other", counts.get("Other") + 1);
			else counts.put("Other", 1.0);
		}
		return counts;
	}
	
	private void normalise (Map<String,Double> map) {
		double sum = 0.0;
		for (Double d : map.values()) sum += d;
		for (String str : map.keySet()) {
			map.put(str, map.get(str) / sum);
		}
	}
	
	private String formattedString (Map<String, Double> map) {
		StringBuilder sb = new StringBuilder();
		for (String key : map.keySet()) {
			double d = map.get(key);
			d = Math.round(d * 1000)/10;
			sb.append(key + ": " + d + "%\n");
		}
		return sb.toString();
	}
	
	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 2) {
			throw new TypeException("Takes exactly 2 arguments.");
		}
		if (!(args[0] instanceof Seq)) {
			throw new TypeException("First argument should be Seq<Cluster>");
		}
		if (!(args[1] instanceof Hash)) {
			throw new TypeException("Second argument should be Hash of Str -> Seq<Str>");
		}
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "The output from clustering, and a hash of string -> list of domains",
				"A formatted string telling you the composition of each cluster.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Seq<Cluster>",  "Seq<Str" }, new String[]{ "Str" });
	}

	@Override
	public String name() {
		return "composition";
	}

	@Override
	public Class returnType() {
		return Str.class;
	}

	
	
}
