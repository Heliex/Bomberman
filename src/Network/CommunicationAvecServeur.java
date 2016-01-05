package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CommunicationAvecServeur implements Runnable{
	private Socket client;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private int delta = 0 ;
	
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
				if(o instanceof String)
				{
					System.out.println("Une Chaine de caract�re � �t� re�ue");
					String recu = (String)o;
					String[] tab =  recu.split(":");
					int numPlayer = Integer.parseInt(tab[0]);
					String commande = tab[1];
					System.out.println("Num�ro du joueur : " + numPlayer);
					System.out.println("Commande recu : " + commande);
					if(Server.game != null)
					{
						switch(commande)
						{
							case "UP":
								if(Server.game.canMove(Server.game.getPlayers()[numPlayer], getDirection("UP"),numPlayer))
								{
									System.out.println("Le joueur peux monter");
									Server.game.getPlayers()[numPlayer].setY(Server.game.getPlayers()[numPlayer].getY() - delta);
								}
							break;
							
							case "LEFT":
								if(Server.game.canMove(Server.game.getPlayers()[numPlayer], getDirection("LEFT"),numPlayer))
								{
									Server.game.getPlayers()[numPlayer].setX(Server.game.getPlayers()[numPlayer].getY() - delta);
								}
							break;
							
							case "DOWN":
								if(Server.game.canMove(Server.game.getPlayers()[numPlayer], getDirection("DOWN"),numPlayer))
								{
									Server.game.getPlayers()[numPlayer].setY(Server.game.getPlayers()[numPlayer].getY() + delta);
								}
							break;
							
							case "RIGHT":
								if(Server.game.canMove(Server.game.getPlayers()[numPlayer], getDirection("RIGHT"),numPlayer))
								{
									Server.game.getPlayers()[numPlayer].setX(Server.game.getPlayers()[numPlayer].getY() + delta);
								}
							break;
						}
					}
				}
				else if(o instanceof Integer)
				{
					delta = (int)o;
				}
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