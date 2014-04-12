package edu.sjsu.cmpe.procurement.stomp;

import javax.jms.Connection;
import javax.jms.JMSException;

import org.fusesource.stomp.jms.StompJmsConnectionFactory;

public class ConnectionObject {
	
	private static StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
	public static Connection connection = null; 
	private static ConnectionObject object = null;
	
	
	private ConnectionObject()
	{
		
	}
	
	public synchronized static ConnectionObject getInstance()
    {	
		if(object == null)
		{
				object = new ConnectionObject();
				try {
					object.factory.setBrokerURI("tcp://" + StompConfiguration.getHostName() + ":" + StompConfiguration.getPort());
					object.connection = factory.createConnection(StompConfiguration.getApolloUser(), StompConfiguration.getPassword());
				}catch (JMSException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return object;	
    }

}
