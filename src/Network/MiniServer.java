package Network;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import MainGame.Game;

public class MiniServer extends Thread{
	
	private Socket socket = null;
	private Game game;
	private ObjectOutputStream os;
	private ObjectInputStream in;
	
	public MiniServer(Socket socket,Game game) throws IOException
	{
		super("Miniserveur");
		this.socket = socket;
		this.game = game;
		this.os = new ObjectOutputStream(this.socket.getOutputStream());
		this.in = new ObjectInputStream(this.socket.getInputStream());
	}
	
	public void run()
	{
		while(true)
		{
			// Ecrire le jeu vers le client.
			try {
				os.writeObject(game);
				os.flush();
				game = (Game)in.readObject();
				System.out.println(game.getPlayers()[0]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
}
