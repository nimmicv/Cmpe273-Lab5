package edu.sjsu.cmpe.procurement.stomp;

public class StompConfiguration {
	
	private static String queueName;
	private static String topicName;
	private static String apolloUser;
	private static String password;
	private static int port;
	private static String hostName;
	private static String libraryName;
	
	public static String getLibraryName() {
		return libraryName;
	}
	public static void setLibraryName(String libraryName) {
		StompConfiguration.libraryName = libraryName;
	}
	public static String getHostName() {
		return hostName;
	}
	public static void setHostName(String hostName) {
		StompConfiguration.hostName = hostName;
	}
	public static String getQueueName() {
		return queueName;
	}
	public static void setQueueName(String queueName) {
		StompConfiguration.queueName = queueName;
	}
	public static String getTopicName() {
		return topicName;
	}
	public static void setTopicName(String topicName) {
		StompConfiguration.topicName = topicName;
	}
	public static String getApolloUser() {
		return apolloUser;
	}
	public static void setApolloUser(String apolloUser) {
		StompConfiguration.apolloUser = apolloUser;
	}
	public static String getPassword() {
		return password;
	}
	public static void setPassword(String password) {
		StompConfiguration.password = password;
	}
	public static int getPort() {
		return port;
	}
	public static void setPort(int port) {
		StompConfiguration.port = port;
	}
	
	

}
