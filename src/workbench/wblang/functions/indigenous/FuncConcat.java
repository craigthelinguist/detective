package wblang.functions.indigenous;

import java.util.ArrayList;
import java.util.List;

import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.Seq;
import wblang.rules.Primitive;

public class FuncConcat extends Function {

	public FuncConcat (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncConcat () {
		super();
	}

	@Override
	public Primitive exec() {
		List<Primitive> bigList = new ArrayList<>();
		for (Primitive arg : args()) {
			Seq<Primitive> seq = (Seq<Primitive>)arg;
			for (Primitive elem : seq) {
				bigList.add(elem);
			}
		}
		return new Seq<Primitive>(bigList);
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		for (Primitive pr : args) {
			if (!(pr instanceof Seq)) throw new TypeException("All args must be sequences.");
		}
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "A bunch of sequences.", "The elements in all those sequences, in one sequence.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Seq..." }, new String[]{ "Seq" });
	}

	@Override
	public String name() {
		return "concat";
	}

	@Override
	public Class returnType() {
		return Seq.class;
	}
	
}
