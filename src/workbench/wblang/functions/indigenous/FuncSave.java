package wblang.functions.indigenous;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.Err;
import wblang.primitives.Seq;
import wblang.primitives.Str;
import wblang.rules.Primitive;

public class FuncSave extends Function {

	public static List<String> validDirs = Arrays.asList(new String[]{ "in", "input", "out", "output" });
	
	public FuncSave (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncSave () {
		super();
	}
	
	@Override
	public Primitive exec() {
		Str fname = (Str)args()[0];
		String folder = "output";
		if (args().length == 3) {
			String s = args()[2].toString();
			if (s.equals("in") || s.equals("input")) folder = "input";
		}
		String fpath = "src/" + folder + "/" + fname;
		fpath = fpath.replace('/', File.separatorChar);
		File file = new File(fpath);
		try {
			PrintStream ps = null;
			try{
				ps = new PrintStream(file);
				Seq<Str> strings = (Seq<Str>)args()[1];
				for (Str s : strings) {
					ps.println(s.toString());
				}
				return new Str("Saved to " + file.getAbsolutePath());
			}
			finally {
				if (ps != null) ps.close();
			}				
		}
		catch (IOException ioe) {
			return new Err("Error while writing to " + fpath);
		}
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 2 && args.length != 3) throw new TypeException("Takes 2 arguments.");
		if (!(args[0] instanceof Str)) throw new TypeException("First argument should be filename to save to.");
		if (!(args[1] instanceof Seq)) throw new TypeException("Second argument should be a sequence of strings.");
		if (args.length == 2) {
			if (!(args[2] instanceof Str)) throw new TypeException("Third argument should be string specifying folder to save.");
			Str s = (Str)args[2];
			if (!validDirs.contains(s.toString())) throw new TypeException("Third argument not a legal folder.");
		}
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "Filename and the sequence of strings to save", "Saves to the file and returns success message.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Str", "Seq" }, new String[]{ "Kore|Err" });
	}

	@Override
	public String name() {
		return "save";
	}

	@Override
	public Class returnType() {
		return Err.class;
	}

}
