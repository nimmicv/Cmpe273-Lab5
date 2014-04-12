package com.sjsu.edu.library.stompMessage;

public class StompConfiguration {
	
	static String queueName;
	static String topicName;
	static String apolloUser;
	static String password;
	static int port;
	static String hostName;
	static String libraryName;
	
	public static String getLibraryName() {
		return libraryName;
	}
	public void setLibraryName(String libraryName) {
		this.libraryName = libraryName;
	}
	public static String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public static String getQueueName() {
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	public static String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public static String getApolloUser() {
		return apolloUser;
	}
	public void setApolloUser(String apolloUser) {
		this.apolloUser = apolloUser;
	}
	public static String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public static int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	
}
