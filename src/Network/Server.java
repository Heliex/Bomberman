package Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import MainGame.Game;

public class Server {
	
	private int port ;
	private ServerSocket serverSocket;
	public static final int NB_CLIENT = 4;
	public static int NB_CLIENT_CONNECTED = 0;
	public static CommunicationAvecServeur[] listeClients = new CommunicationAvecServeur[NB_CLIENT];
	private Game game;
	private boolean isInit = false;
	public static void main(String[] args)
	{
		new Server(4444);
	}
	
	
	public Server(int port)
	{
		this.port = port;
		try {
			this.game = new Game();
			System.out.println("Mise en route du serveur...");
			this.serverSocket = new ServerSocket(port);
			System.out.println("Serveur démarré....");
			while(!serverSocket.isClosed())
			{
				while(NB_CLIENT_CONNECTED < NB_CLIENT)
				{
					System.out.println("En attente de connexion d'un client....");
					Socket client = serverSocket.accept();
					System.out.println("Un Client se connecte...");
					CommunicationAvecServeur com = new CommunicationAvecServeur(client);
					Thread t = new Thread(com);
					t.start();
					listeClients[NB_CLIENT_CONNECTED] = com;	
					NB_CLIENT_CONNECTED++;
				}
				if(!isInit)
				{
					isInit = true;
					broadCast(game);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getPort()
	{
		return this.port;
	}
	
	public static void broadCast(Object message)
	{
		for(int i = 0 ; i < NB_CLIENT ; i++)
		{
			if(listeClients[i] != null)
			{
				try {
					listeClients[i].getOut().writeObject(message);
					listeClients[i].getOut().flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
		}
	}
	
	public void setGame(Game game)
	{
		this.game = game;
	}
	
	public Game getGame()
	{
		return this.game;
	}
}