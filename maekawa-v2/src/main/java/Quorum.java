

import java.util.*;

public class Quorum {
	private int hostId;
	private ArrayList<Integer> QuorumList;
	public int getHostId() {
		return hostId;
	}
	public void setHostId(int hostId) {
		this.hostId = hostId;
	}
	public ArrayList<Integer> getQuorumList() {
		return QuorumList;
	}
	public void setQuorumList(ArrayList<Integer> quorumList) {
		QuorumList = quorumList;
	}
	
	public Quorum(int hostId, ArrayList<Integer> QuorumList) {
		this.setHostId(hostId);
		this.setQuorumList(QuorumList);
	}
}
