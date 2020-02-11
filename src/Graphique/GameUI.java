 package Graphique;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import Commande.DeplacerJoueur;
import Commande.PoserBombe;
import Commande.StopperDeplacementJoueur;
import Logique.Bombe;
import Logique.Case;
import Logique.Explosion;
import Logique.GameLogique;
import Logique.Player;
import Network.ThreadClient;

public class GameUI extends BasicGame{
	

	private ThreadClient client;
	private GameLogique gameLogique;
	private BombeUI[][] bombeUI;
	private ExplosionUI[][] explosionUI;
	private CaseUI[][] plateauUI;
	public final static int TIME_BETWEEN_FRAME_PLAYER = 200;
	private PlayerUI[] playerUI;
	
	public GameUI(String name)
	{
		super(name);
		this.client = new ThreadClient();
		this.bombeUI = new BombeUI[GameLogique.NB_PLAYERS][Player.NB_BOMBE_MAX];
		this.explosionUI = new ExplosionUI[GameLogique.NB_PLAYERS][Player.NB_BOMBE_MAX];
		this.plateauUI = new CaseUI[GameLogique.NB_CASE_HAUTEUR][GameLogique.NB_CASE_LARGEUR];
		this.gameLogique = new GameLogique();
		this.playerUI = new PlayerUI[GameLogique.NB_PLAYERS];
	}

	@Override
	public void init(GameContainer gc)
			throws SlickException {
		
		for(int i = 0 ; i < GameLogique.NB_CASE_HAUTEUR; i++)
		{
			for(int j = 0 ; j < GameLogique.NB_CASE_LARGEUR; j++)
			{
				plateauUI[i][j] = new CaseUI(gameLogique.getPlateau()[i][j]);
			}
		}
		
		for(int i = 0 ; i < GameLogique.NB_PLAYERS; i++) // J'initialise les UI des joueurs
		{
			playerUI[i] = new PlayerUI(gameLogique.getPlayers()[i],TIME_BETWEEN_FRAME_PLAYER);
		}
		
		for(int i = 0 ; i < GameLogique.NB_PLAYERS; i++) // J'initialise le tableau d'animation de bombe
		{
			for(int j =0 ; j < Player.NB_BOMBE_MAX; j++)
			{
				bombeUI[i][j] = new BombeUI(true, Bombe.TIME_BETWEEN_BOMB_MOVEMENT);
				explosionUI[i][j] = new ExplosionUI(true, Bombe.TIME_BETWEEN_BOMB_MOVEMENT);
			}
		}
		
		client.start();
		gc.setTargetFrameRate(60);
		gc.getInput().enableKeyRepeat();
	}

	@Override
	public void render(GameContainer gc, Graphics g)
			throws SlickException {
		
		if(client.getGameLogique() != null)
		{
			//DrawBoard
			for(int i = 0 ; i < GameLogique.NB_CASE_HAUTEUR; i++)
			{
				for(int j = 0 ; j < GameLogique.NB_CASE_LARGEUR ; j++)
				{
					if(plateauUI[i][j] != null)
					g.drawImage(plateauUI[i][j].getImage(plateauUI[i][j].getCase().getType()),j*Case.TAILLE_CASE, i*Case.TAILLE_CASE);
				}
			}
			//DrawPlayer
			for(int i = 0 ; i < GameLogique.NB_PLAYERS; i++)
			{
				if(client.getGameLogique().getPlayers()[i].isMoving()) // If my player is moving
				{
					playerUI[i].getPlayer().setDirection(client.getGameLogique().getPlayers()[i].getDirection());
					g.drawAnimation(playerUI[i].getAnimation(client.getGameLogique().getPlayers()[i].getDirection()), client.getGameLogique().getPlayers()[i].getX(), client.getGameLogique().getPlayers()[i].getY());
				}
				else // Else i get the local reference to get the position of the current player (but i send a command to server for other players)
				{
					g.drawImage(playerUI[i].getCurrentDirection(),client.getGameLogique().getPlayers()[i].getX(), client.getGameLogique().getPlayers()[i].getY());
				}
			}
			
			// DrawBombe
			for(int i = 0 ; i < GameLogique.NB_PLAYERS; i++ )
			{
				Bombe[] bombes = client.getGameLogique().getPlayers()[i].getListeBombes();
				for(int j = 0 ; j < bombes.length ; j++)
				{
					if(bombes[j] != null)
					{	
						if(bombes[j].isDrawable())
						{
							g.drawAnimation(bombeUI[i][j].getAnimation(), bombes[j].getX(), bombes[j].getY());
						}
						else // draw explosion
						{
							Explosion explosion = bombes[j].getExplosion();
							if(explosion != null && !bombes[j].isDrawable())
							{
								g.drawAnimation(explosionUI[i][j].getMilieuExplosion(),bombes[j].getX(),bombes[j].getY());
							}
						}
						
						
					}
				}
				
				
			}
		}
	}

	@Override
	public void update(GameContainer gc, int delta)
			throws SlickException {
	}
	
	@Override
	public void keyPressed(int key, char c)
	{
			switch(key)
			{
				case Input.KEY_UP:
				case Input.KEY_Z:
					client.sendObject(new DeplacerJoueur(client.getNumClient(),Player.UP));
				break;
				
				case Input.KEY_LEFT:
				case Input.KEY_Q:
					client.sendObject(new DeplacerJoueur(client.getNumClient(),Player.LEFT));
					break;
				
				case Input.KEY_DOWN:
				case Input.KEY_S:
					client.sendObject(new DeplacerJoueur(client.getNumClient(),Player.DOWN));
					break;
					
				case Input.KEY_RIGHT:
				case Input.KEY_D:
					client.sendObject(new DeplacerJoueur(client.getNumClient(),Player.RIGHT));
					break;
					
				case Input.KEY_B:
					client.sendObject(new PoserBombe(client.getNumClient()));
					break;
			}
			
	}
	
	@Override
	public void keyReleased(int key, char c)
	{
		client.sendObject(new StopperDeplacementJoueur(client.getNumClient()));
	}
	
	public GameLogique getGameLogique()
	{
		return this.gameLogique;
	}
	
	/**
	 * Méthode Main pour lancer l'UI.
	 * @param args
	 */
	public static void main(String[] args)
	{
		try {
			AppGameContainer app = new AppGameContainer(new GameUI("Bomberman Multiplayer"));
			app.setDisplayMode(GameLogique.NB_CASE_LARGEUR * Case.TAILLE_CASE, GameLogique.NB_CASE_HAUTEUR * Case.TAILLE_CASE, false);
			app.setShowFPS(true);
			app.setAlwaysRender(true);
			app.start();
		} catch (SlickException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
	}
	
	
}
