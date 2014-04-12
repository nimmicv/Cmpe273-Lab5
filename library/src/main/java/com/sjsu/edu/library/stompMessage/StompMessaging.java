package com.sjsu.edu.library.stompMessage;
import java.net.URL;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import org.fusesource.stomp.jms.StompJmsDestination;

import com.fasterxml.jackson.databind.cfg.ConfigFeature;

import edu.sjsu.cmpe.library.config.LibraryServiceConfiguration;
import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.Book.Status;
import edu.sjsu.cmpe.library.repository.BookRepository;
import edu.sjsu.cmpe.library.repository.BookRepositoryInterface;

public class StompMessaging {
	static StompConfiguration stompconfiguration;
	BookRepository bookRepository;
	
	public StompMessaging()
	{
		
	}
	
	public StompMessaging(StompConfiguration config,BookRepositoryInterface bookRepository2)
	{
		stompconfiguration = config;
		bookRepository = (BookRepository) bookRepository2;
	}
	
	public static Connection createConnection() throws JMSException
	{
		Connection connection = ConnectionObject.getInstance().connection;
        return connection;
	}
	
	public void sendMsgQueue(Connection connection, long ISBN) throws Exception {
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = new StompJmsDestination(stompconfiguration.getQueueName());
        MessageProducer producer = session.createProducer(destination);
        TextMessage msg = session.createTextMessage(stompconfiguration.getLibraryName()+":" + ISBN);
        msg.setLongProperty("id", System.currentTimeMillis());
        producer.setTimeToLive(123);
        producer.send(msg); 
        //producer.close();
        session.close();
        //connection.stop();
	}
	
	public void subscriber(Connection connection) throws Exception {
		connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination dest = new StompJmsDestination(stompconfiguration.getTopicName());
        MessageConsumer consumer = session.createConsumer(dest);
        
        while (true) {
            Message msg = consumer.receive();
            String[] updateMessage=((TextMessage)msg).getText().toString().split(":", 4); 
            Long isbn = Long.valueOf(updateMessage[0]);
            System.out.println("Book Arrived : "+ isbn +" ,Name: "+updateMessage[1]);
            Status status = Status.available;
            Book book = bookRepository.getBookByISBN(isbn);
            // book received from Publisher and is in lost status
            if (book != null && book.getStatus()==Status.lost) {
            	book.setStatus(status);
            	System.out.println("Book " + book.getIsbn() +" is made available");
            }
            else if (book == null){
            	String title = updateMessage[1];
            	String category = updateMessage[2];
            	URL coverImage = new URL(updateMessage[3]);
            	book = new Book();
            	book.setTitle(title);
            	book.setCategory(category);
            	book.setCoverimage(coverImage);
            	bookRepository.saveBook(book,isbn);
            	System.out.println("New book  " + book.getIsbn() + " added to library");
            }
            else {
            	System.out.println("Book " + book.getIsbn() + " is duplicate... discarding the entry");
            }
            
        }
	}
}
