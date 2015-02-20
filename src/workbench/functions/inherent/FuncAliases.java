package functions.inherent;

import java.util.Map;

import primitives.Str;
import rules.Primitive;
import errors.TypeException;
import functions.Function;
import functions.FunctionFactory;
import functions.SigTemplate;
import functions.UsageTemplate;

public class FuncAliases extends Function {

	public FuncAliases (Primitive[] args) throws TypeException{
		super(args);
	}
	
	public FuncAliases () {
		super();
	}
	
	@Override
	public Primitive exec() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : FunctionFactory.aliases.entrySet()) {
			sb.append(entry.getKey() + " ----> " + entry.getValue() + "\n");
		}
		return new Str(sb.toString());
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 0) throw new TypeException(name() + " takes no arguments.");
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "", "Return all aliases in use.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[0], new String[]{ "Str" });
	}

	@Override
	public String name() {
		return "aliases";
	}

	@Override
	public Class returnType() {
		return Str.class;
	}

}
