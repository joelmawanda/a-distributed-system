# a-distributed-system
# TCP Server with Rank-based Command Execution
This is a Java application that implements a TCP server that accepts and holds a maximum of N clients, where N is configurable. These clients are assigned ranks based on first-come-first-serve, i.e whoever connects first receives the next available high rank. Ranks are from 0–N, 0 being the highest rank.
Clients can send to the server commands that the server distributes among the clients. Only a client with a lower rank can execute a command of a higher rank client. Higher rank clients cannot execute commands by lower rank clients, so these commands are rejected. The command execution can be as simple as the client printing to console that command has been executed.
If a client disconnects the server re-adjusts the ranks and promote any client that needs to be promoted not to leave any gaps in the ranks.
# Getting started
# Prerequisites
  •	Java 8 or later
  •	Git
# Clone the repository
git clone https://github.com/yourusername/rank-based-command-execution.git 
# Run the application
  1.	Open a terminal and navigate to the cloned repository.
  2.	Compile the Java classes:
      javac *.java 
  3.	Start the server:
    java CommandController 
    This will start the server on port 9090.
# Sending commands
You can send commands to the server using a TCP client, such as telnet or nc. The commands should be in the following format:
    <rank>: <command> 
For example, to send a command with rank 2, you can run the following command in a separate terminal window:
    echo "2: my command" | nc localhost 9090 
This will send the command my command to the server, and it will be executed by the client with rank 2 if there are no higher ranked clients.
# Stopping the server
To stop the server, you can send the following command:
    stop 
This will stop the server and terminate the application.
# License
This project is licensed under the MIT License - see the LICENSE file for details.




