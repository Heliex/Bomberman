package Network;

import java.io.IOException;
import java.net.ServerSocket;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import MainGame.Game;
import MainGame.Main;

public class Server{

	public static Game game;
	private static AppGameContainer app;
	public final static int NB_MAX = 4;
	private ServerSocket server ;
	
	public Server(int port) throws IOException
	{
		this.server = new ServerSocket(port);
	}
	
	
	public static void main(String[] args) throws SlickException {
		// TODO Auto-generated method stub
		Main main = new Main("Serveur Bomberman");
		app = new AppGameContainer(main);
		main.initStatesList(app);
		main.enterState(1);
		
		app.setDisplayMode(Main.WIDTH,Main.HEIGHT, false);
		app.setTargetFrameRate(60);
		app.setShowFPS(false);
		app.start();		
	}
}
