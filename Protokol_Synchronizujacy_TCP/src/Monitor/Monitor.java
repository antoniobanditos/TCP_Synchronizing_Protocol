package Monitor;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;

import Agent.Agent;
import Agent.Agent.Commands;

import java.util.HashMap;



public class Monitor extends JFrame{
	
	private static final long serialVersionUID = 1683326772732486563L;

	// Variables declaration
	private HashMap<String, Socket> agentNameSocket_Map = new HashMap<String, Socket>();

	private final String ip = "127.0.0.1";
	
	//GUI variables
	private JPanel mainPanel;
	private JButton create_Button;
	private JLabel chooseName_Label;
	private JComboBox<String> availableNames_ComboBox;
	private JLabel setCounter_Label;
	private JTextField setCounter_TextField;
	private JLabel chooseParentAgent_Label;
	private JScrollPane activeAgents_ScrollPane;
	private JList<String> activeAgents_List;
	private JLabel setPortNumber_Label;
	private JTextField setPortNumber_TextField;

	private JLabel error_Label;
	//End variables declaration

	public Monitor() throws IOException {
		int startingPort =	1000;
		boolean everythingOK;
		do {
			try{
				new Agent(startingPort, "A");
				Socket firstAgent = new Socket(ip,startingPort);
				agentNameSocket_Map.put("A",firstAgent);
				everythingOK=true;
				System.out.println("started at port:"+startingPort);
			}catch (BindException e) {
				System.err.println("starting port busy");
				startingPort++;
				System.err.println("trying to start at port:"+ startingPort);
				everythingOK=false;
			}catch(IllegalArgumentException e) {
				System.err.println("Can not run a first agent. All ports are busy");
				return;
			}catch(Exception e){
				e.printStackTrace();
				return;
			}
		}while(!everythingOK);
		
		
		initComponents(startingPort);

	}

