package Network;
/**
 * Classe qui permet de faire le lien entre le serveur et le client
 * @author Heliex
 * @date 05/04/2017
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ThreadServeur extends Thread {
	
	// Socket qui est envoy� par le serveur
	private Socket socket;
	// OutputStream qui permet d'�crire dans la socket
	private ObjectOutputStream out;
	
	// InputStream qui permet de lire dans la socket
	private ObjectInputStream in;
	
	// Num�ro de client (fourni par le serveur)
	private int numClient;
	
	/**
	 * Constructeur qui prend en param�tre la socket qu'envoie le serveur et le num�ro de client
	 * @param s
	 * @param numClient
	 */
	public ThreadServeur(Socket s,int numClient)
	{
		this.socket = s;
		this.numClient = numClient;
		try {
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		
		sendObject(numClient); // Lorsque la connexion est �tablie, la socket envoie une information au client instantan�ment
		while(true)
		{
			Object o = readObject(); // Je lis ce que le client m'envoie tout les 25 ms;
			Serveur.clientTreatment(o);
		}
	}
	
	/**
	 * Methode qui permet de lire un objet depuis la socket via l'inputstream de la socket
	 * @return Un objet
	 */
	public Object readObject()
	{
		Object o = null;
		if(in != null)
		{
			try {
				o = this.in.readObject();
			} catch (ClassNotFoundException | IOException e) {
				closeObjectInputStream();
				try {
					socket.close();
				} catch (IOException e1) {
					// TODO Bloc catch g�n�r� automatiquement
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		return o;
	}
	
	/**
	 * M�thode qui permet d'envoyer l'objet passer en param�tre dans la socket (�criture)
	 * @param o
	 */
	public void sendObject(Object o)
	{
		if(out != null)
		{
			try {
				this.out.reset();
				this.out.writeObject(o);
			} catch (IOException e) {
				System.out.println("je rentre dans l'IOException ?");
				closeObjectOutputStream();
				try {
					socket.close();
				} catch (IOException e1) {
					// TODO Bloc catch g�n�r� automatiquement
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * M�thode pour fermer l'inputStream de la socket
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
	 * M�thode pour fermer l'outputStream de la socket
	 */
	public void closeObjectOutputStream()
	{
		if(out != null)
		{
			try{
				this.out.close();
			}
			catch(IOException io)
			{
				io.printStackTrace();
			}
		}
	}
	

}
