/**
 * ResourceScheduler 
 * This is the main class which kickoff loader and sender processes with the help of thread executor service.
 * For simplicity I have selected newFixedThreadpool with pool of two thread one for each process.
 */
package main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import messageprocessor.MessageContainer;
import messageprocessor.MessageLoader;
import messageprocessor.MessageSender;


public class ResourceScheduler {

	/**
	 * @param args
	 */
	
	
	public static void main(String[] args) {
		
		
		MessageContainer msgContainer =  new MessageContainer();		
		MessageLoader msgLoader = new MessageLoader(msgContainer);
		MessageSender msgSender = new MessageSender(msgContainer);		
		ExecutorService threadPool = Executors.newFixedThreadPool(2);		
		threadPool.execute(msgLoader);
		threadPool.execute(msgSender);
		
	}

}
