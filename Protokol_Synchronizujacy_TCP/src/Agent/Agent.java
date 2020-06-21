package Agent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Agent {
	public enum Commands{CLK, NET, END, FIN, SYN, CON, NEW, ACK, DEL}
	
	/////////////Variables declaration/////////////////
	private final String ip = "127.0.0.1";
		//clock info
	long start = System.currentTimeMillis();
	long clock;
		//net info
	HashMap<String, String> activeAddresses=new HashMap<String,String>();
		//personal info
	String name;
	private int port;
	final int halfThePossibleTimeError= 400/2;
	
	    ///Server variables
	private ServerSocket server;
	private boolean isOpen=true;
	ExecutorService executeIt = Executors.newFixedThreadPool(10);
	///////////End variables declaration///////////////
	
    /**
     * Create new Agent with a given name on given port number.<br>
     * Uses for creating new Agents net or single Agent who knows nothing about an existing Agents net
     * @param port - port number on which Agent's server will run
     * @param name - name of new Agent
     * @throws IOException if server could not be run
     */
    public Agent(int port, String name) throws IOException {
    	clock = 0;
    	this.port=port;
    	this.name=name;
    	startServer();
    }
    
	private Agent(long clock, HashMap<String, String> knownAddresses, int port, 
			String name, String superiorName, int superiorPort) 
					throws InterruptedException, IOException {
		this.name=name;
		this.port=port;
		this.clock = clock;
		
		this.activeAddresses = new HashMap<>(knownAddresses);
		this.activeAddresses.put(superiorName, ip+":"+superiorPort);
		
		startServer();
		System.out.println("created agent:"+name+", at port:"+port);
		
		for(String agentName : activeAddresses.keySet()) {
			int existedAgentPort = getPortByName(agentName);
			System.out.println(existedAgentPort);
			informAboutMe(existedAgentPort);
		}
	}

////////////////////////////SERVICE METHODS/////////////////////////
	private void startServer() throws IOException {
		server = new ServerSocket(port);
		
		new Thread(new Runnable() {
			@Override
			public void run(){
				try {
					while (isOpen) {
						Socket client = server.accept();
						executeIt.execute(new ClientHandler(client));
					}
					executeIt.shutdown();
					server.close();
					System.out.println("Serwer, that worked on port "+port+" was closed");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/**
	 * Create new Agent with a given name on given port number with start clock position.
	 * @param name - name of new Agent
	 * @param port - port number on which Agent's server will run
	 * @param clock - starting position of the clock.
	 */
	public void generate(String name, int port,long clock ){
		String myName =this.name;
		int myPort =this.port;
		
		try {
			new Agent(clock, activeAddresses, port, name, myName, myPort);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Synchronize clock with others known active agents.<br>
	 * Method receives the clock states of all known agents, calculates the arithmetic mean of all clocks, 
	 * updates its clock and if the number obtained is not within +\- 200 milliseconds of the current 
	 * state of its own clock, sends the flag "SYN" to all known agents.
	 */
	public void synchronizeClock() {
		ArrayList<String> activeAddressesNames = new ArrayList<String>(activeAddresses.keySet());
		
		if(activeAddressesNames.size()==0) {
		 return;
		}
		
		long allClocksSumm=0;
		
		for(String name : activeAddressesNames) {
			String response =null;
			while(response==null||response.isEmpty()) {
				try {
					response=getMessageResponse(getPortByName(name), Commands.CLK.toString());
				} catch (IOException e) {
					response=null;
				}
			}
			allClocksSumm+=new Long(response);
		}
		
		long myActualClock = getClock();
		allClocksSumm+=myActualClock;
		allClocksSumm/=(activeAddressesNames.size()+1);// arithmetic mean
		
		//set new clock
		clock=allClocksSumm;
		start=System.currentTimeMillis();
		System.out.println(allClocksSumm);
				
		// run synchronization mechanism
		//every agent will be sending "SYN" flag to all until own counter is not in range +/-200 from arithmetic mean of all counters
		if(!((allClocksSumm<=myActualClock+halfThePossibleTimeError)&&(allClocksSumm>=myActualClock-halfThePossibleTimeError))) {
			try {
				sendMessageToEveryone(Commands.SYN.toString());
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
////////////////////////////////GETTERS/////////////////////////////
	/**
	 * @return current state of the agent clock
	 */
	public long getClock() {
		return System.currentTimeMillis()-start+clock;
	}
	
	private int getPortByName(String agentName) {
		return new Integer(activeAddresses.get(agentName).split(":")[1]);
	}
	
	private String getKnownNet() {
		String knownAddressList="";
		for(String name :activeAddresses.keySet() ) {
			knownAddressList = knownAddressList+name+":"+ activeAddresses.get(name)+"/n";
		}
		return knownAddressList;
	}
	
///////////////////////////MESSAGE HANDLERS/////////////////////////	
	/**
	 * Sends a string until the "ACK" flag in the format "ACK sentMessage" is received.
	 * 
	 * @param output - DataOutputStream of Socket on which data should be written.
	 * @param input - DataInputStream of Socket on which data should be written.
	 * @param message - a string to be written.
	 * @throws IOException if an I/O error occurs.
	 */
	public static void sendMessage(DataOutputStream output, DataInputStream input, String message) throws IOException {
		do {
			output.writeUTF(message);
		}while(!input.readUTF().equals(Commands.ACK.toString()+" "+message));
	}
	
	/**
	 * Return a message read from the input stream and sends the ACK 
	 * flag to the output stream in the format "ACK receivedMessage".
	 * @param input - DataInputStream of Socket from which data should be read.
	 * @param output - DataOutputStream of Socket from which data should be read.
	 * @return received message from input stream.
	 * @throws IOException if an I/O error occurs.
	 */
	public static String messageAccepted(DataInputStream input, DataOutputStream output) throws IOException {
    	String message=input.readUTF();
    	output.writeUTF(Commands.ACK.toString()+" "+message);
    	return message;
    	
    }
	
	private void sendMessageToEveryone(String message) throws UnknownHostException, IOException {
		for(String name : activeAddresses.keySet()) {
			Socket socket = new Socket(ip,getPortByName(name));
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			DataInputStream input =  new DataInputStream(socket.getInputStream());
			sendMessage(output,input, message);
			sendMessage(output, input, Commands.END.toString());
			socket.close();
		}
	}
	
	private String getMessageResponse(int port, String message) throws IOException { 
		Socket socket = new Socket(ip,port); 
		DataOutputStream output = new DataOutputStream(socket.getOutputStream());
		DataInputStream input = new DataInputStream(socket.getInputStream()); 
		sendMessage(output, input, message);
		String response = messageAccepted(input, output);
		sendMessage(output, input,Commands.END.toString());
		socket.close();
		return response;
	}
	
	/**
	 * Sent the "CON" flag and personal information in format "agentName;serverIP:serverPort" 
	 * on the given port and IP 127.0.0.1.
	 * 
	 * @param port - port number on which flag "CON" and personal information should be sent.
	 */
	public void informAboutMe(int port) {
		try(Socket socket = new Socket(ip,port); 
				DataOutputStream output = new DataOutputStream(socket.getOutputStream());
				DataInputStream input = new DataInputStream(socket.getInputStream());)
		{  
			sendMessage(output, input, Commands.CON.toString());
			sendMessage(output, input, this.name+";"+this.ip+":"+this.port);
			output.flush();
			sendMessage(output, input, Commands.END.toString());
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	
/////////////////////////CLIENT HANDLER CLASS///////////////////////
	/**
	 * 
	 *
	 */
	private class ClientHandler implements Runnable {
        private Socket clientDialog;
        DataOutputStream output;
        DataInputStream input;
        /**
         * Creates a new client handler that will serve all client requests on the specified socket until 
         * a flag is received to close this connection.
         * @param client - the socket to which the connection has been established.
         * @throws IOException if could not create input or output stream .
         */
        public ClientHandler(Socket client) throws IOException {
            this.clientDialog = client;
            output = new DataOutputStream(clientDialog.getOutputStream());
            input = new DataInputStream(clientDialog.getInputStream());
        }
        @SuppressWarnings("incomplete-switch")
		@Override
        public void run() {
            try {
                boolean isDialogueContinues=true;
                while(isDialogueContinues&&!clientDialog.isClosed()) {
                	String entry = messageAccepted(input,output);
                	System.out.println("Agent "+name+" get message:"+entry+"from "+clientDialog.getPort());
                	switch(Commands.valueOf(entry)) {
                		case NEW: {
                			String[] clock_port_name=messageAccepted(input,output).split(";");
                			generate(clock_port_name[2],new Integer(clock_port_name[1]),new Long(clock_port_name[0]));
                			break;
                		}
		        		case SYN: { 
		        			synchronizeClock();
		        			break;
		        		}
		        		case CLK:{ 
		        			sendMessage(output, input, Long.toString(getClock()));
		        			break;
		        		}
		        		case NET:{
		        			sendMessage(output, input, getKnownNet());
							break;
						}
		        		case CON: {
		        			String message= messageAccepted(input,output);
		        			System.out.println("CON:"+message);
		        			String[] name_IPport=message.split(";");
		        			activeAddresses.put(name_IPport[0],name_IPport[1]);
		        			break;
		        		}
		        		case DEL: {
		        			activeAddresses.remove(messageAccepted(input,output));
		        			break;
		        		}
		        		case END:{
		        			isDialogueContinues=false;
		        			break;
		        		}
		        		case FIN:{
		        			for(String agentName : activeAddresses.keySet()) {
		        				Socket socket = new Socket(ip,getPortByName(agentName));
		        				DataOutputStream output = new DataOutputStream(socket.getOutputStream());
		        				DataInputStream input =  new DataInputStream(socket.getInputStream());
		        				sendMessage(output,input,Commands.DEL.toString());
		        				sendMessage(output, input, name);
		        				sendMessage(output, input, Commands.END.toString());
		        				socket.close();
		        			}
		        			isDialogueContinues=false;
		        			isOpen=false;
		        			break;
		        		}
	                }
                }
                output.flush();
    			clientDialog.close();
    			Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}