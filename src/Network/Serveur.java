package Network;
/**
 *  Classe Serveur qui permet de g�rer les connexions entrantes ainsi que le calcul de logique
 *  @author Heliex
 *  @date 05/04/2017
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import Commande.DeplacerJoueur;
import Commande.PoserBombe;
import Commande.StopperDeplacementJoueur;
import Logique.GameLogique;

public class Serveur{
	
	// Variable qui sert a cr�e le serveur de "socket"
	ServerSocket ss;
	
	// Port du serveur
	public final static int NUMPORT = 7777;
	
	// Nombre de clients connect�s au maximum en simultan� sur le serveur
	public final static int NB_MAX_CONNECTED = 4;
	
	// Compteur de client actuellement connect� sur le serveur
	public static int counterConnected = 0;
	
	// Tableau de ThreadServeur qui permet d'envoyer des informations � tous les clients connect�s
	private static ThreadServeur[] threads = new ThreadServeur[NB_MAX_CONNECTED];
	
	// Intervalle de refresh du serveur (Combien de temps �coul� entre un nouvel envoi du serveur aux clients)
	public final static int INTERVALLE_REFRESH = 10;
	
	// Timer de d�part
	private long startTimer;
	
	public static LinkedList<Object> listeCommande = new LinkedList<>();
	
	private static GameLogique gameLogique = new GameLogique();
	
	
	// Constructeur par d�faut du serveur
	public Serveur()
	{
		try { 
			this.ss = new ServerSocket(NUMPORT); // Cr�ation du serverSocket sur le port en variable
			this.startTimer = System.currentTimeMillis();
			while(counterConnected < NB_MAX_CONNECTED) // Tant que j'ai pas tous les clients de co
			{
				System.out.println("LE SERVEUR EST DEMARRE...");
				System.out.println("En attente de connexion d'un client...");
				Socket s = ss.accept(); // J'attends un co
				
				ThreadServeur ts = new ThreadServeur(s,counterConnected); // Je cr�e un thread correspondent
				threads[counterConnected] = ts; // J'ajoute le thread dans mon tableau (pour le broadCast)
				ts.start(); // D�marrage du thread
				counterConnected++; // Compteur incr�ment�
			}
			go(); /* Lorsque tous les clients sont connect�s, je rentre dans une boucle qui
			 		 va envoy� l'�tat du serveur aux clients toutes les 25 ms
			 	  */
			} catch (IOException e) { // Si j'ai un probl�me avec les socket alors je clos le serveur
			e.printStackTrace();
			try {
				ss.close();
			} catch (IOException e1) {
				// TODO Bloc catch g�n�r� automatiquement
				e1.printStackTrace();
			}
		}
	}


	public void go()
	{
		while(true)
		{
			if(System.currentTimeMillis() - startTimer > INTERVALLE_REFRESH) // Refresh Rate de 40 FPS
			{
				startTimer = System.currentTimeMillis();
				
				while(listeCommande.size() > 0) // Je vide la liste des commandes
				{
					Object o = listeCommande.getFirst();
					if(o instanceof DeplacerJoueur) // Deplacement d'un joueur
					{
						DeplacerJoueur dp = (DeplacerJoueur)o;
						gameLogique.deplacerJoueur(dp.getNum(), dp.getDirection());
						gameLogique.dirigerJoueur(dp.getDirection(), dp.getNum());
						gameLogique.getPlayers()[dp.getNum()].setMoving(true);
					}
					else if (o instanceof StopperDeplacementJoueur) // Stopper le d�placement d'un joueur
					{
						StopperDeplacementJoueur sdj = (StopperDeplacementJoueur)o;
						gameLogique.getPlayers()[sdj.getNum()].setMoving(false);
					}
					else if(o instanceof PoserBombe) // Poser une bombe pour le joueur 
					{
						PoserBombe pb = (PoserBombe)o;
						gameLogique.poserBombe(pb.getNum());
					}
					listeCommande.removeFirst();
				}
			}
			broadCast(gameLogique);
		}
	}
	
	// M�thode qui est appel�e par le ThreadServeur et qui est "ThreadSafe"
	public static synchronized void clientTreatment(Object o)
	{
		listeCommande.add(o);
	}
	
	// M�thode Main qui permet de cr�er le serveur
	public static void main(String[] args)
	{
		new Serveur();
	}
	
	// M�thode qui permet de broadCast � tout les clients un objet
	public void broadCast(Object o)
	{
		for(int i = 0 ; i < NB_MAX_CONNECTED; i++)
		{
			if(threads[i] != null)
			{
				threads[i].sendObject(o);
			}
		}
	}
}
