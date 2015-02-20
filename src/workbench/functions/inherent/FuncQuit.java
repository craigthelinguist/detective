package functions.inherent;

import primitives.Kore;
import rules.Primitive;
import workbench.TestingREPL;
import errors.TypeException;
import functions.Function;
import functions.SigTemplate;
import functions.UsageTemplate;

public class FuncQuit extends Function {

	public FuncQuit (Primitive[] args) throws TypeException {
		super(args);
	}
	
	public FuncQuit () {
		super();
	}
	
	@Override
	public Primitive exec() {
		TestingREPL.shutdown();
		return Kore.kore; // won't actually do anything
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 0) {
			throw new TypeException("quit() takes no arguments.");
		}
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "", "Exits the shell.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[0], new String[0]);
	}

	@Override
	public String name() {
		return "quit";
	}

	@Override
	public Class returnType() {
		return Kore.class;
	}

}
