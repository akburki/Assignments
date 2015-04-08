package messageprocessor;

/**
 * MessageContainer
 *  this class is responsible for holding all information before it deliver to gateway. 
 *  To contain information I used LinkedHashMap which is indexed by group id (key = groupid of message)
 *   where the list of messages are store in queue format as LinkedBlockingQueue.
 *   Map<Long, MessageQueue> messageHolder = new LinkedHashMap<Long, MessageQueue>();
 *   MessageQueue = LinkedBlockingQueue
 *   The main reason of LinkedHashMap selection Is because its data structure is hash table and 
 *   linked list implementation as in this problem we want to deliver the message based on their group sequence.
 *   And the next of using LinkedBlockingQueue is to traverse the list of message as entered and as a queue that additionally supports operations that wait for the queue to become non-empty when retrieving an element, and wait for space to become available in the queue when storing an element.
 */


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public class MessageContainer {

	 
	private Map<Long, MessageQueue> messageHolder = new LinkedHashMap<Long, MessageQueue>();
	public AtomicBoolean isComplete = new AtomicBoolean(false);

	/**
	 * @return the messageHolder
	 */
	public Map<Long, MessageQueue> getMessageHolder() {
		return messageHolder;
	}

	/**
	 * @param messageHolder the messageHolder to set
	 */
	public void setMessageHolder(Map<Long, MessageQueue> messageHolder) {
		this.messageHolder = messageHolder;
		
	
	}
	
	
	public synchronized void addMessage(MessageBody msg){
		
		
		MessageQueue queue = this.messageHolder.get((msg.groupId));
		
		if (queue != null){
			
				queue.getQueue().add(new MessageImpl(msg));
					
		}else
		{
				
			queue = new MessageQueue();
			queue.getQueue().add(new MessageImpl(msg));
			this.messageHolder.put(msg.groupId, queue) ;
			
		}
		
		
	}

	public synchronized void removeMessageHolder(Long key) {
		messageHolder.remove(key);
		
	}
	
}
