package wblang.functions.indigenous;

import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.Kore;
import wblang.rules.Primitive;
import workbench.TestingREPL;

public class FuncCd extends Function {

	public FuncCd (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncCd () {
		super();
	}
	
	@Override
	public Primitive exec() {
		String s = args()[0].toString();
		if (s.equals("out") || s.equals("output")) TestingREPL.changeDir("output");
		else if (s.equals("in") || s.equals("input")) TestingREPL.changeDir("input");
		return Kore.kore;
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 1) throw new TypeException("cd takes one argument.");
		String s = args[0].toString();
		if (!s.equals("input") && !s.equals("in") && !s.equals("out") && !s.equals("output")) {
			throw new TypeException("must cd to \"input\" or \"output\"");
		}
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "'cd(\"input\" or 'cd(\"output\")", "Changes working directory.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Str" }, new String[0]);
	}

	@Override
	public String name() {
		return "cd";
	}

	@Override
	public Class returnType() {
		return Kore.class;
	}

	
	
	
}
