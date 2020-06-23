# TCP Synchronizing Protocol
The program represents the simple example of communication based on TCP connection. The main task of agents synchronizing its own clock with others known agents on the network. The network runs on localhost and first agent start its server on port 1000, if available(if not - look for first free port >1000).

To control all active agents and create new help monitor. It automatically connects to every agent as a client and can send 4 flags: CLK — get actual clock status, NET — get known network agents list, SYN — synchronize the clock with the network and FIN — shut down an agent. To open dialog window with an agent you should double click on the agent, you want to talk with, from monitor agent list.

To create a new agent, you should choose parent agent from monitor agent list,  enter start clock position, enter port you want to agent work on and press button "Stworz".

Program done as part of university classes.
