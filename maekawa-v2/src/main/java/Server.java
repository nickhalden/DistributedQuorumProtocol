

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server extends Thread{
	
	private NodeInfo selfNode;
	
	public Server(NodeInfo selfNode){
		this.selfNode = selfNode;		
	}
	
	public void run(){
		
		
		try {
			ServerSocket server = new ServerSocket(selfNode.getPortId());
			while (true) {
				Socket socket = server.accept();
				ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
				Message message = (Message)inputStream.readObject();
				System.out.println("Receive msg ------------------- "  + message.getType().toString() + " : " + message.getFromNode() + " : "+ message.getTimestamp());
				Main.setTimestamp(Main.getTimestamp()>message.getTimestamp()?Main.getTimestamp():message.getTimestamp());
				RequestHandler handler = new RequestHandler(message,selfNode);
				new Thread(handler).start();		
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
