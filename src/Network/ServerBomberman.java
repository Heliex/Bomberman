package Network;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import MainGame.Game;


public class ServerBomberman {
	public static final int NB_CLIENTS_MAX = 4;
	public static int NB_CLIENTS_CONNECTES = 0;
	public static ServerSocket serverSocket ;
	public static Game game = new Game(true);
	
	public static void main(String[] args) throws IOException
	{
		boolean listening = true;
		try
		{
			serverSocket = new ServerSocket(4444);
		}
		catch(IOException io)
		{
			System.out.println("Impossible d'ecouter sur le port 4444");
			io.printStackTrace();
		}
		
		while(listening)
		{
			try
			{
				Socket clientSocket = serverSocket.accept();
				MiniServer mini = new MiniServer(clientSocket,game);
				mini.start();
				NB_CLIENTS_CONNECTES++;
			}
			catch(SocketException socket)
			{
				socket.printStackTrace();
			}
		}
		serverSocket.close();
	}
}
