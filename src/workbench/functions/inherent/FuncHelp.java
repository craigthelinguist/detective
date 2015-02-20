package functions.inherent;

import primitives.Str;
import rules.Primitive;
import errors.TypeException;
import functions.Function;
import functions.FunctionFactory;

public class FuncHelp extends Function {

	public FuncHelp (Primitive[] args) throws TypeException {
		super(args);
	}
	
	public FuncHelp () {
		super();
	}
	
	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 0){
			System.out.println(args.length);
			throw new TypeException("help() takes no arguments.");
		}
	}

	@Override
	public String usage() {
		return "Don't be lame.";
	}

	@Override
	public String signature() {
		return "help :: () -> (Str)";
	}

	@Override
	public String name() {
		return "help";
	}

	@Override
	public String eval() {		
		return exec().toString();
	}

	@Override
	public Primitive exec () {
		String sb = "List of commands: \n";
		for (String str : FunctionFactory.funcs.keySet()) {
			Function f = FunctionFactory.make(str);
			sb += f.signature() + "\n";
		}
		return new Str(sb.toString());		
	}
	
	@Override
	public Class returnType() {
		return Str.class;
	}

}
