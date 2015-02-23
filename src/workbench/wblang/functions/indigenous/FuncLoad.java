package wblang.functions.indigenous;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.Kore;
import wblang.primitives.Seq;
import wblang.primitives.Str;
import wblang.rules.Primitive;

public class FuncLoad extends Function {
	
	public FuncLoad (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncLoad () {
		super();
	}
	
	@Override
	public Primitive exec() {
		String fpath = "src/input/" + args()[0].toString();
		fpath = fpath.replace('/', File.separatorChar);
		File file = new File(fpath);
		if (!file.exists()) return Kore.kore;
		try{
			FileReader fr = null;
			BufferedReader br = null;	
			try{
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				List<Str> names = new ArrayList<>();
				String line = "";
				while ((line = br.readLine()) != null) {
					String name = line.trim();
					names.add(new Str(name));
				}
				return new Seq<Str>(names);
			}
			finally {
				fr.close();
				br.close();
			}
		}
		catch (IOException ioe) {
			return Kore.kore;
		}
	}


	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 1 || !(args[0] instanceof Str)){
			throw new TypeException(name() + " takes exactly one argument: the file to load.");
		}
		String fpath = "src/input/" + args[0].toString();
		fpath = fpath.replace('/', File.separatorChar);
		File file = new File(fpath);
		if (!file.isFile()) {
			throw new TypeException("Could not find the file " + file.getAbsolutePath());
		}
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(),
				"Name of file to load.",
				"A sequence of the lines in the file.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Str" }, new String[]{ "Seq" });
	}

	@Override
	public String name() {
		return "loadnames";
	}

	@Override
	public Class returnType() {
		return Seq.class;
	}

}
