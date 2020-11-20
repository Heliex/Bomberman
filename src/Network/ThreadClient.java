package Network;
/**
 * Classe qui permet de créer la socket côté client pour échanger avec le serveur
 * @author Heliex
 * @date   05/04/2017
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Logique.GameLogique;

public class ThreadClient extends Thread{
	
	// Numéro de client fourni par le serveur
	private int numClient;
	
	// Objet GameLogique qui est conservé pour permettre a l'UI de comparé son instance local avec celle du serveur
	private GameLogique gl = null;
	// Adresse IP de connexion au serveur.
	public final static String IP = "127.0.0.1";
	
	// Socket créer par l'interfaceGraphique de l'UI.
	private Socket client;
	
	// ObjectInputStream qui permet de lire le contenu de la socket (envoyé par le serveur)
	private ObjectInputStream in;
	
	// ObjectOutputStream qui permet d'écrire dans la socket (écrit côté client)
	private ObjectOutputStream out;
	
	// Timer pour savoir quand envoyé le gameLogique
	private long startTimer;
	
	private boolean exit;
	
	/**
	 * Constructeur public du ThreadClient
	 * Crée la socket pour faire des échanges avec le serveur
	 * Déclare des attributs qui permettent d'écrire/lire dans la socket
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
	 * Methode qui permet de lire les données dans la socket envoyée par le serveur
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
	 * Permet d'écrire dans la socket l'objet passé en paramétre pour le serveur
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
	 * Permet de fermer la socket en écriture
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
	 * Méthode qui est une boucle infinie qui attends les données fournie par le serveur
	 */
	@Override
	public void run() {
		
		Object o = readObject();
		if(o instanceof Integer) // Lors du démarrage de la socket, le serveur envoie le numéro de client au client, 
		{
			this.numClient = Integer.parseInt(""+o); // le numéro est alors fourni par le serveur et je le bind ici
		}
		
		while(!this.exit) // Boucle infinie
		{
			if(System.currentTimeMillis() - startTimer > Serveur.INTERVALLE_REFRESH)
			{
				o = readObject(); // J'attends les données du serveur
				if(o instanceof GameLogique)
				{
					setGameLogique((GameLogique)o);	
				}
				startTimer = System.currentTimeMillis();
			}
		}		
	}
	
	/**
	 * Renvoie le numéro de client sur le serveur
	 * @return entier
	 */
	public int getNumClient()
	{
		return this.numClient;
	}
	
	/**
	 * Renvoie le temps écoulé depuis le démarrage de la socket
	 * @return long
	 */
	public long getStartTimer()
	{
		return this.startTimer;
	}

	/**
	 * Renvoie l'instance de gameLogique (généralement qui à la valeur de celle du serveur)
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
