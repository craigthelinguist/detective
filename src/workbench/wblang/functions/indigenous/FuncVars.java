package wblang.functions.indigenous;

import java.util.List;

import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.Str;
import wblang.rules.Expression;
import wblang.rules.Primitive;
import workbench.Interpreter;

public class FuncVars extends Function {

	public FuncVars (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncVars () {
		super();
	}
	
	@Override
	public Primitive exec() {

		StringBuilder sb = new StringBuilder();
		List<String> vars = Interpreter.getBindings();
		for (String str : vars) {
			Expression expr = Interpreter.getBinding(str);
			String type = "Expression";
			if (expr instanceof Primitive) type = ((Primitive)expr).typeName();
			else if (expr instanceof Function) type = "Function Call";
			sb.append(str + "   :   " + type + "\n");
		}
		return new Str(sb.toString());
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 0) throw new TypeException("Vars takes no arguments.");
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "Takes no arguments", "Lists all variables and their types.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[0], new String[]{ "Str" });
	}

	@Override
	public String name() {
		return "vars";
	}

	@Override
	public Class returnType() {
		return Str.class;
	}

}
