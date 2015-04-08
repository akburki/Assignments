package messageprocessor;

import gateway.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class MessageQueue {

	public MessageQueue() {
	}

	public MessageQueue(BlockingQueue<Message> queue) {
		super();
		this.queue = queue;
	}

	private BlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();

	/**
	 * @return the queue
	 */
	public BlockingQueue<Message> getQueue() {
		return queue;
	}

	/**
	 * @param queue the queue to set
	 */
	public void setQueue(BlockingQueue<Message> queue) {
		this.queue = queue;
	}
	
}
