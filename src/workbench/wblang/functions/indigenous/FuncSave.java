package wblang.functions.indigenous;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.Err;
import wblang.primitives.Seq;
import wblang.primitives.Str;
import wblang.rules.Primitive;

public class FuncSave extends Function {

	public FuncSave (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncSave () {
		super();
	}
	
	@Override
	public Primitive exec() {
		Str fname = (Str)args()[0];
		String fpath = "src/output/" + fname;
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
		if (args.length != 2) throw new TypeException("Takes 2 arguments.");
		if (!(args[0] instanceof Str)) throw new TypeException("First argument should be filename to save to.");
		if (!(args[1] instanceof Seq)) throw new TypeException("Second argument should be a sequence of strings.");
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
