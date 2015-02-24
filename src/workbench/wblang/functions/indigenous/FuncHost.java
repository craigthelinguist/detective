package wblang.functions.indigenous;

import java.util.ArrayList;
import java.util.List;

import dns.Host;
import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.HostPrim;
import wblang.primitives.Seq;
import wblang.primitives.Str;
import wblang.rules.Primitive;

public class FuncHost extends Function {

	public FuncHost (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncHost () {
		super();
	}
	
	@Override
	public Primitive exec() {
		Seq<Str> seq = (Seq<Str>)args()[0];
		List<String> strings = new ArrayList<>();
		for (Str s : seq) {
			strings.add(s.toString());
		}
		String name = "" +System.nanoTime();
		name = name.substring(name.length()-4);
		Host h = new Host(name, strings);
		return HostPrim.make(h);
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 1 && !(args[0] instanceof Seq)) throw new TypeException("Must pass in one argument of Seq<Str>");
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "Sequence of domains queried by host.", "A host");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Seq<Str>" }, new String[]{ "Host" });
	}

	@Override
	public String name() {
		return "host";
	}

	@Override
	public Class returnType() {
		return HostPrim.class;
	}

}
