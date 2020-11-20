package Network;
/**
 * Classe qui permet de cr�er la socket c�t� client pour �changer avec le serveur
 * @author Heliex
 * @date   05/04/2017
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Logique.GameLogique;

public class ThreadClient extends Thread{
	
	// Num�ro de client fourni par le serveur
	private int numClient;
	
	// Objet GameLogique qui est conserv� pour permettre a l'UI de compar� son instance local avec celle du serveur
	private GameLogique gl = null;
	// Adresse IP de connexion au serveur.
	public final static String IP = "127.0.0.1";
	
	// Socket cr�er par l'interfaceGraphique de l'UI.
	private Socket client;
	
	// ObjectInputStream qui permet de lire le contenu de la socket (envoy� par le serveur)
	private ObjectInputStream in;
	
	// ObjectOutputStream qui permet d'�crire dans la socket (�crit c�t� client)
	private ObjectOutputStream out;
	
	// Timer pour savoir quand envoy� le gameLogique
	private long startTimer;
	
	private boolean exit;
	
	/**
	 * Constructeur public du ThreadClient
	 * Cr�e la socket pour faire des �changes avec le serveur
	 * D�clare des attributs qui permettent d'�crire/lire dans la socket
	 */
	public ThreadClient()
	{
		this.startTimer = System.currentTimeMillis();
		this.exit = false;
		try {
			this.client = new Socket(IP,Serveur.NUMPORT);
			this.in = new ObjectInputStream(client.getInputStream());
			this.out = new ObjectOutputStream(client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Methode qui permet de lire les donn�es dans la socket envoy�e par le serveur
	 * @return un objet
	 */
	public Object readObject()
	{
		Object o = null;
		if(in != null)
		{
			try {
				
				 o = in.readObject(); 
			} catch (ClassNotFoundException | IOException e) {
				try {
					client.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		return o;
	}
	
	/**
	 * Permet d'�crire dans la socket l'objet pass� en param�tre pour le serveur
	 * @param o
	 */
	public void sendObject(Object o)
	{
		if(out != null)
		{
			try {
				out.reset();
				out.writeObject(o);
			} catch (IOException e) {
				try {
					this.client.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Permet de fermer la socket en lecture
	 */
	public void closeObjectInputStream()
	{
		if(in != null)
		{
			try {
				this.in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Permet de fermer la socket en �criture
	 */
	public void closeObjectOutputStream()
	{
		if(out != null)
		{
			try {
				this.out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * M�thode qui est une boucle infinie qui attends les donn�es fournie par le serveur
	 */
	@Override
	public void run() {
		
		Object o = readObject();
		if(o instanceof Integer) // Lors du d�marrage de la socket, le serveur envoie le num�ro de client au client, 
		{
			this.numClient = Integer.parseInt(""+o); // le num�ro est alors fourni par le serveur et je le bind ici
		}
		
		while(!this.exit) // Boucle infinie
		{
			if(System.currentTimeMillis() - startTimer > Serveur.INTERVALLE_REFRESH)
			{
				o = readObject(); // J'attends les donn�es du serveur
				if(o instanceof GameLogique)
				{
					setGameLogique((GameLogique)o);	
				}
				startTimer = System.currentTimeMillis();
			}
		}		
	}
	
	/**
	 * Renvoie le num�ro de client sur le serveur
	 * @return entier
	 */
	public int getNumClient()
	{
		return this.numClient;
	}
	
	/**
	 * Renvoie le temps �coul� depuis le d�marrage de la socket
	 * @return long
	 */
	public long getStartTimer()
	{
		return this.startTimer;
	}

	/**
	 * Renvoie l'instance de gameLogique (g�n�ralement qui � la valeur de celle du serveur)
	 * @return une instance de gamelogique
	 */
	public GameLogique getGameLogique()
	{
		return this.gl;
	}
	
	/**
	 * Permet de modifier dynamiquement la logique de jeu
	 * @param gl
	 */
	public void setGameLogique(GameLogique gl)
	{
		this.gl = gl;
	}
	
	public void setExit(boolean exit)
	{
		this.exit = exit;
	}
	
	public Socket getSocket()
	{
		return this.client;
	}
}
