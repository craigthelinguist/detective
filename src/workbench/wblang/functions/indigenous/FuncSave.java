package wblang.functions.indigenous;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import dns.Host;
import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.ClusterPrim;
import wblang.primitives.Err;
import wblang.primitives.HostPrim;
import wblang.primitives.Seq;
import wblang.primitives.Str;
import wblang.rules.Primitive;
import workbench.Interpreter;

public class FuncSave extends Function {

	public static List<String> validDirs = Arrays.asList(new String[]{ "in", "input", "out", "output" });
	
	public FuncSave (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncSave () {
		super();
	}
	
	private Primitive saveClusters() {
		
		
		Seq<ClusterPrim> seq = (Seq<ClusterPrim>)args()[1];
		int i = 0;
		
		for (ClusterPrim cp : seq) {
			
			PrintStream ps = null;
			try{
				ps = new PrintStream(new File("cluster" + i));
				i++;
				for (Str query : cp.asList()) {
					ps.println(query.toString());
				}
			}
			catch (IOException ioe) {
				if (ps != null) ps.close();
			}
			
		}
		
		return new Str("Saved by the bell");
		
	}
	
	@Override
	public Primitive exec() {
		Str fname = (Str)args()[0];
		String fpath = Interpreter.currentDir() + fname;
		File file = new File(fpath);
		Seq seq = (Seq)args()[1];
		if (seq.get(0) instanceof ClusterPrim) return saveClusters();
		
		try {
			PrintStream ps = null;
			try{
				ps = new PrintStream(file);
				seq = (Seq) args()[1];

				// if you're saving Seq<Str>
				if (seq.get(0) instanceof Str) {
					Seq<Str> strings = (Seq<Str>)args()[1];
					for (Str s : strings) {
						ps.println(s.toString());
					}
				}
				
				// if you're saving Seq<Host>
				else if (seq.get(0) instanceof HostPrim) {
					Seq<HostPrim> hostSeq = (Seq<HostPrim>)seq;
					for (HostPrim hp : hostSeq) {
						
						Host host = hp.getHost();
						ps.println(host.toString());
						
						for (String query : host.getQueries()) {
							ps.println("\t" + query);
						}
						
						ps.println();
						
					}
				}
				
				// if you're saving Seq<Cluster>
				else if (seq.get(0) instanceof ClusterPrim) {
					Seq<ClusterPrim> clusterSeq = (Seq<ClusterPrim>)seq;
					int i = 0;
					for (ClusterPrim cp : clusterSeq) {
						ps.println("Cluster " + i);
						i++;
						for (Str query : cp) {
							ps.println(query.toString());
						}
						ps.println();
					}
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
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "Filename and the sequence of strings to save", "Saves to the file and returns success message.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Str", "Seq<Str>|Seq<Host>|Seq<Cluster>" }, new String[]{ "Kore|Err" });
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
