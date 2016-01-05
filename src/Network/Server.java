package Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Graphique.Bomb;
import MainGame.Game;

public class Server {
	
	private int port ;
	private ServerSocket serverSocket;
	public static final int NB_CLIENT = 4;
	public static int NB_CLIENT_CONNECTED = 0;
	public static CommunicationAvecServeur[] listeClients = new CommunicationAvecServeur[NB_CLIENT];
	public static  Game game;
	private boolean isInit = false;
	private long  TIMER_AT_START = 0, TIMER_EXECUTION = 0;
	private final static long TICK = 128;
	public static void main(String[] args)
	{
		new Server(4444);
	}
	
	
	public Server(int port)
	{
		this.port = port;
		try {
			game = new Game(true);
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
				if(isInit)
				{
					if(game.getTempsExecution() - game.getTimerStart() > Game.getTIMER())
					{
						Game.setCURRENT_TIME(Game.getCURRENT_TIME() - Game.getTIMER());
						Game.setS((Game.getCURRENT_TIME()/Game.getTIMER()) % 60);
						Game.setMin(((Game.getCURRENT_TIME()/Game.getTIMER()) % 3600 ) / 60);
						game.setTimerStart(Bomb.getTime());
					}
					TIMER_EXECUTION = Bomb.getTime();
					if(TIMER_EXECUTION - TIMER_AT_START > TICK)
					{
						broadCast(game);
						TIMER_AT_START=Bomb.getTime();
					}
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
}