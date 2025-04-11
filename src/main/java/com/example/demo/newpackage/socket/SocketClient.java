package com.example.demo.newpackage.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class SocketClient extends Thread{
	private final Socket socket;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	private String username;
	List<SocketClient> socketClients;
	public SocketClient(Socket socket,List<SocketClient> socketClients){
		this.socket = socket;
		this.socketClients = socketClients;
	}

	@Override
	public void run() {
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			printWriter = new PrintWriter(socket.getOutputStream());
			printWriter.println("Please Insert Your Username?");
			printWriter.flush();
			username = bufferedReader.readLine();
			printWriter.println(String.format("Welcome %s", username));
			printWriter.flush();
			sendMessageToAll(String.format("%s has Join the chat.",username));
			socketClients.add(this);
			String message;
			while ((message = bufferedReader.readLine()) != null) {
						if(message.startsWith("/p")){
							message = message.replace("/p ","");
							String[] messagePaylaod = message.split(" ");
							String privateUsername = messagePaylaod[0];
							message = message.replace(privateUsername,"");
							sendPrivateMessage(privateUsername,message);
						}else {
							String formatMessage = String.format("%s >> %s", username, message);
							sendMessageToAll(formatMessage);
						}
				if ("bye".equals(message)) {
					socket.close();
					socketClients.remove(this);
					break;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void sendMessageToAll(String message){
		System.out.println(message);
		socketClients.forEach(client->{
			if(!client.getUsername().equals(username)) {
				client.sendMessage(message);
			}
		});
	}

	private void sendPrivateMessage(String username,String message){
		socketClients.forEach(client->{
			if(client.getUsername().equals(username)) {
				client.sendMessage(message);
			}
		});
	}
//WIFI PASSWORD: 0987654321
	private void sendMessage(String message){
		printWriter.println(message);
		printWriter.flush();
	}

	public String getUsername(){
		return this.username;
	}
}