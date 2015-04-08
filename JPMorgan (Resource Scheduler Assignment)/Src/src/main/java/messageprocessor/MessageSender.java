package messageprocessor;

/**
 * MessageSender This class is responsible of sending message to gateway. 
 * It simply reads the messages from LinkedBlockingQueue in a same sequential order as these were inserted 
 * with help of LinkedHashMap.
 */
import java.util.Map.Entry;

import gateway.Gateway;
import gateway.Message;

public class MessageSender implements Runnable, Gateway{

	private MessageContainer messageContainer;
	
	
	public MessageSender(){}
	
	public MessageSender(MessageContainer messageContainer ){
		
		this.messageContainer = messageContainer;
	}
	
	/**
	 * @return the messageContainer
	 */
	public MessageContainer getMessageContainer() {
		return messageContainer;
	}


	/**
	 * @param messageContainer the messageContainer to set
	 */
	public void setMessageContainer(MessageContainer messageContainer) {
		this.messageContainer = messageContainer;
	}


	
	public synchronized void consumeMessages() throws InterruptedException{

		// if (messageContainer.isComplete.get()){    // if we only want to deliver message when all messages are loaded and loading process is complete.  
														// then we can uncomment this if condition otherwise it will check each resource and send it to gateway
		// iterate over the map
		for(Entry<Long, MessageQueue> entry : messageContainer.getMessageHolder().entrySet()){
			MessageQueue msgQueue = entry.getValue();
			Message msg = null;	
			if (!msgQueue.getQueue().isEmpty()){
				msg = msgQueue.getQueue().take();

			}
			
			while(msg != null ){
//				if (!msg.IsComplete()){
					this.send(msg);
					if(!msgQueue.getQueue().isEmpty()){
						msg = msgQueue.getQueue().take();
					}else{
						msg = null;
					}
					
//				}else{
//					
//					System.out.println("All Messages of Group is sent");
//				}

			}
			
		}
//		}  END OF IF 
		if (messageContainer.isComplete.get()){
			messageContainer.getMessageHolder().clear();			
			messageContainer.isComplete.set(false);
			}
			
		}
		
		
		
	
	

	@Override
	public void send(Message msg) {
		
		MessageImpl m2 = (MessageImpl) msg;
		
		System.out.println("Message Sent --- " + m2.getMessagebody().getGroupId() +  m2.getMessagebody().getMessageId() + m2.getMessagebody().getMessage());
		
	}

	@Override
	public void run() {
			while(true){
			try {
				consumeMessages();
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
}
