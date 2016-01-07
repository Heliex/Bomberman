package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import Graphique.Bomb;
import Graphique.Case;
import Graphique.Explosion;
import MainGame.Game;

public class ThreadClient implements Runnable{

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Client client;
	private float delta;

	public ThreadClient(Socket socket,Client client)
	{
		this.socket = socket;
		this.client = client;
		try {
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while(true)
		{
			// Lecture de donnees venant du server
			try {
				Object o = in.readObject();
				if(o instanceof Integer)
				{
					client.setNumClient((int)o);
				}
				else if(o instanceof Game)
				{
					client.setGame((Game)o);
				}
				else if(o instanceof Float)
				{
					delta = (float)o;
				}
				else if(o instanceof String)
				{
					String s = (String)o;
					String[] commandes = s.split(":");
					if(commandes.length == 2)
					{
						int numClient = Integer.parseInt(commandes[0]);
						String commande = commandes[1];
						switch(commande)
						{
						case "UP":
							if(client.getGame().canMove(client.getGame().getPlayers()[numClient], Game.UP, (int)delta))
							{
								System.out.println("Le joueur : " + numClient + " peut se déplacer vers le haut ");
								client.getGame().getPlayers()[numClient].setY(client.getGame().getPlayers()[numClient].getY() - (delta * Game.COEFF_DEPLACEMENT));
								client.getGame().getPlayers()[numClient].setDirection(Game.UP);
								
							}
							break;
						case "LEFT":
							if(client.getGame().canMove(client.getGame().getPlayers()[numClient], Game.LEFT, (int)delta))
							{
								client.getGame().getPlayers()[numClient].setX(client.getGame().getPlayers()[numClient].getX() - (delta * Game.COEFF_DEPLACEMENT));
								client.getGame().getPlayers()[numClient].setDirection(Game.LEFT);

							}
							break;
						case "DOWN":
							if(client.getGame().canMove(client.getGame().getPlayers()[numClient], Game.DOWN, (int)delta))
							{
								client.getGame().getPlayers()[numClient].setY(client.getGame().getPlayers()[numClient].getY() + (delta * Game.COEFF_DEPLACEMENT));
								client.getGame().getPlayers()[numClient].setDirection(Game.DOWN);
							}
							break;
						case "RIGHT":
							if(client.getGame().canMove(client.getGame().getPlayers()[numClient], Game.RIGHT, (int)delta))
							{
								client.getGame().getPlayers()[numClient].setX(client.getGame().getPlayers()[numClient].getX() + (delta * Game.COEFF_DEPLACEMENT));
								client.getGame().getPlayers()[numClient].setDirection(Game.RIGHT);
							}
							break;
						case "BOMB":
							if(client.getGame().getPlayers()[numClient].getBombe().size() < Game.NB_BOMB_AVAILABLE)
							{
								new Thread(new Runnable(){
									public void run()
									{
										Case c = client.getGame().getCaseFromCoord(client.getGame().getPlayers()[numClient].getX()+Game.getTailleCase()/2, client.getGame().getPlayers()[numClient].getY()+Game.getTailleCase() - (Game.getTailleCase()/2));
										if(c.getType() != "WALL" && c.getType() != "INDESTRUCTIBLE" && !c.hasBombe())
										{
											client.getGame().getBoard()[c.getY()][c.getX()].setHasBombe(true);
											Bomb b = new Bomb(client.getGame().getBombSheet(),c.getRealX()+Game.getTailleBomb()/2,c.getRealY()+Game.getTailleBomb()/2,new Explosion(c.getRealX()+Game.getTailleBomb()/2,c.getRealY()+Game.getTailleBomb()/2,client.getGame().getExplosionSheet()));
											client.getGame().getPlayers()[numClient].getBombe().add(b);
										}
									}
								}).start();
							}
						break;
						case "BONUS":
							break;
						}
					}
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
				this.socket = null;
				System.out.println("Exception de socket - Fermeture du flux");
			}
			catch (IOException e) {
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
		}catch(IOException io)
		{
			io.printStackTrace();
		}

	}

	public Socket getSocket()
	{
		return this.socket;
	}

	public void setSocket(Socket socket)
	{
		this.socket = socket;
	}

	public ObjectInputStream getIn()
	{
		return this.in;
	}

	public void setIn(ObjectInputStream in)
	{
		this.in = in;
	}

	public ObjectOutputStream getOut()
	{
		return this.out;
	}

	public void setOut(ObjectOutputStream out)
	{
		this.out = out;
	}
}
