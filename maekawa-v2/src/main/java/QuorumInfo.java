

import java.util.*;

public class QuorumInfo {
	private int hostNodeId; 
	private ArrayList<Integer> QuorumList;
	
	public QuorumInfo (int hostNodeId) {
		this.setHostNodeId(hostNodeId);
		this.QuorumList = new ArrayList<Integer>();
	}
	
	public Quorum convertToQuorum() {
		return (new Quorum(this.hostNodeId,this.QuorumList));
	}
	
	public void addQuorumList (int nodeId) {
		this.QuorumList.add(nodeId);
	}

	public ArrayList<Integer> getQuorumList() {
		return QuorumList;
	}

	public void setQuorumList(ArrayList<Integer> quorumList) {
		QuorumList = quorumList;
	}

	public int getHostNodeId() {
		return hostNodeId;
	}

	public void setHostNodeId(int hostNodeId) {
		this.hostNodeId = hostNodeId;
	}
}
