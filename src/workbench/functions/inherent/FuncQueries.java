package functions.inherent;

import java.util.ArrayList;
import java.util.List;

import dns.Host;
import primitives.HostPrim;
import primitives.Seq;
import primitives.Str;
import rules.Primitive;
import errors.TypeException;
import functions.Function;
import functions.SigTemplate;
import functions.UsageTemplate;

public class FuncQueries extends Function {

	public FuncQueries (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncQueries () {
		super();
	}
	
	@Override
	public Primitive exec() {
		HostPrim hp = (HostPrim) args()[0];
		Host host = hp.getHost();
		List<String> queries = host.getQueries();
		List<Str> strs = new ArrayList<>();
		for (String s : queries) strs.add(new Str(s));
		return new Seq<Str>(strs);
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 1 || !(args[0] instanceof HostPrim)) throw new TypeException("Only argument should be a host.");
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "A host.", "The queries sent by that host.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Host" }, new String[]{ "Seq<Str>" });
	}

	@Override
	public String name() {
		return "queries";
	}

	@Override
	public Class returnType() {
		return Seq.class;
	}

}
