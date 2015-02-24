package wblang.functions.indigenous;

import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.Str;
import wblang.rules.Primitive;
import workbench.Interpreter;

public class FuncDir extends Function {

	public FuncDir (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncDir () {
		super();
	}
	
	@Override
	public Primitive exec() {
		return new Str(Interpreter.currentDir());
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 0) throw new TypeException("dir takes no arguments.");
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "No arguments.", "The working directory (either 'input' or 'output'");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[0], new String[]{ "Str" });
	}

	@Override
	public String name() {
		return "dir";
	}

	@Override
	public Class returnType() {
		return Str.class;
	}

}
