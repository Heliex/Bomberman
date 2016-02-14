package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;


public class CommunicationAvecServeur implements Runnable{
	private Socket client;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	public static ConcurrentLinkedQueue<Object> listeDeMessage = new ConcurrentLinkedQueue<Object>(); 
	
	public CommunicationAvecServeur(Socket socket) {
		this.client = socket;
		try {
			this.out = new ObjectOutputStream(client.getOutputStream());
			this.in = new ObjectInputStream(client.getInputStream());
			sendObject(Server.NB_CLIENT_CONNECTED);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(true)
		{
			// Attente d'envoi d'objet de la part d'un client
			try {
				if( in != null)
				{
					Object o = in.readObject();
					listeDeMessage.add(o);
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch(SocketException s)
			{
				this.in = null;
				this.out = null;
				this.client = null;
				Server.NB_CLIENT_CONNECTED--;
				Server.broadCast(Server.NB_CLIENT_CONNECTED+":NB_CLIENT");
				
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("JE passe dans L'I/O Exception de la communication avec le Serveur");
				e.printStackTrace();
			}
		}
	}
	
	public void sendObject(Object o)
	{
		try
		{
			if(out != null)
			{
				out.writeObject(o);
				out.flush();
			}
		}
		catch(IOException io)
		{
			io.printStackTrace();
		}
		
	}
	
	public ObjectOutputStream getOut()
	{
		return this.out;
	}
	
	public void setOut(ObjectOutputStream out)
	{
		this.out = out;
	}
	
	public ObjectInputStream getIn()
	{
		return this.in;
	}
	
	public void setIn(ObjectInputStream in)
	{
		this.in = in;
	}
}