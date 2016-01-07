package Network;

import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import MainGame.Game;

public class Server {
	
	private int port ;
	private ServerSocket serverSocket;
	public static final int NB_CLIENT = 4;
	public static int NB_CLIENT_CONNECTED = 0;
	public static CommunicationAvecServeur[] listeClients = new CommunicationAvecServeur[NB_CLIENT];
	private boolean isInit = false;
	public static Game game;
	public static String commande;
	public static int delta;
	private Sound bonusSound,bombExplode, background;
	public static void main(String[] args)
	{
		try {
			new Server(4444);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public Server(int port) throws SlickException
	{
		this.port = port;
		try {
			game = new Game(true);
			bonusSound = new Sound("sons/ramasserBonus.wav");
			bombExplode = new Sound("sons/bombExplode.wav");
			background = new Sound("sons/builtToFall.wav");
			game.setBackground(background);
			game.setBombExplode(bombExplode);
			game.setBonusSound(bonusSound);
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
				if(NB_CLIENT_CONNECTED == NB_CLIENT && !isInit)
				{
					isInit = true;
					broadCast(game);
				}
				if(isInit)
				{
					//Traitement de la liste
					while(!CommunicationAvecServeur.listeDeMessage.isEmpty())
					{
						Object o = CommunicationAvecServeur.listeDeMessage.poll();
						if(o instanceof String)
						{
							broadCast((String)o);
						}
						else if( o instanceof Integer)
						{
							Integer i = (int)o;
							Float f = (float)i;
							broadCast(f);
						}
					}
				}
			}
		}
		catch(ConnectException c)
		{
			System.out.println("Un problème de connexion au serveur est survenue");
		}
		catch(SocketException s)
		{
			serverSocket = null;
			System.out.println("Erreur fatale - Fermeture du serveur");
		}
		catch (IOException e) {
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