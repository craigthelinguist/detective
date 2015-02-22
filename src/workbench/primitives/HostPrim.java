package primitives;

import java.util.ArrayList;
import java.util.List;

import dns.Host;
import rules.Primitive;

public class HostPrim extends Seq {

	private Host host;
	
	private HostPrim (List<Str> strs, Host host) {
		super(strs);
		this.host = host;
	}
	
	public static HostPrim make (Host host) {
		List<String> strings = host.getQueries();
		List<Str> strs = new ArrayList<>();
		for (String s : strings) strs.add(new Str(s));
		return new HostPrim(strs, host);
	}
	
	@Override
	public boolean equals (Object other) {
		if (!(other instanceof HostPrim)) return false;
		HostPrim hp = (HostPrim)other;
		return hp.host.equals(host);
	}
	
	@Override
	public String toString() {
		return host.toString() + ", " + host.getQueries().size() + " queries.";
	}

	public Host getHost() {
		return this.host;
	}
	
	@Override
	public String typeName() {
		return "Host";
	}
	
}
