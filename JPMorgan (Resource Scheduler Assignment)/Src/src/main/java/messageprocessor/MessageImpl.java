package messageprocessor;

import gateway.Message;

public class MessageImpl implements Message{

	private MessageBody messagebody;
	private volatile boolean messageComplete;

	public MessageImpl() {
	}
	public MessageImpl(MessageBody messagebody) {
		super();
		this.messagebody = messagebody;
	}


	@Override
	public void completed() {
		messageComplete = true;
		System.out.println("Message Completely sent");
	}


	/**
	 * @return the messagebody
	 */
	public MessageBody getMessagebody() {
		return messagebody;
	}


	/**
	 * @param messagebody the messagebody to set
	 */
	public void setMessagebody(MessageBody messagebody) {
		this.messagebody = messagebody;
	}


	@Override
	public boolean isComplete() {

		return messageComplete;
	}
	

	
	
	
}
