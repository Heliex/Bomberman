package Network;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import Graphique.Case;
import Graphique.Player;
import MainGame.Game;
import MainGame.Main;

public class Client extends BasicGameState{

	private Socket socket;
	private ThreadClient threadClient;
	private int numClient;
	private Game game;
	private Image wall,ground,indestructible_wall,groundGrass, house, wood, groundGrassTexas,groundTexas,hud,timer;
	private SpriteSheet sheet, bombSheet,explosionSheet,bonusSheet,deadSheet,groundSheet,numbers;
	private Image[] compteur = new Image[10];
	private Sound bonusSound,bombExplode, background;
	private Player[] players = new Player[Server.NB_CLIENT];
	private List<Animation[]> listeAnimations = new ArrayList<Animation[]>();
	private int numberClientConnected = 0;
	private Case[][] plateau = new Case[Game.NB_CASE_HAUTEUR][Game.NB_CASE_LARGEUR];
	
	public Client(String host, int port)
	{
		try {
			System.out.println("Connexion au serveur....");
			this.socket = new Socket(host,port);
			System.out.println("Connexion effectuée avec succès....");
			System.out.println("Création du Thread Client");
			threadClient = new ThreadClient(socket,this);
			Thread t = new Thread(threadClient);
			System.out.println("Création du Thread effectué");
			t.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(ConnectException c)
		{
			System.out.println("Impossible de se connecter au serveur - il n'est peut être pas démarré");
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void keyPressed(int key,char c)
	{
		switch(key)
		{
		case Input.KEY_UP:
		case Input.KEY_Z:
			players[numClient].setDirection(Game.UP);
			players[numClient].setMoving(true);
			threadClient.sendObject(numClient+":"+"TRUE");
			break;
		case Input.KEY_LEFT:
		case Input.KEY_Q:
			players[numClient].setDirection(Game.LEFT);
			players[numClient].setMoving(true);
			threadClient.sendObject(numClient+":"+"TRUE");
			break;
		case Input.KEY_DOWN:
		case Input.KEY_S:
			players[numClient].setDirection(Game.DOWN);
			players[numClient].setMoving(true);
			threadClient.sendObject(numClient+":"+"TRUE");
			break;
		case Input.KEY_RIGHT:
		case Input.KEY_D:
			players[numClient].setDirection(Game.RIGHT);
			players[numClient].setMoving(true);
			threadClient.sendObject(numClient+":"+"TRUE");
			break;
		case Input.KEY_B:
			threadClient.sendObject(numClient+":"+"BOMB");
			break;
		}
	}
	
	@Override
	public void keyReleased(int key,char c)
	{
		players[numClient].setMoving(false);
		threadClient.sendObject(numClient+":"+"FALSE");
	}


	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		sheet = new SpriteSheet("images/Deplacements.png",Game.LARGEUR_SPRITE,Game.HAUTEUR_SPRITE);
		bombSheet = new SpriteSheet("images/Bombe.png",Game.TAILLE_BOMB,Game.TAILLE_BOMB);
		explosionSheet = new SpriteSheet("images/Explosions.png",Game.TAILLE_BOMB,Game.TAILLE_BOMB);
		deadSheet = new SpriteSheet("images/Mort.png",Game.TAILLE_MORT_LARGEUR,Game.TAILLE_MORT_HAUTEUR);
		groundSheet = new SpriteSheet("images/Grounds.png",Game.TAILLE_CASE,Game.TAILLE_CASE);
		bonusSheet = new SpriteSheet("images/Bonus.png",Game.TAILLE_BOMB,Game.TAILLE_BOMB);
		numbers = new SpriteSheet("images/Number.png",Game.LARGEUR_NUMBER,Game.HAUTEUR_NUMBER);
		hud = new Image("images/HUD.png");
		// Initialize Images
		groundGrass = groundSheet.getSprite(0,0);
		ground = groundSheet.getSprite(1,0);
		indestructible_wall = groundSheet.getSprite(2,0);
		wall = groundSheet.getSprite(3,0);
		house = groundSheet.getSprite(0,1);
		wood = groundSheet.getSprite(1, 1);
		groundGrassTexas = groundSheet.getSprite(2, 1);
		groundTexas = groundSheet.getSprite(3, 1);
		Game.setCOEFF_DEPLACEMENT(0.02f);
		hud = hud.getSubImage(0, 0, Game.TAILLE_CASE*4 - Game.TAILLE_BOMB, Game.TAILLE_CASE);
		timer = hud.getSubImage(4*Game.TAILLE_CASE - Game.TAILLE_BOMB, 0,Game.TAILLE_CASE, Game.TAILLE_CASE);
		compteur[0] = numbers.getSprite(0, 0);
		compteur[1] = numbers.getSprite(1, 0);
		compteur[2] = numbers.getSprite(2, 0);
		compteur[3] = numbers.getSprite(3, 0);
		compteur[4] = numbers.getSprite(4, 0);
		compteur[5] = numbers.getSprite(5, 0);
		compteur[6] = numbers.getSprite(6, 0);
		compteur[7] = numbers.getSprite(7, 0);
		compteur[8] = numbers.getSprite(8, 0);
		compteur[9] = numbers.getSprite(9, 0);
		bonusSound = new Sound("sons/ramasserBonus.wav");
		bombExplode = new Sound("sons/bombExplode.wav");
		background = new Sound("sons/builtToFall.wav");
		players[0] = new Player(sheet,0,0,Game.TAILLE_CASE,deadSheet);
		players[1] = new Player(sheet,1,(Game.NB_CASE_LARGEUR - 1) * Game.TAILLE_CASE,Game.TAILLE_CASE,deadSheet);
		players[2] = new Player(sheet,2,0,(Game.NB_CASE_HAUTEUR -1) * Game.TAILLE_CASE,deadSheet);
		players[3] = new Player(sheet,3,(Game.NB_CASE_LARGEUR - 1) * Game.TAILLE_CASE,(Game.NB_CASE_HAUTEUR - 1) * Game.TAILLE_CASE,deadSheet);
		players[0].setDrawable(true);
		players[1].setDrawable(true);
		players[2].setDrawable(true);
		players[3].setDrawable(true);
		for(int i = 0 ; i < Server.NB_CLIENT; i++)
		{
			listeAnimations.add(players[i].getAllAnimation());
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame statedGame, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		if(game != null)
		{
			System.out.println(plateau[1][0]);
			game.render(gc,statedGame,g);
		}
		else
		{
			g.drawString("En attente de connexion de tous les joueurs !!", Main.WIDTH /2 - 200, Main.HEIGHT / 2);
			g.drawString("JOUEUR CONNECTES : " + numberClientConnected+ "/" + Server.NB_CLIENT, Main.WIDTH /2 - 200, Main.HEIGHT / 2 + 100);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame stateGame, int delta) throws SlickException {
		// TODO Auto-generated method stub
		threadClient.sendObject(delta);
		if(game != null)
		{
			for(int i = 0 ; i < Server.NB_CLIENT ; i++)
			{
				if(players[i].isDrawable())
				{
					if(players[i].isMoving())
					{
						switch(players[i].getDirection())
						{
						case Game.UP :
							threadClient.sendObject(i+":UP");
						break;
						case Game.LEFT : 
							threadClient.sendObject(i+":LEFT");
						break;
						case Game.DOWN : 
							threadClient.sendObject(i+":DOWN");
						break;
						case Game.RIGHT : 
							threadClient.sendObject(i+":RIGHT");
						break;
						}
						
					}
				}
				threadClient.sendObject(plateau);
			}
			game.update(gc, stateGame, delta);
		}
	}


	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 2;
	}
	
	public void setNumClient(int num)
	{
		this.numClient = num;
	}
	
	public int getNumClient()
	{
		return this.numClient;
	}
	
	public void setGame(Game gameReceived)
	{
		game = gameReceived;
	}
	
	public Game getGame()
	{
		return this.game;
	}
	
	public int getNumberClientConnected()
	{
		return this.numberClientConnected;
	}
	
	public void setNumberClientConnected(int nb)
	{
		this.numberClientConnected = nb;
	}
	
	public void setPlateau(Case[][] board)
	{
		this.plateau = board;
	}
}
