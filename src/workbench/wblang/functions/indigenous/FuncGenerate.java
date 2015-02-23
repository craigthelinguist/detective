package wblang.functions.indigenous;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dns.Host;
import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.HostPrim;
import wblang.primitives.Int;
import wblang.primitives.Seq;
import wblang.primitives.Str;
import wblang.rules.Primitive;

public class FuncGenerate extends Function {

	public FuncGenerate (Primitive[] args) throws TypeException {
		super(args);
	}
	
	public FuncGenerate () {
		super();
	}
	
	@Override
	public Primitive exec() {
		
		// get args
		int numHosts = ((Int)args()[0]).toInt();
		Seq<Str> domains = (Seq<Str>) (args()[1]);
		
		// map of host num to list of domains
		Map<Integer, List<String>> hostMap = new HashMap<>();
		for (int i = 0; i < numHosts; i++) {
			hostMap.put(i, new ArrayList<String>());
		}
		
		// randomly assign to hosts
		for (Str domain : domains) {
			int randIndex = (int) (Math.random() * numHosts);
			hostMap.get(randIndex).add(domain.toString());
		}
		
		// turn into Seq<HostPrim>
		List<HostPrim> hosts = new ArrayList<>();
		for (Integer key : hostMap.keySet()) {
			List<String> queries = hostMap.get(key);
			Host h = new Host("Host " + key, queries);
			HostPrim hostPrim = HostPrim.make(h);
			hosts.add(hostPrim);
		}
		
		return new Seq<HostPrim>(hosts);
		
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 2) throw new TypeException("Function takes two arguments.");
		if (!(args[0] instanceof Int)) throw new TypeException("First argument should be number of hosts.");
		if (!(args[1] instanceof Seq)) throw new TypeException("Second argument should be Seq<Str>.");
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "The number of hosts and a sequence of domain names.", "That many hosts with randomly assigned domain names.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Int", "Seq<Str>" }, new String[]{ "Seq<Host>"});
	}

	@Override
	public String name() {
		return "generate";
	}

	@Override
	public Class returnType() {
		return Seq.class;
	}

}
