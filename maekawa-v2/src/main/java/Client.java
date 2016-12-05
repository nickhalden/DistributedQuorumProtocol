

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {
	

	private Message message;
	
	
	
	public Client(Message message)
	{
		this.message = message;	
	}
	

	
	public void run(){
	
		int toId = message.getToNode();
		NodeInfo toNode = Main.staticNodeInfoMap.get(toId);
		NodeInfo selfNode = Main.selfNode;
		Main.setTimestamp(Main.getTimestamp()+1);
		message.setTimestamp(Main.getTimestamp());
		
		System.out.println("Send msg ------------------- "  + message.getType().toString() + " : " + message.getToNode() + " : "+ message.getTimestamp());
		try {		
			Socket clientSocket = new Socket(toNode.getHostname(), toNode.getPortId());
			ObjectOutputStream outStream = new ObjectOutputStream(clientSocket.getOutputStream());
			outStream.writeObject(message);
			outStream.flush();
			outStream.close();
			clientSocket.close();
			System.out.println("End Broadcast Client machine -id :"+ selfNode.getId());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}

}
