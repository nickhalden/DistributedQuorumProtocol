/* AOS Project 1
 * Submitted by Mahesh Kothagere Siddalingappa 
 * netId : mxk145330
 * Course number: 6378
 * Section: 002
 * Fall 2016
 */


public class NodeInfo {
	private int id;
	private String hostname; 
	private int portId;
	private boolean isAvailable;
	private long timestamp;
	
	public NodeInfo(int id, String hostname, int portId){
		this.setId(id);
		this.setHostname(hostname);
		this.setPortId(portId);
		this.setAvailable(true);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPortId() {
		return portId;
	}

	public void setPortId(int portId) {
		this.portId = portId;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	
	

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "NodeInfo [id=" + id + ", hostname=" + hostname + ", portId=" + portId + ", isAvailable=" + isAvailable
				+ ", timestamp=" + timestamp + "]";
	}


	
	
}
