package primitives;

import dns.Host;
import rules.Primitive;

public class HostPrim extends Primitive {

	private Host host;
	
	public HostPrim (Host host) {
		this.host = host;
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
