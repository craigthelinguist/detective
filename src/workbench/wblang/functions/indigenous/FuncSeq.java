package wblang.functions.indigenous;

import java.util.ArrayList;
import java.util.List;

import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.Seq;
import wblang.rules.Primitive;

public class FuncSeq extends Function {

	public FuncSeq (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncSeq () {
		super();
	}
	
	@Override
	public Primitive exec() {
		List<Primitive> prims = new ArrayList<>();
		for (Primitive prim : args()) {
			prims.add(prim);
		}
		return new Seq<>(prims);
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length == 0) throw new TypeException("seq needs at least one argument.");
		Class cl = args[0].getClass();
		for (Primitive p : args) {
			if (!p.getClass().equals(cl)) throw new TypeException("seq's arguments must be all the same type.");
		}
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(),
								"Any number of arguments of the same type.",
								"A sequence containing those arguments.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Any..." }, new String[]{ "Seq" });
	}

	@Override
	public String name() {
		return "seq";
	}

	@Override
	public Class returnType() {
		return Seq.class;
	}

}
