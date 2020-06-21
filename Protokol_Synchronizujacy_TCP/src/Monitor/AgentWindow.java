package Monitor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Agent.Agent;
import Agent.Agent.Commands;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class AgentWindow  extends JFrame {
	private static final long serialVersionUID = -203767638765449807L;
	//Variables declaration
	Socket socket;
	private final Monitor parentWindow;
	private final String agentName;
	
	private DataOutputStream outputStream;
	private DataInputStream inputStream;
	//GUI variables
	private JPanel mainPanel;
	
    private JLabel selectCommand_Label;
    private JComboBox<String> availableCommands_ComboBox;
    private JButton podaj_Button;
    
    private JLabel śieć_Label;
    private JScrollPane network_ScrollPane;
    private JList<String> network_List;
    
    private JLabel zegar_Label;
    private JTextPane clock_TextPane;
    //End variables declaration
	
	
	
 
    private AgentWindow(String windowName,String agentName, Socket socket, Monitor parentWindow) 
    		throws IOException {
    	this.parentWindow=parentWindow;
    	this.agentName=agentName;
    	this.socket=socket;
    	setTitle(windowName);
        outputStream = new DataOutputStream(socket.getOutputStream());
        inputStream = new DataInputStream(socket.getInputStream());
        
        initComponents();
    }

                 
    private void initComponents() {

        mainPanel = new JPanel();
        selectCommand_Label = new JLabel();
        availableCommands_ComboBox = new JComboBox<>();
        podaj_Button = new JButton();
        zegar_Label = new JLabel();
        clock_TextPane = new JTextPane();
        network_ScrollPane = new JScrollPane();
        network_List = new JList<>();
        śieć_Label = new JLabel();
      

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        mainPanel.setFont(new Font("Tahoma", 0, 20)); // NOI18N

        selectCommand_Label.setFont(new Font("Tahoma", 0, 24)); // NOI18N
        selectCommand_Label.setText("Podaj Komende");

        availableCommands_ComboBox.setFont(new Font("Tahoma", 0, 18)); // NOI18N
        availableCommands_ComboBox.setModel(
        		new DefaultComboBoxModel<>(
        				new String[] { 
        				Commands.NET.toString(),
        				Commands.CLK.toString(),
        				Commands.SYN.toString(),
        				Commands.FIN.toString()}));
        
        availableCommands_ComboBox.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent event) {
        		availableCommands_ComboBoxItemStateChanged();
        	}
        });
        

        podaj_Button.setText("Podaj");
        podaj_Button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt) {
                try {
					podaj_ButtonActionPerformed();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });

        zegar_Label.setFont(new Font("Tahoma", 0, 36)); // NOI18N
        zegar_Label.setText("Zegar");
        
        network_List.setModel(new DefaultListModel<String>());
        network_ScrollPane.setViewportView(network_List);

        śieć_Label.setFont(new Font("Tahoma", 0, 24)); // NOI18N
        śieć_Label.setText("Śieć");
        śieć_Label.setToolTipText("");
        
        clock_TextPane.setFont(new Font("Tahoma", Font.PLAIN, 18));
        clock_TextPane.setEditable(false);

        GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
        mainPanelLayout.setHorizontalGroup(
        	mainPanelLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(mainPanelLayout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(mainPanelLayout.createSequentialGroup()
        					.addComponent(śieć_Label, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
        					.addGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING)
        						.addGroup(mainPanelLayout.createSequentialGroup()
        							.addGap(155)
        							.addComponent(selectCommand_Label, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE))
        						.addGroup(mainPanelLayout.createSequentialGroup()
        							.addGap(199)
        							.addComponent(availableCommands_ComboBox, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)))
        					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        				.addGroup(mainPanelLayout.createSequentialGroup()
        					.addComponent(network_ScrollPane, GroupLayout.PREFERRED_SIZE, 202, GroupLayout.PREFERRED_SIZE)
        					.addGap(18)
        					.addGroup(mainPanelLayout.createParallelGroup(Alignment.TRAILING)
        						.addGroup(mainPanelLayout.createSequentialGroup()
        							.addComponent(podaj_Button, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
        							.addContainerGap(30, Short.MAX_VALUE))
        						.addGroup(mainPanelLayout.createSequentialGroup()
        							.addGap(0, 0, Short.MAX_VALUE)
        							.addGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING)
        								.addComponent(zegar_Label, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)
        								.addComponent(clock_TextPane, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE))
        							.addGap(266))))))
        );
        mainPanelLayout.setVerticalGroup(
        	mainPanelLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(mainPanelLayout.createSequentialGroup()
        			.addGroup(mainPanelLayout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(mainPanelLayout.createSequentialGroup()
        					.addGap(6)
        					.addComponent(selectCommand_Label, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
        					.addGap(20)
        					.addComponent(availableCommands_ComboBox, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        				.addGroup(mainPanelLayout.createSequentialGroup()
        					.addContainerGap(34, Short.MAX_VALUE)
        					.addComponent(śieć_Label, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
        					.addGap(27)))
        			.addGroup(mainPanelLayout.createParallelGroup(Alignment.TRAILING)
        				.addComponent(network_ScrollPane, GroupLayout.PREFERRED_SIZE, 195, GroupLayout.PREFERRED_SIZE)
        				.addGroup(mainPanelLayout.createSequentialGroup()
        					.addComponent(zegar_Label, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(clock_TextPane, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
        					.addGap(35)
        					.addComponent(podaj_Button, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)))
        			.addGap(48))
        );
        mainPanel.setLayout(mainPanelLayout);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, 419, GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 341, GroupLayout.PREFERRED_SIZE)
        );
        
        pack();
    }            

    private void podaj_ButtonActionPerformed() throws IOException {
    	
    	switch((String)availableCommands_ComboBox.getSelectedItem()){
    		case "SYN": {
    	        Agent.sendMessage(outputStream, inputStream, Commands.SYN.toString());
    	        break;
    		}
    		case "CLK":{
    			Agent.sendMessage(outputStream, inputStream, Commands.CLK.toString());
    			clock_TextPane.setText(Agent.messageAccepted(inputStream, outputStream));
    			break;
    		}
    		case "NET":{
    			Agent.sendMessage(outputStream, inputStream, Commands.NET.toString());
    			String str []= Agent.messageAccepted(inputStream, outputStream).split("/n");
    			DefaultListModel<String> listModel=(DefaultListModel<String>) network_List.getModel();
    			listModel.removeAllElements();
    			for(int i =0; i<str.length;i++) {
    				listModel.addElement(str[i]);
    			}
    			break;
    		}
    		case "FIN":{
    			this.dispose();
    			
    			Agent.sendMessage(outputStream, inputStream, Commands.FIN.toString());
    			outputStream.close();
    			inputStream.close();
    			socket.close();

    			parentWindow.getAgentNameSocket_Map().remove(agentName);	
    			
    			DefaultListModel<String> listModel = (DefaultListModel<String>)parentWindow.getActiveAgents_List().getModel();
    			System.out.println(getTitle());
    			listModel.removeElement(getTitle());
    			parentWindow.getActiveAgents_List().setModel(listModel);
    			parentWindow.getAvailableNames_ComboBox().insertItemAt(agentName, agentName.charAt(0));//TODO write proper agentname position counting
    			
    			break;
    		}
    	}
    }         
    
    private void availableCommands_ComboBoxItemStateChanged() {
    	if(parentWindow.getActiveAgents_List().getModel().getSize()==1 && 
    			((String)availableCommands_ComboBox.getSelectedItem()).equals("FIN") ) {
    		podaj_Button.setEnabled(false);
    	}else {
    		podaj_Button.setEnabled(true);
    	}
    }


    /**
     * Return new window which represent agent on specified socket and let to communicate with it 
     * 
     * @param windowName - the full name of the represented agent as it is on a monitor list.
     * @param agentName - the name of the agent that this window will represent.
     * @param socket - socket on which a monitor communicates with the represented agent.
     * @param parentWindow - monitor that want to create new AgentWindow.
     * @return window representing specified agent.
     * @throws IOException if an I/O error occurs.
     */
    public static AgentWindow createAgentWindow(String windowName,String agentName, Socket socket, Monitor parentWindow) 
    		throws IOException {
    	AgentWindow window = new AgentWindow(windowName,agentName,socket,parentWindow);
		window.setVisible(true);
		return window;
				
    }
}
