package wblang.functions.indigenous;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.FunctionFactory;
import wblang.primitives.Str;
import wblang.rules.Primitive;

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
		StringBuilder sb = new StringBuilder("List of commands: \n");
		List<String> strs = new ArrayList<>();
		for (String str : FunctionFactory.funcs.keySet()) strs.add(str);
		Collections.sort(strs);
		for (String str : strs) {
			Function f = FunctionFactory.make(str);
			sb.append(f.signature() + "\n");
		}
		return new Str(sb.toString());
	}
	
	@Override
	public Class returnType() {
		return Str.class;
	}

}
