/* AOS Project 1
 * Submitted by Mahesh Kothagere Siddalingappa 
 * netId : mxk145330
 * Course number: 6378
 * Section: 002
 * Fall 2016
 */

import java.io.*;
import java.util.*;

public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int toNode;
	private int fromNode;
	private int timestamp;
	private Queue<Integer> sendList;
	public enum typeOfMessage{Message,Complete,Reply,Request,Failed,Grant,Inquire,Yeild,Release};
	private typeOfMessage type; 
	private String Content;
	private int value;
	
	public Message(int fromNode, int toNode, String type) {
		this.setToNode(toNode);
		this.setFromNode(fromNode);
		this.setValue(0);
		this.setContent("Dummy For now");
		this.setType(typeOfMessage.Complete);
		this.setSendList(null);
	}
	public Message(int fromNode, int toNode, typeOfMessage msgType) {
		this.setToNode(toNode);
		this.setFromNode(fromNode);
		this.setValue(0);
		this.setContent("Dummy For now");
		this.setType(msgType);
		this.setSendList(null);
	}
	public Message(Queue<Integer> sendList) {
		this.setToNode(Integer.MIN_VALUE);
		this.setFromNode(Integer.MIN_VALUE);
		this.setValue(0);
		this.setContent("Dummy For now");
		this.setType(typeOfMessage.Message);
		this.setSendList(sendList);
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getToNode() {
		return toNode;
	}
	public void setToNode(int toNode) {
		this.toNode = toNode;
	}
	/**
	 * @return the fromNode
	 */
	public int getFromNode() {
		return fromNode;
	}

	/**
	 * @param fromNode the fromNode to set
	 */
	public void setFromNode(int fromNode) {
		this.fromNode = fromNode;
	}

	/**
	 * @return the sendList
	 */
	public Queue<Integer> getSendList() {
		return sendList;
	}

	/**
	 * @param sendList the sendList to set
	 */
	public void setSendList(Queue<Integer> sendList) {
		this.sendList = sendList;
	}

	public typeOfMessage getType() {
		return type;
	}

	public void setType(typeOfMessage messageType) {
		this.type = messageType;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
	public int getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
}
