package functions.inherent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dns.Host;
import primitives.Err;
import primitives.HostPrim;
import primitives.Seq;
import primitives.Str;
import rules.Primitive;
import errors.TypeException;
import functions.Function;
import functions.SigTemplate;
import functions.UsageTemplate;

public class FuncLoadHosts extends Function {

	public FuncLoadHosts (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncLoadHosts () {
		super();
	}
	
	@Override
	public Primitive exec() {
		String fpath = "src/input/" + args()[0].toString();
		fpath = fpath.replace('/', File.separatorChar);

		try{
			FileReader fr = null;
			BufferedReader br = null;
			
			try{
				
				fr = new FileReader(fpath);
				br = new BufferedReader(fr);
				List<HostPrim> hosts = new ArrayList<>();
				String line = "";
				while ((line = br.readLine()) != null)  {
					
					if (line.length() == 0) continue; //looking for ip address
					
					String ip = line;
					List<String> domains = new ArrayList<>();
					
					while ((line = br.readLine()).length() > 0) {
						domains.add(line.trim());
					}
					
					Host host = new Host(ip, domains);
					HostPrim hostPrim = new HostPrim(host);
					hosts.add(hostPrim);
				}
	
				return new Seq<>(hosts);	
			}
			finally {
				if (fr != null) fr.close();
				if (br != null) br.close();
			}
			
			
		}
		catch (IOException ioe) {
			return new Err("Error while reading " + fpath);
		}
		
		
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 1){
			throw new TypeException("Only argument should be name of the file.");
		}
		if (!(args[0] instanceof Str)) {
			throw new TypeException("Only argument should be name of the file.");
		}
		String fpath = "src/input/" + args[0].toString();
		fpath = fpath.replace('/', File.separatorChar);
		File file = new File(fpath);
		if (!file.exists()) throw new TypeException("Could not find file " + fpath);
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(),
				"File name containing the hosts to load.",
				"A Sequence of the hosts in that file.");				
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Str" }, new String[]{ "Seq" });
	}

	@Override
	public String name() {
		return "loadhosts";
	}

	@Override
	public Class returnType() {
		return Seq.class;
	}

}