	private void initComponents(int firstPort) {

		mainPanel = new JPanel();
		create_Button = new JButton();

		chooseName_Label = new JLabel();
		availableNames_ComboBox = new JComboBox<>();

		setCounter_Label = new JLabel();
		setCounter_TextField = new JTextField();

		chooseParentAgent_Label = new JLabel();
		activeAgents_ScrollPane = new JScrollPane();
		activeAgents_List = new JList<>();

		setPortNumber_Label = new JLabel();
		setPortNumber_TextField = new JTextField();

		error_Label = new JLabel();


		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		
		create_Button.setText("Stworz");
		create_Button.setName(""); // NOI18N
		create_Button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					create_ButtonActionEvent(evt);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		availableNames_ComboBox.setModel(new DefaultComboBoxModel<>(new String[] { "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P" }));
		
		chooseName_Label.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
		chooseName_Label.setText("Wybierz nazwe Agenta");

		setCounter_Label.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
		setCounter_Label.setText("Ustaw licznik");

		setCounter_TextField.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

		activeAgents_List.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
		activeAgents_List.setModel(new DefaultListModel<String>());
		((DefaultListModel<String>)activeAgents_List.getModel()).addElement("A;"+ip+":"+firstPort);
		activeAgents_List.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				activeAgents_ListMouseClicked(evt);
			}
		});
		activeAgents_ScrollPane.setViewportView(activeAgents_List);

		chooseParentAgent_Label.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
		chooseParentAgent_Label.setText("Wybiez Agenta ktory stworzy nowego");

		setPortNumber_Label.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
		setPortNumber_Label.setText("Ustaw port roboczy(>1000)");

		setPortNumber_TextField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

		GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
		mainPanel.setLayout(mainPanelLayout);
		mainPanelLayout.setHorizontalGroup(
				mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(mainPanelLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(mainPanelLayout.createSequentialGroup()
										.addComponent(chooseParentAgent_Label, GroupLayout.PREFERRED_SIZE, 325, GroupLayout.PREFERRED_SIZE)
										.addGap(0, 0, Short.MAX_VALUE))
								.addGroup(mainPanelLayout.createSequentialGroup()
										.addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
												.addGroup(mainPanelLayout.createSequentialGroup()
														.addGap(10, 10, 10)
														.addComponent(activeAgents_ScrollPane, GroupLayout.PREFERRED_SIZE, 214, GroupLayout.PREFERRED_SIZE)
														.addGap(53, 53, 53)
														.addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(setPortNumber_Label, GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
																.addGroup(mainPanelLayout.createSequentialGroup()
																		.addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
																				.addComponent(setCounter_TextField, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)
																				.addComponent(setCounter_Label, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE)
																				.addComponent(setPortNumber_TextField, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE))
																		.addGap(0, 0, Short.MAX_VALUE))))
												.addGroup(mainPanelLayout.createSequentialGroup()
														.addGap(28, 28, 28)
														.addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addGroup(mainPanelLayout.createSequentialGroup()
																		.addComponent(chooseName_Label, GroupLayout.PREFERRED_SIZE, 228, GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(availableNames_ComboBox, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
																.addGroup(mainPanelLayout.createSequentialGroup()
																		.addGap(105, 105, 105)
																		.addComponent(create_Button, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
																		.addGap(18, 18, 18)
																		.addComponent(error_Label, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE)))))
										.addContainerGap())))
				);
		mainPanelLayout.setVerticalGroup(
				mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(chooseName_Label, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
								.addComponent(availableNames_ComboBox, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(chooseParentAgent_Label, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(activeAgents_ScrollPane, GroupLayout.PREFERRED_SIZE, 249, GroupLayout.PREFERRED_SIZE)
								.addGroup(mainPanelLayout.createSequentialGroup()
										.addComponent(setCounter_Label, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
										.addGap(18, 18, 18)
										.addComponent(setCounter_TextField, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
										.addGap(18, 18, 18)
										.addComponent(setPortNumber_Label, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
										.addGap(3, 3, 3)
										.addComponent(setPortNumber_TextField, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)))
						.addGap(18, 18, 18)
						.addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(create_Button, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
								.addComponent(error_Label, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE))
						.addGap(36, 36, 36))
				);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				);
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, 469, GroupLayout.PREFERRED_SIZE)
				);
		
		pack();
		setTitle("Monitor");
		setLocationRelativeTo(null);
	}                       

	private void create_ButtonActionEvent(ActionEvent evt) 
			throws UnknownHostException, IOException, InterruptedException {     
		
		error_Label.setText("");
		String selectedName = new String(availableNames_ComboBox.getSelectedItem().toString());//<--------------
		char c[] =setCounter_TextField.getText().toCharArray();
		if(c.length==0){
			error_Label.setText("Niema danych licznika");
			setCounter_TextField.setText("");
			return;
		}

		for(int i = 0; i<c.length;i++){
			if(!Character.isDigit(c[i])){
				error_Label.setText("Niepoprawne dane licznika");
				return;
			}
		}
		long licz = new Long(setCounter_TextField.getText());//<-----------------------

		if(activeAgents_List.getSelectedValue()==null) {
			error_Label.setText("Nie wybrano agenta-ojca");
			return;
		}
		String nazojc = new String(activeAgents_List.getSelectedValue().substring(0,1));//<------------------
		c =setPortNumber_TextField.getText().toCharArray();
		if(c.length==0){
			error_Label.setText("Niema danych o porcie");
			return;
		}
		for(int i = 0; i<c.length;i++){
			if(!Character.isDigit(c[i])){
				error_Label.setText("Niepoprawne dane portu");
				setPortNumber_TextField.setText("");
				return;
			}
		}
		
		int port = new Integer(setPortNumber_TextField.getText());//<-----------------------
		
		if(port<=1000||port>65535) {
			error_Label.setText("Niepoprawne dane portu");
			setPortNumber_TextField.setText("");
			return;
		}
		for(Socket socket : agentNameSocket_Map.values()) {
			if(socket.getPort()==port) {
				error_Label.setText("Taki port juz istnieje");
				setPortNumber_TextField.setText("");
				return;
			}
		}
		

		setPortNumber_TextField.setText("");
		setCounter_TextField.setText("");
		
		DataOutputStream output = new DataOutputStream(agentNameSocket_Map.get(nazojc).getOutputStream());
		DataInputStream input = new DataInputStream(agentNameSocket_Map.get(nazojc).getInputStream());
		Agent.sendMessage(output, input, Commands.NEW.toString());
		Agent.sendMessage(output, input, licz+";"+port+";"+selectedName);

		availableNames_ComboBox.removeItemAt(availableNames_ComboBox.getSelectedIndex()); 
		agentNameSocket_Map.put(selectedName, new Socket(ip,port));
		DefaultListModel<String> model = (DefaultListModel<String>) activeAgents_List.getModel();
		model.addElement(selectedName+";"+ip+":"+port);
		activeAgents_List.setModel(model);
	}                    

	private void activeAgents_ListMouseClicked(MouseEvent evt) {                                
		if(evt.getClickCount()==2) {
			Monitor parentWindow = this;
			String agentName = activeAgents_List.getSelectedValue().substring(0, 1);
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						AgentWindow.createAgentWindow(activeAgents_List.getSelectedValue(), 
								agentName, agentNameSocket_Map.get(agentName),parentWindow);
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
			}).start();;
		}
	}
	
	/**
	 * @return the List of active agents
	 */
	public JList<String> getActiveAgents_List() {
		return activeAgents_List;
	}

	/**
	 * @return the Map, witch contains as key - name of an agent, as value - socket, 
	 * on which the monitor communicates with the agent 
	 */
	public HashMap<String, Socket> getAgentNameSocket_Map() {
		return agentNameSocket_Map;
	}         
	
	/**
	 * @return the ComboBox, witch contains all available names for new agent
	 */
	public JComboBox<String> getAvailableNames_ComboBox() {
		return availableNames_ComboBox;
	}
	
	
	
	

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(Monitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(Monitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(Monitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(Monitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Monitor().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
