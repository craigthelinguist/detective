package wblang.functions.indigenous;

import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.Hash;
import wblang.rules.Primitive;

public class FuncHash extends Function {

	public FuncHash (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncHash () {
		super();
	}
	
	@Override
	public Primitive exec() {
		return new Hash();
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 0) throw new TypeException("Function takes no arguments.");
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "", "An empty Hash.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[0], new String[]{ "Hash" });
	}

	@Override
	public String name() {
		return "hash";
	}

	@Override
	public Class returnType() {
		return Hash.class;
	}
	
}
