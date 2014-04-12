package edu.sjsu.cmpe.library;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sjsu.edu.library.stompMessage.StompMessaging;
import com.sjsu.edu.library.stompMessage.StompConfiguration;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.views.ViewBundle;

import edu.sjsu.cmpe.library.api.resources.BookResource;
import edu.sjsu.cmpe.library.api.resources.RootResource;
import edu.sjsu.cmpe.library.config.LibraryServiceConfiguration;
import edu.sjsu.cmpe.library.repository.BookRepository;
import edu.sjsu.cmpe.library.repository.BookRepositoryInterface;
import edu.sjsu.cmpe.library.ui.resources.HomeResource;

public class LibraryService extends Service<LibraryServiceConfiguration> {

    private final Logger log = LoggerFactory.getLogger(getClass());
    ExecutorService executor;
    Runnable backgroundTask;

    public static void main(String[] args) throws Exception {
	new LibraryService().run(args);
	
	
    }

    @Override
    public void initialize(Bootstrap<LibraryServiceConfiguration> bootstrap) {
	bootstrap.setName("library-service");
	bootstrap.addBundle(new ViewBundle());
	bootstrap.addBundle(new AssetsBundle());

    }

    @Override
    public void run(LibraryServiceConfiguration configuration,
	    Environment environment) throws Exception {
	// This is how you pull the configurations from library_x_config.yml
    StompConfiguration stompconfig= new StompConfiguration();
    stompconfig.setApolloUser(configuration.getApolloUser());
    stompconfig.setPassword(configuration.getApolloPassword());
    stompconfig.setPort(configuration.getApolloPort());
    stompconfig.setQueueName(configuration.getStompQueueName());
    stompconfig.setTopicName(configuration.getStompTopicName());
    stompconfig.setHostName(configuration.getApolloHost());
    stompconfig.setLibraryName(configuration.getLibraryName());
	
	
	
	log.debug("{} - Queue name is {}. Topic name is {}",
		configuration.getLibraryName(), configuration.getStompQueueName(),
		configuration.getStompTopicName());
	// TODO: Apollo STOMP Broker URL and login

	/** Root API */
	environment.addResource(RootResource.class);
	
	/** Books APIs */
	BookRepositoryInterface bookRepository = new BookRepository();
	final StompMessaging stompInstance = new StompMessaging(stompconfig,bookRepository);
	environment.addResource(new BookResource(bookRepository,stompInstance));

	/** UI Resources */
	environment.addResource(new HomeResource(bookRepository));
	int numThreads = 1;
	executor = Executors.newFixedThreadPool(numThreads);

	  backgroundTask = new Runnable() {

		    @Override
		    public void run() {
		 
		    	
		    	try {
					Thread.sleep(3000);
					Connection connect;
					connect = stompInstance.createConnection();
					//stompInstance.subscriber(connect);		
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				}
		    	
		    }
		    

		};
	//executor.execute(backgroundTask);
    }
}
