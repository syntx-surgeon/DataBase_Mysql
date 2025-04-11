package com.example.demo.newpackage.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer{
	private ServerSocket serverSocket;
	private List<SocketClient> socketClients = Collections.synchronizedList(new ArrayList<>());
	ConcurrentHashMap<String,SocketClient> mapClients = new ConcurrentHashMap<>();
	public static void main(String[] args){
		ChatServer chatServer = new ChatServer();
		chatServer.startServer(4000);
	}

	public void startServer(int port){
		try {
			serverSocket = new ServerSocket(port);
			System.out.printf("Server Started on port %s%n",port);
			while(true) {
				Socket socket = serverSocket.accept();
				System.out.println("Client Connected");
				SocketClient client = new SocketClient(socket,socketClients);
				client.start();
			}
		}catch (IOException exception){
			System.out.println(exception.getMessage());
		}
		}
}