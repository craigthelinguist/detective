package wblang.functions.indigenous;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import dns.Host;
import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.Err;
import wblang.primitives.HostPrim;
import wblang.primitives.Seq;
import wblang.primitives.Str;
import wblang.rules.Primitive;

public class FuncSaveHosts extends Function {

	public FuncSaveHosts (Primitive[] args) throws TypeException {
		super(args);
	}
	
	public FuncSaveHosts () {
		super();
	}
	
	@Override
	public Primitive exec() {
		Str s = (Str)args()[0];
		String fpath = "src/output/" + s;
		fpath = fpath.replace('/', File.separatorChar);
		File file = new File(fpath);
		
		try {
			
			PrintStream ps = null;
			try{
				ps = new PrintStream(file);
				Seq<HostPrim> hosts = (Seq<HostPrim>)args()[1];
				for (HostPrim hp : hosts) {
					Host h = hp.getHost();
					ps.println(h.toString());
					for (String domain : h.getQueries()) {
						ps.println("\t" + domain);
					}
					ps.println();
				}
				return new Str("Saved.");
			}
			finally {
				if (ps != null) ps.close();
			}
			
		}
		catch (IOException ioe) {
			return new Err("IO error while saving to " + fpath);
		}
		
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 2) throw new TypeException("Takes 2 arguments.");
		if (!(args[0] instanceof Str)) throw new TypeException("First argument should be filename to save to.");
		if (!(args[1] instanceof Seq)) throw new TypeException("Second argument should be a sequence of hosts.");
		if (args.length == 2) {
			if (!(args[2] instanceof Str)) throw new TypeException("Third argument should be string specifying folder to save.");
			Str s = (Str)args[2];
			if (!FuncSave.validDirs.contains(s.toString())) throw new TypeException("Third argument not a legal folder.");
		}
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "String and sequence of hosts to save.", "Err message if it failed to save, or a success message otherwise.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Str", "Seq<Host>" }, new String[]{ "Err|Kore" });
	}

	@Override
	public String name() {
		return "savehosts";
	}

	@Override
	public Class returnType() {
		return Err.class;
	}

}
