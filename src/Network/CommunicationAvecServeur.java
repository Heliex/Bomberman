package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
				Object o = in.readObject();
				listeDeMessage.add(o);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void sendObject(Object o)
	{
		try
		{
			out.writeObject(o);
			out.flush();
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
	
	public int getDirection(String commande)
	{
		int direction = 0;
		switch(commande)
		{
		case "UP":
			direction = 0;
		break;
		
		case "LEFT":
			direction = 1;
			break;
		
		case "DOWN":
			direction = 2;
			break;
		case "RIGHT":
			direction = 3;
			break;
		}
		return direction;
	}
}