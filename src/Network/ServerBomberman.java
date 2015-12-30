package Network;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import MainGame.Game;


public class ServerBomberman {
	public static final int NB_CLIENTS_MAX = 4;
	public static int NB_CLIENTS_CONNECTES = 0;
	public static ServerSocket serverSocket ;
	public static Game game = new Game(true);
	public static ArrayList<GestionClient> listeClient = new ArrayList<GestionClient>();
	
	public static void main(String[] args) throws IOException
	{
		boolean listening = true,sent = false;
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
			while(NB_CLIENTS_CONNECTES < NB_CLIENTS_MAX)
			{
				try
				{
					Socket client = serverSocket.accept();
					GestionClient gestion = new GestionClient(client);
					gestion.start();
					listeClient.add(gestion);
					NB_CLIENTS_CONNECTES++;
					for( GestionClient g : listeClient)
					{
						if(g!= null)
						{
							g.sendMessage("Joueurs connectés : " + NB_CLIENTS_CONNECTES + "/" + NB_CLIENTS_MAX);
						}
					}
					System.out.println("Connexion du client : " + NB_CLIENTS_CONNECTES);
				}
				catch(SocketException socket)
				{
					socket.printStackTrace();
				}
			}
			
			for(GestionClient g : listeClient)
			{
				if(!sent)
				{
					g.sendMessage("La partie va commencée");
					g.readMessage();
				}
			}
			sent = true;
		}
		serverSocket.close();
	}
}
