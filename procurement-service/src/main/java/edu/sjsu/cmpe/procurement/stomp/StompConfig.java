package edu.sjsu.cmpe.procurement.stomp;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import org.fusesource.stomp.jms.StompJmsDestination;

import com.sun.jersey.api.client.Client;

import edu.sjsu.cmpe.procurement.config.ProcurementServiceConfiguration;
import edu.sjsu.cmpe.procurement.domain.BookRequest;
import edu.sjsu.cmpe.procurement.domain.ShippedBook;

public class StompConfig {

	private BookRequest bookReuqest = null;
	private String order_book_isbns;
	Client client;

	public StompConfig() {
		// Do nothing
	}

	public Connection makeConnection() throws Exception {
		Connection connection = ConnectionObject.getInstance().connection;
		return connection;
	}

	// Method to Receive Message from Queue

	public BookRequest reveiveQueueMessage(Connection connection)
			throws Exception {
		connection.start();
		bookReuqest = new BookRequest();
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		Destination dest = new StompJmsDestination(
				StompConfiguration.getQueueName());
		MessageConsumer consumer = session.createConsumer(dest);
		

		while (true) {

			Message msg = consumer.receive(3000);
			if (msg instanceof TextMessage) {
				String body = ((TextMessage) msg).getText();
				System.out.println("Message = " + body);
				order_book_isbns = body.substring(10);
				bookReuqest.getOrder_book_isbns().add(
						Integer.parseInt(order_book_isbns));

			}
			if (msg == null)
			{
				session.close();
				consumer.close();
				break;
			}
				
		}
		//connection.stop();
		return bookReuqest;
	}

	public void publishTopicMessage(Connection connection, ShippedBook text)
			throws Exception {
		connection.start();
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		for (int isbn_count = 0; isbn_count < text.getShipped_books().size(); isbn_count++) {
			Destination dest = new StompJmsDestination(
					StompConfiguration.getTopicName()
							+ text.getShipped_books().get(isbn_count)
									.getCategory());
			MessageProducer producer = session.createProducer(dest);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage msg = session.createTextMessage(text.getShipped_books()
					.get(isbn_count).getIsbn()
					+ ":"
					+ text.getShipped_books().get(isbn_count).getTitle()
					+ ":"
					+ text.getShipped_books().get(isbn_count).getCategory()
					+ ":"
					+ text.getShipped_books().get(isbn_count).getCoverimage());
			producer.send(msg);
		}

	}

}
