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
import workbench.Interpreter;

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
		if (args.length != 0) throw new TypeException("ls takes no arguments.");
	}

	@Override
	public String usage () {
		return UsageTemplate.make(signature(), "None.", "The files in the working director.y");
	}

	@Override
	public String signature () {
		return SigTemplate.make(name(), new String[0], new String[]{ "Str" });
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
		String dir = Interpreter.currentDir();
		File file = new File(dir);
		File[] files = file.listFiles();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < files.length; i++) {
			sb.append(files[i].getName());
			if (i != files.length - 1) sb.append("\n");
		}
		return new Str(sb.toString());
	}

	@Override
	public String eval () {
		return exec().toString();
	}
	
}
