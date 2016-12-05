

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.PriorityBlockingQueue;

import javax.swing.text.StyledEditorKit.StyledTextAction;


public class RequestHandler implements Runnable{
	
	private Message message;
	private NodeInfo selfNode;


	public  RequestHandler(Message message, NodeInfo selfNode) {
		this.message = message;
		this.selfNode= selfNode;
	}

	public void run() {
		
		try {
			 
			Message.typeOfMessage messageType = message.getType();
			switch (messageType)
			{
				case Request:
					handleRequestMessage(message);
					break;
				case Release:
					handleReleaseMessage(message);
					break;
				case Grant:
					handleGrantMessage(message);
					break;
				case Inquire:
					handleInquireMessage(message);
					break;
				case Yeild:
					handleYieldMessage(message);
					break;
				case Failed:
					handleFailedMessage(message);
					break;
				default:
					System.out.println("unknown message type");
				
					
			}
				 
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	private void handleRequestMessage(Message message) throws Exception
	{
		PriorityBlockingQueue<NodeInfo> requestQueue= Main.requestQueue;
		NodeInfo topRequestor = requestQueue.peek();
		NodeInfo requestor = Main.staticNodeInfoMap.get(message.getFromNode());
		requestor.setTimestamp(message.getTimestamp());
		
		requestQueue.add(requestor);  // synchronized
		
		//NodeInfo topRequestor = requestQueue.peek();
		
		if(Main.getLockAvailable())
		{
				Main.setLockAvailable(false);
				Main.lockHolderNode = requestQueue.peek();	
				Message grantMessage = new Message(selfNode.getId(),Main.lockHolderNode.getId(),Message.typeOfMessage.Grant);
				//grantMessage.setTimestamp(Main.getTimestamp());
				//send message logic
				Client client = new Client(grantMessage);
				new Thread(client).start();
		}
		else
		{
			
			NodeInfo currentTop =  requestQueue.peek();
			
			if(currentTop == requestor)
			{
				Message inquireMessage = new Message(selfNode.getId(),currentTop.getId(),Message.typeOfMessage.Inquire);
				//inquireMessage.setTimestamp(Main.getTimestamp());
				//send message logic
				Client client = new Client(inquireMessage);
				new Thread(client).start();
				
			}
			
		}
		
	}
	
	private void handleReleaseMessage(Message message)  throws Exception
	{
		System.out.println(Main.requestQueue);
		System.out.println(Main.grantSet);
		Main.setLockAvailable(true);
		if(Main.getLockAvailable())
		{
			NodeInfo removedNode = Main.requestQueue.take();
			Main.setLockAvailable(false);
			NodeInfo newTopNode = Main.requestQueue.peek();
			if(newTopNode!=null)
			{
				synchronized(Main.lockHolderNode)
				{
					Main.lockHolderNode = newTopNode;
					Message grantMessage = new Message(selfNode.getId(),Main.lockHolderNode.getId(),Message.typeOfMessage.Grant);
					//grantMessage.setTimestamp(Main.getTimestamp());
					//send message logic
					Client client = new Client(grantMessage);
					new Thread(client).start();
				}
				
				
			}
			
			
		}
	}
	
	private void handleGrantMessage(Message message)
	{
		NodeInfo sender = Main.staticNodeInfoMap.get(message.getFromNode());
		sender.setTimestamp(Main.getTimestamp());
		
		Main.grantSet.add(sender);
		if(Main.grantSet.size()==Main.selfQuorum.size())
		{
			Main.setLockAvailable(false);
			Main.setInCriticalSection(true);
			csEnter(message);
			csExit(message);
		}
	}
	
	private void handleInquireMessage(Message message)
	{
		 if(Main.isInCriticalSection())
		 {
			 Message failedMessage = new Message(selfNode.getId(),message.getFromNode(),Message.typeOfMessage.Failed);
			 //failedMessage.setTimestamp(Main.getTimestamp());
			 // send logic
			 Client client = new Client(failedMessage);
				new Thread(client).start();
		 }
		 else
		 {
			 Main.setLockAvailable(false);
			 Message yieldMessage = new Message(selfNode.getId(),message.getFromNode(),Message.typeOfMessage.Yeild);
			 //yieldMessage.setTimestamp(Main.getTimestamp());
			 Client client = new Client(yieldMessage);
				new Thread(client).start();
			 NodeInfo requestor = Main.staticNodeInfoMap.get(message.getFromNode());
			 boolean removeRequestorFlag = Main.grantSet.remove(requestor);
			 boolean removeSelfNodeFlag = Main.grantSet.remove(selfNode);
			 if(removeRequestorFlag)
			 {
				 Message requestMessage = new Message(selfNode.getId(),requestor.getId(),Message.typeOfMessage.Request);
				// requestMessage.setTimestamp(Main.getTimestamp());
				//send logic
				Client client2 = new Client(requestMessage);
				new Thread(client).start();
			 }
			 if(removeSelfNodeFlag)
			 {
				 Message requestMessage = new Message(selfNode.getId(),selfNode.getId(),Message.typeOfMessage.Request);
				// requestMessage.setTimestamp(Main.getTimestamp());
				//send logic
				 Client client3 = new Client(requestMessage);
				 new Thread(client).start();
			 }
			 
		 }
	}
	
	private void handleYieldMessage(Message message)
	{
		Main.setLockAvailable(true);
		NodeInfo topRequestor = Main.requestQueue.peek();
		 Message grantMessage = new Message(selfNode.getId(),topRequestor.getId(),Message.typeOfMessage.Grant);
		// grantMessage.setTimestamp(Main.getTimestamp());
		//send logic
		 Client client = new Client(grantMessage);
		 new Thread(client).start();
		 Main.setLockAvailable(false);
		Main.lockHolderNode = topRequestor;
		 
	}
	
	private void handleFailedMessage(Message message)
	{
		System.out.println("Failed Msg from : "+ message.getFromNode() + " to : "+ message.getToNode());
	}
	
	private void csEnter(Message message)
	{
		// logic for writing into file  - to do
		System.out.println("Start CS - from : " + selfNode.getId() + " with time : "+ message.getTimestamp());
		try {
			FileWriter fw = new FileWriter("C:\\Users\\eparchh\\Desktop\\testOutput.txt", true);
			fw.write("\n");
			fw.write("Start CS - from : " + selfNode.getId() + " with time : "+ message.getTimestamp());
			Thread.sleep(5000);
			fw.write("\n");
			fw.write("End CS - from : " + selfNode.getId());
			fw.flush();
			fw.close();
			System.out.println("Exit Critical Section ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void csExit(Message message)
	{
		Main.setLockAvailable(true);
		Main.grantSet.clear();
		for(Integer id : Main.selfQuorum)
		{
			Message releaseMessage = new Message(selfNode.getId(),id,Message.typeOfMessage.Release);
			//releaseMessage.setTimestamp(Main.getTimestamp());
			// send logic
			Client client = new Client(releaseMessage);
			 new Thread(client).start();
		}
		Main.setInCriticalSection(false);
	}

}

