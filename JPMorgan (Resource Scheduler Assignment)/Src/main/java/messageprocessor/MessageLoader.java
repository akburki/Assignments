package messageprocessor;

/**
 * MessageLoader   
 * this class reads files from location of resources mentioned in configuration file and 
 * for each resource I have created new thread through ReadMessage.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessageLoader implements Runnable{
	
	private List<File> resourceList = new ArrayList<File>();
	MessageContainer messageContainer; 
	final String CONFIG_FILE_PATH = "config/ResourceConfig.xml";
	
	public MessageLoader(){
		
		initialise();
	}
	
	public MessageLoader(MessageContainer messageContainer){
		
		initialise();
		this.messageContainer = messageContainer;
	}

	public void initialise(){
		
		setResouseList();
		
	}

	@Override
	public void run(){
	
			try {
				while(true){
					if (!messageContainer.isComplete.get()){
					loadResource();
					}
					Thread.sleep(100);
					}	
				}catch (InterruptedException e) {
					e.printStackTrace();
				}
	}
	
	
	private void setResouseList(){
		
		try{
			File fXmlFile = new File(CONFIG_FILE_PATH);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			
			NodeList nList = doc.getElementsByTagName("Directory");
			 
			for (int index = 0 ; index < nList.getLength() ; index++){			
				Element element = (Element) nList.item(index);
				resourceList.add(new File(element.getTextContent()));			
			}
			
		}catch(Exception e){
			e.printStackTrace();			
		}		
	}
	
	private synchronized void loadResource() throws InterruptedException {
		
		Iterator<File> itr = resourceList.iterator();
				
				while( itr.hasNext()){
					File file = itr.next();
					if (file.isDirectory()){
						File [] files = file.listFiles();
						for(File tempFile : files){
							
							new  ReadMessage(tempFile);
							tempFile.delete();
						}	
					
					}
				}
				
		
	}

	
	class ReadMessage extends Thread{
		
		File file;
		public ReadMessage(){
			
			
		}
		public ReadMessage(File file){
			this.file = file;
			this.run();
		}
		
		@Override
		public void run(){
		
			try{				
				readMessages(this.file);
		//		Thread.sleep(10);
			}catch(IOException e){				
				e.printStackTrace();				
			} 
//			catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		
		
		private void readMessages(File file) throws IOException{
			
			 BufferedReader br = new BufferedReader (new FileReader(file));
			    try {		     
			        String line = br.readLine();	        
			        while (line != null) {
			        	addMessage(line);
			            line = br.readLine();
			        }		        
			    } finally {
			        br.close();
			    }		
		}	
		
		
		public synchronized void addMessage(String str){
			
			String delims = "|";
			StringTokenizer stokenizer = new StringTokenizer(str, delims);
			String[] tokens = new String[stokenizer.countTokens()];
			MessageBody msg = new MessageBody();
			
			if(stokenizer.countTokens() == 3){
			msg.setGroupId(new Long(stokenizer.nextToken()));
			msg.setMessageId(new Long(stokenizer.nextToken()));
			msg.setMessage(stokenizer.nextToken());
			}else if(stokenizer.countTokens() == 4){
				msg.setGroupId(new Long(stokenizer.nextToken()));
				msg.setMessageId(new Long(stokenizer.nextToken()));
				msg.setMessage(stokenizer.nextToken());
				msg.setStatus(stokenizer.nextToken());
			}else if(str.equalsIgnoreCase("complete")) {
				
				messageContainer.isComplete.set(true);	
			}
			if (!messageContainer.isComplete.get())
				messageContainer.addMessage(msg);
			
		}
		
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
	
	
	

}
