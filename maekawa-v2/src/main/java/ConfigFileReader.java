

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigFileReader {
	public ArrayList<QuorumInfo> getQuorumInfo() {
		return quorumInfo;
	}

	public void setQuorumInfo(ArrayList<QuorumInfo> quorumInfo) {
		this.quorumInfo = quorumInfo;
	}

	private int numberOfNodes;
	private int requestDelay;
	private int csExecutionTime;
	private int noOfRequestPerNode;
	private ArrayList<NodeInfo> nodeInfo;
	private ArrayList<QuorumInfo> quorumInfo;
	public ArrayList<NodeInfo> getNodeInfo() {
		return nodeInfo;
	}
	
	public ArrayList<MessageInfo> getMessageInfo() {
		return messageInfo;
	}

	private ArrayList<MessageInfo> messageInfo;
	public ConfigFileReader (String fileName) {
		try {
			nodeInfo = new ArrayList<NodeInfo>();
			boolean notReadFirstLine = true;
			messageInfo = new ArrayList<MessageInfo>();
			quorumInfo = new ArrayList<QuorumInfo>();
			Scanner in = new Scanner(new File (fileName));
			while (in.hasNextLine()) {
				String temp = in.nextLine();
				if (temp.matches( "^\\s*#.*" )||temp.matches( "^\\s*$" )) {
					continue;
				} else if (temp.length()==0) {
					continue;
				} else if (temp.matches(".*,.*")){
					int temp_id = Integer.MIN_VALUE;
					ArrayList<Integer> list = new ArrayList<Integer>();
					System.out.println("found the paths of messages to be sent");
					String[] process = temp.split("\\s+");
					for (int i = 0; i<process.length; i++) {
						if (process[i].length()==0) {
							continue;
						} else if (temp_id == Integer.MIN_VALUE) {
							temp_id = Integer.parseInt(process[i]);
						} else {
							String regexGetList = "\\(.*\\)";
							Pattern getList = Pattern.compile(regexGetList);
							Matcher gotList = getList.matcher(temp);
							String sendList = gotList.find() ? gotList.group() : "";
							if (sendList.length()==0) {
								System.out.println("Did not find the send list");
							}
							//lets get rid of parenthesis
							sendList=sendList.replace("(","");
							sendList=sendList.replace(")","");
							// and now lets split
							String[] temp_split = sendList.split(",");
							for (String s: temp_split) {
								list.add(Integer.parseInt(s.trim()));
							}
							break;
						}
					}
					if (temp_id == Integer.MIN_VALUE || list.size()==0) {
						System.out.println("unable to process the send list");
					} else {
						int[] ret = new int[list.size()];
						  for(int i = 0;i < ret.length;i++)
						    ret[i] = list.get(i);
						messageInfo.add(new MessageInfo(temp_id,ret));
						System.out.println("Added to messageInfo List with sender id "+temp_id);
					}
				} else if (temp.matches(".*dc.*") || temp.matches(".*local.*")) {
					String[] process = temp.split("\\s+");
					if (process.length == 1) {
						//found number of nodes 
						setNumberOfNodes(Integer.parseInt(process[0]));
					} else {
						int temp_id = Integer.MIN_VALUE;
						int temp_portid = Integer.MIN_VALUE;
						String temp_hostname="";
						for (int i = 0; i<process.length; i++) {
							if (process[i].length()==0) {
								continue;
							} else if (process[i].matches("^[a-zA-Z].*")) {
								temp_hostname = process[i];
							} else if (temp_id == Integer.MIN_VALUE) {
								temp_id = Integer.parseInt(process[i]);
							} else {
								temp_portid = Integer.parseInt(process[i]);
							}
						}
						if (temp_id == Integer.MIN_VALUE || temp_portid==Integer.MIN_VALUE || temp_hostname.isEmpty()) {
							System.out.println("Error reading the config file");
							return;
						} else {
							nodeInfo.add(new NodeInfo(temp_id, temp_hostname, temp_portid));
							System.out.println("Addded to the NodeInfo Array");
						}
						}
				} else {
					String[] process = temp.split("\\s+");
					if (process.length == 1) {
						//found number of nodes 
						setNumberOfNodes(Integer.parseInt(process[0]));
					} else if (notReadFirstLine) {
						this.numberOfNodes = Integer.parseInt(process[0]);
						this.setRequestDelay(Integer.parseInt(process[1]));
						this.setCsExecutionTime(Integer.parseInt(process[2]));
						this.setNoOfRequestPerNode(Integer.parseInt(process[3]));
						notReadFirstLine = false;
					}	else {
						QuorumInfo temp_qorum = new QuorumInfo(Integer.parseInt(process[0]));
						for (int i = 1; i<process.length; i++) {
							temp_qorum.addQuorumList(Integer.parseInt(process[i]));
						}
						System.out.println("Added quoum for nodeid"+process[0]);
						this.quorumInfo.add(temp_qorum);
						
						}
				}
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ConfigFileReader x = new ConfigFileReader("config.txt");
	}

	public int getNumberOfNodes() {
		return numberOfNodes;
	}

	public void setNumberOfNodes(int numberOfNodes) {
		this.numberOfNodes = numberOfNodes;
	}

	public int getRequestDelay() {
		return requestDelay;
	}

	public void setRequestDelay(int requestDelay) {
		this.requestDelay = requestDelay;
	}

	public int getCsExecutionTime() {
		return csExecutionTime;
	}

	public void setCsExecutionTime(int csExecutionTime) {
		this.csExecutionTime = csExecutionTime;
	}

	public int getNoOfRequestPerNode() {
		return noOfRequestPerNode;
	}

	public void setNoOfRequestPerNode(int noOfRequestPerNode) {
		this.noOfRequestPerNode = noOfRequestPerNode;
	}
}
