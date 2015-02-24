package wblang.functions.indigenous;

import java.io.File;

import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.Err;
import wblang.primitives.Str;
import wblang.rules.Primitive;
import workbench.Interpreter;

public class FuncRm extends Function {

	public FuncRm (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncRm () {
		super();
	}
	
	@Override
	public Primitive exec() {
		String fname = args()[0].toString();
		String fpath = Interpreter.currentDir() + fname;
		File file = new File(fpath);
		if (!file.exists()) return new Err("Could not find " + fname);
		if (file.isDirectory()) return new Err("Can't delete directory.");
		file.delete();
		return new Str("Deleted " + fname);
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 1 && !(args[0] instanceof Str)) throw new TypeException("Only argument should be a filename");
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "File name to delete.", "Success message or error message.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Str" }, new String[]{ "Str|Err" });
	}

	@Override
	public String name() {
		return "rm";
	}

	@Override
	public Class returnType() {
		return Str.class;
	}

}
