package wblang.functions.indigenous;

import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.Hash;
import wblang.primitives.Str;
import wblang.rules.Primitive;

public class FuncHashPut extends Function {

	public FuncHashPut (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncHashPut () {
		super();
	}
	
	@Override
	public Primitive exec() {
		Hash hash = (Hash)args()[0];
		Primitive key = (Primitive)args()[1];
		Primitive value = (Primitive)args()[2];
		hash.add(key, value);
		return new Str(hash.toString());
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 3) throw new TypeException("hashput takes 3 arguments.");
		if (!(args[0] instanceof Hash)) throw new TypeException("hashput's 1st argument must be a Hash.");
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "A hash, and the key-value pair", "Adds the key-value pair to the hash.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Hash", "Any", "Any" }, new String[]{});
	}

	@Override
	public String name() {
		return "hashput";
	}

	@Override
	public Class returnType() {
		return Str.class;
	}

}
