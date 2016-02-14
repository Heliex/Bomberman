package Network;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import Graphique.Case;
import MainGame.Game;

public class Server {
	
	private int port ;
	private File lvl ;
	private Case[][] board = new Case[Game.NB_CASE_HAUTEUR][Game.NB_CASE_LARGEUR];
	private ServerSocket serverSocket;
	public static final int NB_CLIENT = 4;
	public static int NB_CLIENT_CONNECTED = 0;
	public static CommunicationAvecServeur[] listeClients = new CommunicationAvecServeur[NB_CLIENT];
	private boolean isInit = false;
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
			lvl = new File("niveaux/" + Game.LEVEL + Game.LEVEL_START+ ".txt");
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
					System.out.println("Numéro de client affecté : " + NB_CLIENT_CONNECTED);
					CommunicationAvecServeur com = new CommunicationAvecServeur(client);
					Thread t = new Thread(com);
					t.start();
					listeClients[NB_CLIENT_CONNECTED] = com;
					NB_CLIENT_CONNECTED++;
					broadCast(NB_CLIENT_CONNECTED + ":NB_CLIENT");
				}
				if(NB_CLIENT_CONNECTED == NB_CLIENT && !isInit)
				{
					isInit = true;
					broadCast("GO");
					System.out.println("GO");
					initLevel(new File("niveaux/" + Game.LEVEL + Game.LEVEL_START+ ".txt"));
					broadCast(board);
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
						else if(o instanceof Case[][])
						{
							broadCast((Case[][])o);
						}
					}
				}
				if(NB_CLIENT_CONNECTED == 0)
				{
					System.out.println("Tous les clients se sont déconnectés, le serveur va se fermer automatiquement...");
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
				}catch(NullPointerException n)
				{
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
		}
	}
	

	public void initLevel(File f)
	{
		// Read the level file
				try {
					BufferedReader reader = new BufferedReader(new FileReader(f));
					String line;
					String type = "";
					int i = 0;
					while((line = reader.readLine()) != null) // While you got line
					{
						for(int j = 0 ; j < line.length(); j++)
						{
							switch(line.charAt(j))
							{
							case '0':
								type=Game.GROUND;
								break;
							case '1':
								type=Game.GRASSGROUND;
								break;
							case '2':
								type=Game.WALL;
								break;
							case '3':
								type=Game.INDESTRUCTIBLE;
								break;
							case '4':
								type=Game.HOUSE;
								break;
							case '5':
								type=Game.WOOD;
								break;
							case '6':
								type=Game.GROUNDGRASSTEXAS;
								break;
							case '7':
								type=Game.GROUNDTEXAS;
								break;
							case '8':
								type=Game.EMPTY;
								break;
							}
							
							board[i][j] = new Case(j,i,type); // Create a case with the type set before
						}
						i++;
					}
					reader.close(); // Close the buffer
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException io) {
					// TODO Auto-generated catch block
					io.printStackTrace();
				}
	}
}