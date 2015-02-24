package wblang.functions.indigenous;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.Err;
import wblang.primitives.Str;
import wblang.rules.Primitive;
import workbench.Interpreter;

public class FuncCat extends Function {

	public FuncCat (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncCat () {
		super();
	}
	
	@Override
	public Primitive exec() {
		StringBuilder sb = new StringBuilder();
		Primitive[] fnames = args();
		for (Primitive fname : fnames) {
			String fpath = Interpreter.currentDir() + fname;
			File file = new File(fpath);
			if (!file.isFile()) return new Err("Could not find file " + fpath);
			
			Scanner sc = null;
			try{
				sc = new Scanner(file);
				StringBuilder sb2 = new StringBuilder();
				while (sc.hasNextLine()) sb2.append(sc.nextLine() + "\n");
				sb.append(sb2);
			}
			catch(IOException ioe){
				return new Err("Error reading file " + fpath);
			}
		}
		return new Str(sb.toString());
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length == 0) throw new TypeException("Cat needs at least one argument");
		for (Primitive p : args) {
			if (!(p instanceof Str)) throw new TypeException("Arguments to cat must be of type Str");
		}
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "File names in the current directory", "Contents of those files");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Str..." }, new String[]{ "Str" });
	}

	@Override
	public String name() {
		return "cat";
	}

	@Override
	public Class returnType() {
		return Str.class;
	}

}
