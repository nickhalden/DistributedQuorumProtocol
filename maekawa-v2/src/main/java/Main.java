import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

public class Main {
	
	public static boolean lockAvailable;
	public static boolean inCriticalSection;
	public static NodeInfo lockHolderNode;
	public static int timestamp;
	public static Map<Integer,NodeInfo> staticNodeInfoMap;
	public static NodeInfo selfNode;
	public static ArrayList<Integer> selfQuorum;
	public static Map<Integer,ArrayList<Integer>> staticQuorumInfoMap;
	public static PriorityBlockingQueue<NodeInfo> requestQueue;
	public static Set<NodeInfo> grantSet;
	
	
	public static synchronized boolean isInCriticalSection() {
		return inCriticalSection;
	}

	public static synchronized void setInCriticalSection(boolean inCriticalSection) {
		Main.inCriticalSection = inCriticalSection;
	}

	public static synchronized void setTimestamp(int newTimestamp)
	{
	    timestamp = newTimestamp+1;
	}
	
	public static synchronized int getTimestamp()
	{
	    return timestamp;
	}
	
	public static synchronized void setLockAvailable(boolean newFlag)
	{
		lockAvailable = newFlag;
	}
	
	public static synchronized boolean getLockAvailable()
	{
		return lockAvailable;
	}
	
	
	public static void main(String ars[]) throws InterruptedException
	{
		
		Main.setLockAvailable(true);
		grantSet = new HashSet<NodeInfo>();
		
		int localMachineId = 0; // cmd args
		
		ConfigFileReader reader = new ConfigFileReader("C:\\Users\\eparchh\\Desktop\\configMk1.txt");
		List<NodeInfo> staticNodeList = reader.getNodeInfo();
		staticNodeInfoMap = new HashMap<Integer,NodeInfo>();
		for(NodeInfo node : staticNodeList)
		{
			staticNodeInfoMap.put(node.getId(), node);
		}
		selfNode = staticNodeInfoMap.get(localMachineId);
		List<QuorumInfo> quorumList = reader.getQuorumInfo();
		staticQuorumInfoMap = new HashMap<Integer, ArrayList<Integer>>();
		for(QuorumInfo qInfo : quorumList)
		{
			staticQuorumInfoMap.put(qInfo.getHostNodeId(), qInfo.getQuorumList());
		}
		
		
		selfQuorum = staticQuorumInfoMap.get(localMachineId);
		requestQueue = new PriorityBlockingQueue<NodeInfo>(selfQuorum.size(), new TimeStampComparator());
		System.out.println("selfNode : " + selfNode);
		System.out.println("staticNodeInfoMap : " + staticNodeInfoMap);
		/*int i=0;
		for(NodeInfo node : staticNodeInfoMap.values())
		{
			node.setTimestamp(i);
			requestQueue.add(node);
			i++;
		}*/
		
		
		System.out.println("quorum map : " +staticQuorumInfoMap);
		
		Server server = new Server(selfNode);
		new Thread(server).start();
		Thread.sleep(20000);
		for(Integer id : selfQuorum)
		{
			Message requestMessage = new Message(selfNode.getId(),id,Message.typeOfMessage.Request);
			// requestMessage.setTimestamp(Main.getTimestamp());
			//send logic
			Client client = new Client(requestMessage);
			new Thread(client).start();
		}
		
	}

}
