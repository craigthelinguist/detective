package wblang.functions.indigenous;

import java.io.File;

import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.Err;
import wblang.primitives.Str;
import wblang.rules.Primitive;
import workbench.IO;

public class FuncLs extends Function {

	public FuncLs (Primitive[] args) throws TypeException {
		super(args);
	}
	
	public FuncLs (){
		super();
	}
	
	@Override
	public void verifyArguments (Primitive[] args)
	throws TypeException {	
		if (args.length != 1) throw new TypeException("Too many arguments: type signature is: " + signature());
		if (!(args[0] instanceof Str)) throw new TypeException("First and only argument must be either \"output\" or \"input\"");
		String s = args[0].toString();
		if (!(s.equals("output") || s.equals("input"))) throw new TypeException("First and only argument must be either \"output\" or \"input\"");
	}

	@Override
	public String usage () {
		return UsageTemplate.make(signature(), "\"Output\" or \"Input\"", "The files in the specified folder.");
	}

	@Override
	public String signature () {
		return SigTemplate.make(name(), new String[]{ "Str" }, new String[]{ "Str" });
	}
	
	@Override
	public Class returnType () {
		return Str.class;
	}

	@Override
	public String name () {
		return "ls";
	}

	@Override
	public Primitive exec() {
		String fpath = args()[0].toString();
		if (fpath.equals("output")) fpath = IO.OUTPUT;
		else fpath = IO.INPUT;
		File file = new File(fpath);
		if (!file.isDirectory()) return new Err("Could not find the directory " + file.getAbsolutePath());
		StringBuilder sb = new StringBuilder();
		File[] files = file.listFiles();
		if (files.length == 0) return new Err("No files in " + (args()[0]));
		for (int i = 0; i < files.length; i++) {
			sb.append("* " + files[i].getName());
			if (i != files.length - 1) sb.append("\n");
		}
		return new Str(sb.toString());
	}

	@Override
	public String eval () {
		return exec().toString();
	}
	
}
