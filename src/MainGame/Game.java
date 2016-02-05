package MainGame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import Graphique.Bomb;
import Graphique.Bonus;
import Graphique.Case;
import Graphique.Explosion;
import Graphique.Player;
import Network.Server;


public class Game extends BasicGameState implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4628054333033856137L;
	// All CONSTANT
	public final static int UP = 0, LEFT = 1, DOWN = 2, RIGHT = 3, LEVEL_AT_START=1,LIFE_AT_START = 5;
	public final static int LARGEUR_SPRITE = 18, HAUTEUR_SPRITE = 32,LARGEUR_NUMBER=8,HAUTEUR_NUMBER=14;
	public final static int TAILLE_BOMB = 16;
	public final static int TAILLE_CASE = 32;
	public final static int NB_BOMB_AT_START = 1,NB_BOMB_MAXI = 5;
	public final static int NB_CASE_HAUTEUR = 19, NB_CASE_LARGEUR = 25,TIMEGAME=121000;
	public final static int OFFSET_VERTICAL_Y_LEFTORRIGHT = 29, OFFSET_HORIZONTAL_X_LEFTORRIGHT = 16;
	public final static int TIMER=1000,TIME_TO_EXPLODE = 4000, TIME_TO_BONUS_APPEAR = 10000, TIME_BEFORE_DISSAPEAR = 30000, TIME_BETWEEN_BOMB_MOVEMENT = 10;
	public final static int NB_BONUS = 14,TAILLE_MORT_LARGEUR = 17, TAILLE_MORT_HAUTEUR = 24,TAILLE_EXPLODE_MIN = 2;
	public final static String BOMBADD = "Bombe Supplémentaire",SPEEDUP="Vitesse supérieur",MOVEBOMB="Bombe déplacable";
	public final static String BOXE = "Gant de boxe",EXPLODEMORE="Explosion plus longue", DEAD="Bombe plus efficiente",EXPLODELESS="Explosion diminuée";
	public final static String BOMBLESS = "Bombe en moins", SPEEDLESS = "Vitesse inférieure", STOPBOMB = "Bombe non déplaçable";
	public final static String UNBOXE = "Fin du bonus de boxe", LOWDMG = "Bombe moins efficiente", EXPLODELESSEFFICIENT = "Explosion efficiente";
	public final static String LEVEL = "niveau";
	public final static String WALL = "WALL",GROUND= "GROUND", GRASSGROUND = "GRASSGROUND",INDESTRUCTIBLE ="INDESTRUCTIBLE",
			HOUSE = "HOUSE", WOOD = "WOOD", GROUNDGRASSTEXAS="GROUNDGRASSTEXAS", GROUNDTEXAS="GROUNDTEXAS", EMPTY = "EMPTY" ;
	public final static float COEFF_MAX = 0.25f,EXPLOSION_MAX = 4, COEFF_MIN = 0.10f;
	// All static but modifiable variables
	public static int NB_BOMB_AVAILABLE= 1,NB_BOMB_ON_BOARD = 0,LEVEL_START=1,LIFE_AVAILABLE=5;
	public static int NB_OBJECTIVE = 3;
	public static float COEFF_DEPLACEMENT = 0.10f;
	public static int TAILLE_EXPLOSION = 2;
	public static int CURRENT_TIME,s,min;
	
	// Other variables
	private boolean isMoving = false,isGameOver = false, isNewtorkGame;
	private boolean isLevelFinished;
	private int direction = 0;
	private Player[] players = new Player[Server.NB_CLIENT] ;
	private Case[][] plateau;
	transient private Image wall,ground,indestructible_wall,groundGrass, house, wood, groundGrassTexas,groundTexas,hud,timer;
	transient private SpriteSheet sheet, bombSheet,explosionSheet,bonusSheet,deadSheet,groundSheet,numbers;
	transient private Image[] compteur = new Image[10];
	private LinkedList<Bonus> bonus ;
	private static long tempsExecution = 0,tempsAuLancement = 0,timerStart=0;
	private Random rand ;
	transient private Sound bonusSound,bombExplode, background;
	private int numPlayer = 0;
	
	public Game()
	{
		isLevelFinished = false;
		isGameOver = false;
		// Initialize board
		plateau = new Case[NB_CASE_HAUTEUR][NB_CASE_LARGEUR];
		bonus = new LinkedList<Bonus>();
		// Initialize level
		File level = new File("niveaux/" + LEVEL + LEVEL_START+ ".txt");
		initLevel(level);
		rand = new Random();
		// Initialize timer
		timerStart = Bomb.getTime();
		
		NB_BOMB_AVAILABLE = NB_BOMB_AT_START;
		COEFF_DEPLACEMENT = COEFF_MIN;
		TAILLE_EXPLOSION = TAILLE_EXPLODE_MIN;
		CURRENT_TIME = TIMEGAME;
		min = ((TIMEGAME/TIMER) % 3600) / 60;
		s = (TIMEGAME/TIMER) % 60 ;
	}
	
	public Game(boolean network)
	{
		isLevelFinished = false;
		isGameOver = false;
		// Initialize board
		plateau = new Case[NB_CASE_HAUTEUR][NB_CASE_LARGEUR];
		bonus = new LinkedList<Bonus>();
		// Initialize level
		File level = new File("niveaux/" + LEVEL + LEVEL_START+ ".txt");
		initLevel(level);
		rand = new Random();
		// Initialize timer
		timerStart = Bomb.getTime();
		
		NB_BOMB_AVAILABLE = NB_BOMB_AT_START;
		COEFF_DEPLACEMENT = COEFF_MIN;
		TAILLE_EXPLOSION = TAILLE_EXPLODE_MIN;
		CURRENT_TIME = TIMEGAME;
		min = ((TIMEGAME/TIMER) % 3600) / 60;
		s = (TIMEGAME/TIMER) % 60 ;
		isNewtorkGame = network;
	}
	
	// This is method is called when windows is opening
	@Override
	public void init(GameContainer gc,StateBasedGame game) throws SlickException
	{
		min = ((TIMEGAME/TIMER) % 3600) / 60;
		s = (TIMEGAME/TIMER) % 60 ;
		// Initialize SpriteSheet
		sheet = new SpriteSheet("images/Deplacements.png",LARGEUR_SPRITE,HAUTEUR_SPRITE);
		bombSheet = new SpriteSheet("images/Bombe.png",TAILLE_BOMB,TAILLE_BOMB);
		explosionSheet = new SpriteSheet("images/Explosions.png",TAILLE_BOMB,TAILLE_BOMB);
		deadSheet = new SpriteSheet("images/Mort.png",TAILLE_MORT_LARGEUR,TAILLE_MORT_HAUTEUR);
		groundSheet = new SpriteSheet("images/Grounds.png",TAILLE_CASE,TAILLE_CASE);
		bonusSheet = new SpriteSheet("images/Bonus.png",TAILLE_BOMB,TAILLE_BOMB);
		numbers = new SpriteSheet("images/Number.png",LARGEUR_NUMBER,HAUTEUR_NUMBER);
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
		
		hud = hud.getSubImage(0, 0, TAILLE_CASE*4 - TAILLE_BOMB, TAILLE_CASE);
		timer = hud.getSubImage(4*TAILLE_CASE - TAILLE_BOMB, 0,TAILLE_CASE, TAILLE_CASE);
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
		tempsAuLancement = Bomb.getTime();
		
		// Initialize Player
		players[0] = new Player(sheet,0,0,TAILLE_CASE,deadSheet);
		players[1] = new Player(sheet,1,(NB_CASE_LARGEUR - 1) * TAILLE_CASE,TAILLE_CASE,deadSheet);
		players[2] = new Player(sheet,2,0,(NB_CASE_HAUTEUR -1) * TAILLE_CASE,deadSheet);
		players[3] = new Player(sheet,3,(NB_CASE_LARGEUR - 1) * TAILLE_CASE,(NB_CASE_HAUTEUR - 1) * TAILLE_CASE,deadSheet);
		if(!isNewtorkGame)
			players[0].setDrawable(true);
	
		
		// Initialize song
		bonusSound = new Sound("sons/ramasserBonus.wav");
		bombExplode = new Sound("sons/bombExplode.wav");
		background = new Sound("sons/builtToFall.wav");
	}
	
	// This method is called on some interval
	@Override
	public void render(GameContainer gc,StateBasedGame game, Graphics g) throws SlickException // Render
	{
			if(background != null && !isNewtorkGame)
			{
				if(!background.playing())
				{
					background.play();
				}
			}
			g.setBackground(new Color(255,255,255,.5f));
			// Draw Board
			drawBoard(g);
			drawPlayers(g,gc,game);
			// Draw Hud and timer
			if(hud != null && compteur[LIFE_AVAILABLE] != null && timer != null && compteur[min] != null)
			{
				g.drawImage(hud,0,0);
				g.drawImage(compteur[LIFE_AVAILABLE], TAILLE_BOMB,9);
				g.drawImage(timer,(NB_CASE_LARGEUR/2 - 1 ) * TAILLE_CASE,0);
				g.drawImage(compteur[min],(NB_CASE_LARGEUR/2 - 1 ) * TAILLE_CASE + TAILLE_BOMB,9);
				String sec = String.valueOf(s);
				int sec1 = Character.getNumericValue(sec.charAt(0));
				int sec2 = 0;
				if(sec.length() > 1)
				{
					sec2 = Character.getNumericValue(sec.charAt(1));
					g.drawImage(compteur[sec1],(NB_CASE_LARGEUR/2 -1) * TAILLE_CASE + LARGEUR_NUMBER*2 + TAILLE_BOMB,9);
					g.drawImage(compteur[sec2],(NB_CASE_LARGEUR/2 -1) * TAILLE_CASE + LARGEUR_NUMBER*3 + TAILLE_BOMB,9);
				}
				else
				{
					g.drawImage(compteur[0],(NB_CASE_LARGEUR/2 -1) * TAILLE_CASE + LARGEUR_NUMBER*2 + TAILLE_BOMB,9);
					g.drawImage(compteur[sec1],(NB_CASE_LARGEUR/2 -1) * TAILLE_CASE + LARGEUR_NUMBER*3 + TAILLE_BOMB,9);
				} 
			}
			if(isLevelFinished)
			{
				LEVEL_START++;
				init(gc,game);
			}
			
			// Draw count on timer
	}
	
	// Game logic
	@Override
	public void update(GameContainer gc,StateBasedGame game, int delta) throws SlickException
	{
		if(!isNewtorkGame)
		{
			if(tempsExecution - timerStart > TIMER) // Modify count here
			{
				CURRENT_TIME -= TIMER;
				s = (CURRENT_TIME/TIMER) % 60;
				min = ((CURRENT_TIME/TIMER) % 3600 ) / 60;
				timerStart = Bomb.getTime();
			}
			if(s == 0 && min == 0)
			{
				isGameOver= true;
				LIFE_AVAILABLE--;
				init(gc,game);
				
			}
				if(players[0] != null)
				{
					if(players[0].isInExplosion() && players[0].isDrawable())
					{
						players[0].setIsDeadDrawable(true);
					}
					// Check if you can move before move
					if(canMove(players[0],players[0].getDirection(),delta) && players[0].isDrawable())
					{
						if(players[0].isMoving())
						{
							switch(players[0].getDirection())
							{
							case UP :
								players[0].setY(players[0].getY() - delta * COEFF_DEPLACEMENT); 	
							break;
							case LEFT : 
								players[0].setX(players[0].getX() - delta * COEFF_DEPLACEMENT); 
							break;
							case DOWN : 
								players[0].setY(players[0].getY() + delta * COEFF_DEPLACEMENT); 
							break;
							case RIGHT : 
								players[0].setX(players[0].getX() + delta * COEFF_DEPLACEMENT); 
							break;
							}
						}
					}
				}
			
			if(tempsExecution - tempsAuLancement > TIME_TO_BONUS_APPEAR)
			{
				new Thread(new Runnable(){
					public void run()
					{
						int index = rand.nextInt(NB_BONUS);
						switch(index)
						{
						case 0:
							bonus.add(new Bonus(BOMBADD,bonusSheet.getSprite(0, 0)));
							break;
						case 1:
							bonus.add(new Bonus(SPEEDUP,bonusSheet.getSprite(1,0)));
							break;
						case 2:
							bonus.add(new Bonus(MOVEBOMB,bonusSheet.getSprite(2, 0)));
							break;
						case 3:
							bonus.add(new Bonus(BOXE,bonusSheet.getSprite(3,0)));
							break;
						case 4:
						case 13:
							bonus.add(new Bonus(EXPLODEMORE,bonusSheet.getSprite(4,0)));
							break;
						case 5:
							bonus.add(new Bonus(DEAD,bonusSheet.getSprite(5, 0)));
							break;
						case 6:
							bonus.add(new Bonus(EXPLODELESS,bonusSheet.getSprite(6, 0)));
							break;
						case 7:
							bonus.add(new Bonus(BOMBLESS,bonusSheet.getSprite(0, 1)));
							break;
						case 8:
							bonus.add(new Bonus(SPEEDLESS,bonusSheet.getSprite(1, 1)));
							break;
						case 9:
							bonus.add(new Bonus(STOPBOMB,bonusSheet.getSprite(2, 1)));
							break;
						case 10:
							bonus.add(new Bonus(UNBOXE,bonusSheet.getSprite(3, 1)));
							break;
						case 11:
							bonus.add(new Bonus(EXPLODELESSEFFICIENT,bonusSheet.getSprite(4, 1)));
							break;
						case 12:
							bonus.add(new Bonus(LOWDMG,bonusSheet.getSprite(5, 1)));
							break;
						default:
							break;
						}
						int x = rand.nextInt(NB_CASE_LARGEUR);
						int y = rand.nextInt(NB_CASE_HAUTEUR);
						Case c = plateau[y][x];
						while(c.getType() == "WALL" || c.getType() == "INDESTRUCTIBLE" || c.getY() == 0)
						{
							x = rand.nextInt(NB_CASE_LARGEUR);
							y = rand.nextInt(NB_CASE_HAUTEUR);
							c = plateau[y][x];
						}
						plateau[y][x].setHasbonus(true);
						if(bonus.size() > 0)
						{
							bonus.getLast().setDrawable(true);
							bonus.getLast().setX(c.getX());
							bonus.getLast().setY(c.getY());
						}
						
						
						rand = new Random();
					}
				}).start();
				tempsAuLancement = Bomb.getTime();
			}	
		}
	}
	
	@Override
	public String toString() {
		return "Game [isMoving=" + isMoving + ", isGameOver=" + isGameOver
				+ ", isNewtorkGame=" + isNewtorkGame + ", isLevelFinished="
				+ isLevelFinished + ", direction=" + direction + ", players="
				+ Arrays.toString(players) + ", plateau="
				+ Arrays.toString(plateau) + ", wall=" + wall + ", ground="
				+ ground + ", indestructible_wall=" + indestructible_wall
				+ ", groundGrass=" + groundGrass + ", house=" + house
				+ ", wood=" + wood + ", groundGrassTexas=" + groundGrassTexas
				+ ", groundTexas=" + groundTexas + ", hud=" + hud + ", timer="
				+ timer + ", sheet=" + sheet + ", bombSheet=" + bombSheet
				+ ", explosionSheet=" + explosionSheet + ", bonusSheet="
				+ bonusSheet + ", deadSheet=" + deadSheet + ", groundSheet="
				+ groundSheet + ", numbers=" + numbers + ", compteur="
				+ Arrays.toString(compteur) + ", bonus=" + bonus + ", rand="
				+ rand + ", numPlayer=" + numPlayer + "]";
	}

	public void drawPlayers(Graphics g,GameContainer gc,StateBasedGame game)
	{
		for(int i = 0 ; i < Server.NB_CLIENT ; i++)
		{
			if(players[i] != null)
			{
				if(players[i].isDrawable())
				{
					// Draw All Bombe And Explosion
					drawArray(players[i].getBombe(),g);
					drawExplosion(players[i].getBombe(),g);
					drawBonus(bonus,g);
					
					// Check if bomberman walk on bonus
					checkBonus(players[i]);
					tempsExecution = Bomb.getTime();
					// Draw Animation
					if(players[i].isDeadDrawable())
					{
						g.drawAnimation(players[i].getDead(), players[i].getX(), players[i].getY());
						if(players[i].getDead().getFrame() == 3)
						{
							LIFE_AVAILABLE--;
							try {
								init(gc, game);
							} catch (SlickException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
					}
					else
					{
						try
						{
							if(players[i] != null)
							{
								if(players[i].getAnimation(players[i].getDirection() + ( players[i].isMoving() ? 4 : 0)) != null)
								{
									g.drawAnimation(players[i].getAnimation(players[i].getDirection() + ( players[i].isMoving() ? 4 : 0)), players[i].getX(), players[i].getY());
								}
							}
						}
						catch(NullPointerException bl)
						{
							System.out.println("Impossible d'afficher l'animation car elle est nulle");
						}
						
					}
				}
			}
		}
	}
	// Catch key pressed events
	@Override
	public void keyPressed(int key, char c)
	{
		if(!isNewtorkGame)
		{
			switch(key)
			{
			case Input.KEY_UP :
			case Input.KEY_Z :
				players[0].setDirection(UP) ; 
				players[0].setMoving(true); 
				break;
			case Input.KEY_LEFT :
			case Input.KEY_Q:
				players[0].setDirection(LEFT) ; 
				players[0].setMoving(true); 
				break;
			case Input.KEY_DOWN : 
			case Input.KEY_S:
				players[0].setDirection(DOWN) ; 
				players[0].setMoving(true); 
				break;
			case Input.KEY_RIGHT :
			case Input.KEY_D:
				players[0].setDirection(RIGHT) ; 
				players[0].setMoving(true);  
				break;
				
			case Input.KEY_B:
				if(players[numPlayer].getBombe().size() < NB_BOMB_AVAILABLE)
				{
					new Thread(new Runnable(){
						public void run()
						{
							Case c = getCaseFromCoord(players[numPlayer].getX()+TAILLE_CASE/2, players[numPlayer].getY()+TAILLE_CASE - (TAILLE_CASE/2));
							if(c.getType() != "WALL" && c.getType() != "INDESTRUCTIBLE" && !c.hasBombe())
							{
								plateau[c.getY()][c.getX()].setHasBombe(true);
								Bomb b = new Bomb(bombSheet,c.getRealX()+TAILLE_BOMB/2,c.getRealY()+TAILLE_BOMB/2,new Explosion(c.getRealX()+TAILLE_BOMB/2,c.getRealY()+TAILLE_BOMB/2,explosionSheet));
								players[numPlayer].getBombe().add(b);
							}
						}
					}).start();
				}
				break;
			case Input.KEY_ESCAPE:
				System.exit(0);
				break;
			case Input.KEY_X:
			    if(players[numPlayer].isOnBomb() && players[numPlayer].canMoveBomb())
			    {
			    	Bomb bomb = players[numPlayer].getBombFromCoord(players[numPlayer].getX(), players[numPlayer].getY());
			    	switch(direction)
			    	{
				    	case UP:
				    		new Thread(){
				    			public void run()
				    			{
				    				long timerDeplacementDepart = Bomb.getTime();
				    				Case courante = getCaseFromCoord(bomb.getXBomb(), bomb.getYBomb());
				    				while(courante != null && courante.getType() != "WALL" && courante.getType() != "INDESTRUCTIBLE" && bomb.getYBomb()-1 > 0 && bomb.isDrawable())
						    		{
				    					long timerDeplacementCourant = Bomb.getTime();
						    			if(timerDeplacementCourant - timerDeplacementDepart > TIME_BETWEEN_BOMB_MOVEMENT)
						    			{
						    				bomb.setYBomb(bomb.getYBomb()-1);
						    				bomb.getExplosion().setY(bomb.getYBomb());
							    			courante = getCaseFromCoord(bomb.getXBomb(), bomb.getYBomb());
							    			timerDeplacementDepart = Bomb.getTime();
							    			courante.setHasBombe(false);
						    			}
						    			
						    		}
				    			}
				    		}.start();
				    		
				    		break;
				    	case LEFT:
				    		new Thread(){
				    			public void run()
				    			{
				    				long timerDeplacementDepart = Bomb.getTime();
				    				Case courante = getCaseFromCoord(bomb.getXBomb(), bomb.getYBomb());
				    				while(courante != null && courante.getType() != "WALL" && courante.getType() != "INDESTRUCTIBLE" && bomb.getXBomb()-1 > 0 && bomb.isDrawable())
						    		{
						    			long timerDeplacementCourant = Bomb.getTime();
						    			if(timerDeplacementCourant - timerDeplacementDepart > TIME_BETWEEN_BOMB_MOVEMENT)
						    			{
						    				bomb.setXBomb(bomb.getXBomb()-1);
						    				bomb.getExplosion().setX(bomb.getXBomb());
							    			courante = getCaseFromCoord(bomb.getXBomb(), bomb.getYBomb());
							    			timerDeplacementDepart = Bomb.getTime();
							    			courante.setHasBombe(false);
						    			}
						    			
						    		}
				    			}
				    		}.start();
				    		break;
				    	case DOWN:
				    		new Thread(){
				    			public void run()
				    			{
				    				long timerDeplacementDepart = Bomb.getTime();
				    				Case courante = getCaseFromCoord(bomb.getXBomb(), bomb.getYBomb());
				    				while(courante != null && courante.getType() != "WALL" && courante.getType() != "INDESTRUCTIBLE" && bomb.getYBomb()+TAILLE_BOMB < Main.HEIGHT && getCaseFromCoord(bomb.getXBomb(), bomb.getYBomb() + TAILLE_BOMB).getType() != "WALL" && bomb.getYBomb()+TAILLE_BOMB < Main.HEIGHT && getCaseFromCoord(bomb.getXBomb(), bomb.getYBomb() + TAILLE_BOMB).getType() != "INDESTRUCTIBLE" && bomb.isDrawable())
						    		{
						    			long timerDeplacementCourant = Bomb.getTime();
						    			if(timerDeplacementCourant - timerDeplacementDepart > TIME_BETWEEN_BOMB_MOVEMENT)
						    			{
						    				bomb.setYBomb(bomb.getYBomb()+1);
						    				bomb.getExplosion().setX(bomb.getYBomb());
							    			courante = getCaseFromCoord(bomb.getXBomb(), bomb.getYBomb());
							    			timerDeplacementDepart = Bomb.getTime();
							    			courante.setHasBombe(false);
						    			}
						    			
						    		}
				    			}
				    		}.start();
				    		break;
				    	case RIGHT:
				    		new Thread(){
				    			public void run()
				    			{
				    				long timerDeplacementDepart = Bomb.getTime();
				    				Case courante = getCaseFromCoord(bomb.getXBomb(), bomb.getYBomb());
				    				while(courante != null && courante.getType() != "WALL" && courante.getType() != "INDESTRUCTIBLE" && bomb.getXBomb() + TAILLE_BOMB < Main.WIDTH && getCaseFromCoord(bomb.getXBomb() + TAILLE_BOMB, bomb.getYBomb()).getType() != "WALL" && getCaseFromCoord(bomb.getXBomb() + TAILLE_BOMB, bomb.getYBomb()).getType() != "INDESTRUCTIBLE" && bomb.isDrawable())
						    		{
						    			long timerDeplacementCourant = Bomb.getTime();
						    			if(timerDeplacementCourant - timerDeplacementDepart > TIME_BETWEEN_BOMB_MOVEMENT)
						    			{
						    				bomb.setXBomb(bomb.getXBomb()+1);
						    				bomb.getExplosion().setX(bomb.getXBomb());
							    			courante = getCaseFromCoord(bomb.getXBomb(), bomb.getYBomb());
							    			timerDeplacementDepart = Bomb.getTime();
							    			courante.setHasBombe(false);
						    			}
						    			
						    		}
				    			}
				    		}.start();
				    		break;
			    	}
			    }
				break;
			}
		}
		
	}
	
	@Override
	public void keyReleased(int key, char c)
	{
		if(!isNewtorkGame)
		{
			players[0].setMoving(false);
		}
	}
	
	
	// Detect collision
	public boolean canMove(Player p,int direction,int delta)
	{
		boolean canMove = false;
		switch(direction)
		{
		// For each , i create a square and then i test than edge are not on wall or indestructible wall
			case UP : // UP
				if(p.getY() - delta * COEFF_DEPLACEMENT > TAILLE_CASE )
				{
					Rectangle rect = new Rectangle(p.getX(), p.getY() - delta*COEFF_DEPLACEMENT,OFFSET_HORIZONTAL_X_LEFTORRIGHT,OFFSET_VERTICAL_Y_LEFTORRIGHT);
					float[] points = rect.getPoints();
					Case hautGauche = getCaseFromCoord(points[0], points[1]);
					Case hautDroite = getCaseFromCoord(points[2], points[3]);
					if(hautGauche != null && hautDroite != null)
					{
						if(hautGauche.getType() != "WALL"
								&& hautGauche.getType() != "INDESTRUCTIBLE"
								&& hautDroite.getType() != "WALL"
								&& hautDroite.getType() != "INDESTRUCTIBLE"
								)
						canMove = true;
					}
					
					
				}
			break;
			
			case LEFT : // LEFT
				if(p.getX() - delta * COEFF_DEPLACEMENT > 0)
				{
					Rectangle rect = new Rectangle(p.getX() - delta*COEFF_DEPLACEMENT, p.getY(),OFFSET_HORIZONTAL_X_LEFTORRIGHT, OFFSET_VERTICAL_Y_LEFTORRIGHT);
					float[] points = rect.getPoints();
					Case hautGauche = getCaseFromCoord(points[0], points[1]);
					Case basGauche = getCaseFromCoord(points[6], points[7]);
					if(hautGauche != null && basGauche != null)
					{
						if(hautGauche.getType() != "WALL"
								&& hautGauche.getType() != "INDESTRUCTIBLE"
								&& basGauche.getType() != "WALL"
								&& basGauche.getType() != "INDESTRUCTIBLE"
								)
						canMove = true;
						
					}
					

				}
				
			break;
			
			case DOWN : // DOWN
				if(p.getY() + delta * COEFF_DEPLACEMENT < Main.HEIGHT)
				{
					Rectangle rect = new Rectangle(p.getX(),p.getY() + delta*COEFF_DEPLACEMENT,OFFSET_HORIZONTAL_X_LEFTORRIGHT,OFFSET_VERTICAL_Y_LEFTORRIGHT);
					float[] points = rect.getPoints();
					Case basDroite = getCaseFromCoord(points[4], points[5]);
					Case basGauche = getCaseFromCoord(points[6], points[7]);
					
					if(basDroite != null && basGauche != null)
					{
						if(basDroite.getType() != "WALL"
								&& basDroite.getType() != "INDESTRUCTIBLE"
								&& basGauche.getType() != "WALL"
								&& basGauche.getType() != "INDESTRUCTIBLE")
							canMove = true;
					}
					
				}
			break;
			
			case RIGHT : // RIGHT
				if(p.getX() + delta*COEFF_DEPLACEMENT < Main.WIDTH)
				{
					Rectangle rect = new Rectangle(p.getX()+delta*COEFF_DEPLACEMENT,p.getY(),OFFSET_HORIZONTAL_X_LEFTORRIGHT,OFFSET_VERTICAL_Y_LEFTORRIGHT);
					float[] points = rect.getPoints();
					Case hautDroite = getCaseFromCoord(points[2], points[3]);
					Case basDroite = getCaseFromCoord(points[4], points[5]);
					
					if(hautDroite != null && basDroite != null)
					{
						if(basDroite.getType() != "WALL"
								&& basDroite.getType() != "INDESTRUCTIBLE"
								&& hautDroite.getType() != "WALL"
								&& hautDroite.getType() != "INDESTRUCTIBLE")
							canMove = true;
					}
					
				}
			break;
		}
		return canMove;
	}
	
	
	
	
	
	public void initLevel(File f)
	{
		// Read the level file
				try {
					BufferedReader reader = new BufferedReader(new FileReader(f));
					String line;
					String type = "";
					int i = 0;
					while((line = reader.readLine()) != null) // While you got line
					{
						for(int j = 0 ; j < line.length(); j++)
						{
							switch(line.charAt(j))
							{
							case '0':
								type=GROUND;
								break;
							case '1':
								type=GRASSGROUND;
								break;
							case '2':
								type=WALL;
								break;
							case '3':
								type=INDESTRUCTIBLE;
								break;
							case '4':
								type=HOUSE;
								break;
							case '5':
								type=WOOD;
								break;
							case '6':
								type=GROUNDGRASSTEXAS;
								break;
							case '7':
								type=GROUNDTEXAS;
								break;
							case '8':
								type=EMPTY;
								break;
							}
							
							plateau[i][j] = new Case(j,i,type); // Create a case with the type set before
						}
						i++;
					}
					reader.close(); // Close the buffer
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException io) {
					// TODO Auto-generated catch block
					io.printStackTrace();
				}
	}
	
	// Draw the board 
	public void drawBoard(Graphics g)
	{
		Image toDraw = null;
		if(plateau != null)
		{
			for(int i = 0 ; i < NB_CASE_HAUTEUR; i++)
			{
				for(int j = 0 ; j < NB_CASE_LARGEUR ; j++)
				{
						switch(plateau[i][j].getType())
						{
						case WALL:
							toDraw = wall;
							break;
						case GROUND:
							toDraw = ground;
							break;
						case GRASSGROUND:
							toDraw = groundGrass;
							break;
						case INDESTRUCTIBLE:
							toDraw = indestructible_wall;
							break;
						case HOUSE:
							toDraw = house;
							break;
						case WOOD:
							toDraw = wood;
							break;
						case GROUNDGRASSTEXAS:
							toDraw = groundGrassTexas;
							break;
						case GROUNDTEXAS:
							toDraw = groundTexas;
							break;
						case EMPTY:
							toDraw = null;
							break;
						default:
							toDraw = ground;
							break;
						}
					if(toDraw != null && plateau != null)
						g.drawImage(toDraw,plateau[i][j].getRealX(),plateau[i][j].getRealY());
					else
					{
						g.setColor(new Color(240, 128, 0));
						g.fillRect(plateau[i][j].getRealX(),plateau[i][j].getRealY() , TAILLE_CASE, TAILLE_CASE);
					}
				}
		
			}
		}
	}
	
	// Draw bomb
	public void drawArray(LinkedList<Bomb> array,Graphics g)
	{
		if(array != null)
		{
			for(int i = 0 ; i < array.size(); i++)
			{
				if(array.get(i) != null)
				{
					if (Bomb.getTime() - array.get(i).getFirstTime()  > TIME_TO_EXPLODE)
					{
						if(bombExplode != null)
						{
							bombExplode.play();
						}
						Case c = getCaseFromCoord(array.get(i).getXBomb(),array.get(i).getYBomb());
						plateau[c.getY()][c.getX()].setHasBombe(false);
						array.get(i).setDrawable(false);
						array.get(i).setFirstTime(Bomb.getTime());
						if(array.get(i).getExplosion() != null)
						{
							array.get(i).getExplosion().setDrawable(true);
						}
					}
					else if(array.get(i).isDrawable())
					{
						g.drawAnimation(array.get(i).getAnimation(),array.get(i).getXBomb(),array.get(i).getYBomb());
						
					}
				}
			}
		}
	}
	
	// Draw explosion
	public void drawExplosion(LinkedList<Bomb> array,Graphics g)
	{
		if(array != null)
		{
			for(int i = 0 ; i< array.size(); i++)
			{
				if(array.get(i) != null)
				{
					if(array.get(i).getExplosion() != null)
					{
						if(array.get(i).getExplosion().getAnimation(i).getFrame() == 4 && array.get(i).getExplosion().isDrawable())
						{
							array.removeFirst();
						}
						else if(array.get(i).getExplosion().isDrawable())
						{
							
							if(isGameOver)
							{
								
							}
							else
							{
								for(int j = 0 ; j < TAILLE_EXPLOSION ; j++)
								{
									
									canRemove(array.get(i).getXBomb(),array.get(i).getYBomb() + TAILLE_CASE*j);
									canRemove(array.get(i).getXBomb()+ TAILLE_CASE*j,array.get(i).getYBomb());
									canRemove(array.get(i).getXBomb(),array.get(i).getYBomb() - TAILLE_CASE*j);
									canRemove(array.get(i).getXBomb() - TAILLE_CASE*j,array.get(i).getYBomb());
									g.drawAnimation(array.get(i).getExplosion().getAnimation()[1], array.get(i).getXBomb() - TAILLE_BOMB*j, array.get(i).getYBomb());
									g.drawAnimation(array.get(i).getExplosion().getAnimation()[3], array.get(i).getXBomb() + TAILLE_BOMB*j, array.get(i).getYBomb());
									g.drawAnimation(array.get(i).getExplosion().getAnimation()[5], array.get(i).getXBomb(), array.get(i).getYBomb() - TAILLE_BOMB*j);
									g.drawAnimation(array.get(i).getExplosion().getAnimation()[7], array.get(i).getXBomb(), array.get(i).getYBomb() + TAILLE_BOMB*j);
								}
								g.drawAnimation(array.get(i).getExplosion().getAnimation()[0], array.get(i).getXBomb(),array.get(i).getYBomb());
								g.drawAnimation(array.get(i).getExplosion().getAnimation()[2], array.get(i).getXBomb() - TAILLE_BOMB * TAILLE_EXPLOSION, array.get(i).getYBomb());
								g.drawAnimation(array.get(i).getExplosion().getAnimation()[4], array.get(i).getXBomb() + TAILLE_BOMB * TAILLE_EXPLOSION, array.get(i).getYBomb());
								g.drawAnimation(array.get(i).getExplosion().getAnimation()[6], array.get(i).getXBomb(), array.get(i).getYBomb() - TAILLE_BOMB * TAILLE_EXPLOSION);
								g.drawAnimation(array.get(i).getExplosion().getAnimation()[8], array.get(i).getXBomb(),array.get(i).getYBomb() + TAILLE_BOMB * TAILLE_EXPLOSION );
							}	
						}
					}
				}
			}
		}
	}
	
	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 1;
	}
	
	// Can remove wall ( when a bomb explode)
	public void canRemove(float x, float y)
	{
		Case c = getCaseFromCoord(x, y);
		if(c != null && c.getType() == WALL)
		{
			this.plateau[c.getY()][c.getX()].setType(GROUND);
		}
	}
	
	// Drawing bonus
	public void drawBonus(LinkedList<Bonus> bonus , Graphics g)
	{
		for(int i = 0 ; i < bonus.size() ; i++)
		{
			if(bonus.get(i) != null)
			{
				if(tempsExecution - bonus.get(i).getTimer() > TIME_BEFORE_DISSAPEAR)
				{
				
					if(bonus.size() >0)
					{
						bonus.removeFirst();
					}
				}
				else if(bonus.get(i).getDrawable())
				{
					g.drawImage(bonus.get(i).getImage(), bonus.get(i).getX() * TAILLE_CASE + (TAILLE_CASE - TAILLE_BOMB)/2, bonus.get(i).getY() * TAILLE_CASE + (TAILLE_CASE - TAILLE_BOMB)/2);
				}
			}
		}
	}
	
	// Check if a bonus is where the player moves
	public void checkBonus(Player p)
	{
		Case c = getCaseFromCoord(p.getX() + OFFSET_HORIZONTAL_X_LEFTORRIGHT/2, p.getY() + OFFSET_VERTICAL_Y_LEFTORRIGHT/2);
		for(int i = 0 ; i < bonus.size() ; i++)
		{
			Case toCompare = getCaseFromIndice(bonus.get(i).getX(),bonus.get(i).getY());
			if(c.equals(toCompare) && toCompare.isHasBonus())
			{
				bonus.get(i).setDrawable(false);
				toCompare.setHasbonus(false);
				plateau[toCompare.getY()][toCompare.getX()].setHasbonus(false);
				bonusSound.play();
				switch(bonus.get(i).getNom())
				{
					case BOMBADD:
						if(NB_BOMB_AVAILABLE < NB_BOMB_MAXI)
						NB_BOMB_AVAILABLE++;
					break;
					
					case BOMBLESS:
						if(NB_BOMB_AVAILABLE > NB_BOMB_AT_START)
							NB_BOMB_AVAILABLE--;
						break;
					case SPEEDUP:
						if(COEFF_DEPLACEMENT < COEFF_MAX)
						COEFF_DEPLACEMENT+= 0.05f;
					break;
					
					case SPEEDLESS:
						if(COEFF_DEPLACEMENT > COEFF_MIN)
							COEFF_DEPLACEMENT-=0.05f;
						break;
						
					case MOVEBOMB:
						p.setMoveBomb(true);
					break;
					
					case STOPBOMB:
						p.setMoveBomb(false);
					break;
					
					case BOXE:
						p.setPushPlayer(true);
					break;
					
					case UNBOXE:
						p.setPushPlayer(false);
					break;
					
					case EXPLODEMORE:
						if(TAILLE_EXPLOSION < EXPLOSION_MAX)
							TAILLE_EXPLOSION++;
					break;
					
					case DEAD:
						p.setIsDeadDrawable(true);
					break;
					
					case EXPLODELESS:
						if(TAILLE_EXPLOSION > TAILLE_EXPLODE_MIN)
							TAILLE_EXPLOSION--;
					break;
					
					case EXPLODELESSEFFICIENT:
						TAILLE_EXPLOSION = TAILLE_EXPLODE_MIN;
					break;
				}
			}
		}
	}
	
	// Get case from indice
	public Case getCaseFromIndice(int x,int y)
	{
		if(x >= 0 && x < NB_CASE_LARGEUR && y >=0 && y < NB_CASE_HAUTEUR)
			return plateau[y][x];
		return null;
	}
	
	// I get the case from x and y coord
		public Case getCaseFromCoord(float x, float y)
		{
			for(int i = 0 ; i < NB_CASE_HAUTEUR ; i++)
			{
				for(int j = 0 ; j < NB_CASE_LARGEUR ; j++)
				{
					Case c = plateau[i][j];
					if(x > c.getRealX() && x <= c.getRealX() + TAILLE_CASE && y > c.getRealY() && y <= c.getRealY() + TAILLE_CASE)
					{
						return c;
					}
				}
			}
			return null;
		}
		
	public Case[][] getBoard()
	{
		return this.plateau;
	}
	
	public int getNumPlayer()
	{
		return this.numPlayer;
	}
	
	public void setNumPlayer(int num)
	{
		this.numPlayer = num;
	}
	
	public SpriteSheet getDeplacementSheet()
	{
		return this.sheet;
	}
	
	public Player[] getPlayers()
	{
		return this.players;
	}

	public static int getNB_BOMB_AVAILABLE() {
		return NB_BOMB_AVAILABLE;
	}

	public static void setNB_BOMB_AVAILABLE(int nB_BOMB_AVAILABLE) {
		NB_BOMB_AVAILABLE = nB_BOMB_AVAILABLE;
	}

	public static int getNB_BOMB_ON_BOARD() {
		return NB_BOMB_ON_BOARD;
	}

	public static void setNB_BOMB_ON_BOARD(int nB_BOMB_ON_BOARD) {
		NB_BOMB_ON_BOARD = nB_BOMB_ON_BOARD;
	}

	public static int getLEVEL_START() {
		return LEVEL_START;
	}

	public static void setLEVEL_START(int lEVEL_START) {
		LEVEL_START = lEVEL_START;
	}

	public static int getLIFE_AVAILABLE() {
		return LIFE_AVAILABLE;
	}

	public static void setLIFE_AVAILABLE(int lIFE_AVAILABLE) {
		LIFE_AVAILABLE = lIFE_AVAILABLE;
	}

	public static int getNB_OBJECTIVE() {
		return NB_OBJECTIVE;
	}

	public static void setNB_OBJECTIVE(int nB_OBJECTIVE) {
		NB_OBJECTIVE = nB_OBJECTIVE;
	}

	public static float getCOEFF_DEPLACEMENT() {
		return COEFF_DEPLACEMENT;
	}

	public static void setCOEFF_DEPLACEMENT(float cOEFF_DEPLACEMENT) {
		COEFF_DEPLACEMENT = cOEFF_DEPLACEMENT;
	}

	public static int getTAILLE_EXPLOSION() {
		return TAILLE_EXPLOSION;
	}

	public static void setTAILLE_EXPLOSION(int tAILLE_EXPLOSION) {
		TAILLE_EXPLOSION = tAILLE_EXPLOSION;
	}

	public static int getCURRENT_TIME() {
		return CURRENT_TIME;
	}

	public static void setCURRENT_TIME(int cURRENT_TIME) {
		CURRENT_TIME = cURRENT_TIME;
	}

	public static int getS() {
		return s;
	}

	public static void setS(int s) {
		Game.s = s;
	}

	public static int getMin() {
		return min;
	}

	public static void setMin(int min) {
		Game.min = min;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	public boolean isGameOver() {
		return isGameOver;
	}

	public void setGameOver(boolean isGameOver) {
		this.isGameOver = isGameOver;
	}

	public boolean isNewtorkGame() {
		return isNewtorkGame;
	}

	public void setNewtorkGame(boolean isNewtorkGame) {
		this.isNewtorkGame = isNewtorkGame;
	}

	public boolean isLevelFinished() {
		return isLevelFinished;
	}

	public void setLevelFinished(boolean isLevelFinished) {
		this.isLevelFinished = isLevelFinished;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public Case[][] getPlateau() {
		return plateau;
	}

	public void setPlateau(Case[][] plateau) {
		this.plateau = plateau;
	}

	public Image getWall() {
		return wall;
	}

	public void setWall(Image wall) {
		this.wall = wall;
	}

	public Image getGround() {
		return ground;
	}

	public void setGround(Image ground) {
		this.ground = ground;
	}

	public Image getIndestructible_wall() {
		return indestructible_wall;
	}

	public void setIndestructible_wall(Image indestructible_wall) {
		this.indestructible_wall = indestructible_wall;
	}

	public Image getGroundGrass() {
		return groundGrass;
	}

	public void setGroundGrass(Image groundGrass) {
		this.groundGrass = groundGrass;
	}

	public Image getHouse() {
		return house;
	}

	public void setHouse(Image house) {
		this.house = house;
	}

	public Image getWood() {
		return wood;
	}

	public void setWood(Image wood) {
		this.wood = wood;
	}

	public Image getGroundGrassTexas() {
		return groundGrassTexas;
	}

	public void setGroundGrassTexas(Image groundGrassTexas) {
		this.groundGrassTexas = groundGrassTexas;
	}

	public Image getGroundTexas() {
		return groundTexas;
	}

	public void setGroundTexas(Image groundTexas) {
		this.groundTexas = groundTexas;
	}

	public Image getHud() {
		return hud;
	}

	public void setHud(Image hud) {
		this.hud = hud;
	}

	public Image getTimer() {
		return timer;
	}

	public void setTimer(Image timer) {
		this.timer = timer;
	}

	public SpriteSheet getSheet() {
		return sheet;
	}

	public void setSheet(SpriteSheet sheet) {
		this.sheet = sheet;
	}

	public SpriteSheet getBombSheet() {
		return bombSheet;
	}

	public void setBombSheet(SpriteSheet bombSheet) {
		this.bombSheet = bombSheet;
	}

	public SpriteSheet getExplosionSheet() {
		return explosionSheet;
	}

	public void setExplosionSheet(SpriteSheet explosionSheet) {
		this.explosionSheet = explosionSheet;
	}

	public SpriteSheet getBonusSheet() {
		return bonusSheet;
	}

	public void setBonusSheet(SpriteSheet bonusSheet) {
		this.bonusSheet = bonusSheet;
	}

	public SpriteSheet getDeadSheet() {
		return deadSheet;
	}

	public void setDeadSheet(SpriteSheet deadSheet) {
		this.deadSheet = deadSheet;
	}

	public SpriteSheet getGroundSheet() {
		return groundSheet;
	}

	public void setGroundSheet(SpriteSheet groundSheet) {
		this.groundSheet = groundSheet;
	}

	public SpriteSheet getNumbers() {
		return numbers;
	}

	public void setNumbers(SpriteSheet numbers) {
		this.numbers = numbers;
	}

	public Image[] getCompteur() {
		return compteur;
	}

	public void setCompteur(Image[] compteur) {
		this.compteur = compteur;
	}

	public LinkedList<Bonus> getBonus() {
		return bonus;
	}

	public void setBonus(LinkedList<Bonus> bonus) {
		this.bonus = bonus;
	}

	public long getTempsExecution() {
		return tempsExecution;
	}

	public void setTempsExecution(long tps) {
		tempsExecution = tps;
	}

	public long getTempsAuLancement() {
		return tempsAuLancement;
	}

	public void setTempsAuLancement(long temps) {
		tempsAuLancement = temps;
	}

	public long getTimerStart() {
		return timerStart;
	}

	public void setTimerStart(long timer) {
		timerStart = timer;
	}

	public Random getRand() {
		return rand;
	}

	public void setRand(Random rand) {
		this.rand = rand;
	}

	public Sound getBonusSound() {
		return bonusSound;
	}

	public void setBonusSound(Sound bonusSound) {
		this.bonusSound = bonusSound;
	}

	public Sound getBombExplode() {
		return bombExplode;
	}

	public void setBombExplode(Sound bombExplode) {
		this.bombExplode = bombExplode;
	}

	public Sound getBackground() {
		return background;
	}

	public void setBackground(Sound background) {
		this.background = background;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static int getUp() {
		return UP;
	}

	public static int getLeft() {
		return LEFT;
	}

	public static int getDown() {
		return DOWN;
	}

	public static int getRight() {
		return RIGHT;
	}

	public static int getLevelAtStart() {
		return LEVEL_AT_START;
	}

	public static int getLifeAtStart() {
		return LIFE_AT_START;
	}

	public static int getLargeurSprite() {
		return LARGEUR_SPRITE;
	}

	public static int getHauteurSprite() {
		return HAUTEUR_SPRITE;
	}

	public static int getLargeurNumber() {
		return LARGEUR_NUMBER;
	}

	public static int getHauteurNumber() {
		return HAUTEUR_NUMBER;
	}

	public static int getTailleBomb() {
		return TAILLE_BOMB;
	}

	public static int getTailleCase() {
		return TAILLE_CASE;
	}

	public static int getNbBombAtStart() {
		return NB_BOMB_AT_START;
	}

	public static int getNbBombMaxi() {
		return NB_BOMB_MAXI;
	}

	public static int getNbCaseHauteur() {
		return NB_CASE_HAUTEUR;
	}

	public static int getNbCaseLargeur() {
		return NB_CASE_LARGEUR;
	}

	public static int getTimegame() {
		return TIMEGAME;
	}

	public static int getOffsetVerticalYLeftorright() {
		return OFFSET_VERTICAL_Y_LEFTORRIGHT;
	}

	public static int getOffsetHorizontalXLeftorright() {
		return OFFSET_HORIZONTAL_X_LEFTORRIGHT;
	}

	public static int getTIMER() {
		return TIMER;
	}

	public static int getTimeToExplode() {
		return TIME_TO_EXPLODE;
	}

	public static int getTimeToBonusAppear() {
		return TIME_TO_BONUS_APPEAR;
	}

	public static int getTimeBeforeDissapear() {
		return TIME_BEFORE_DISSAPEAR;
	}

	public static int getTimeBetweenBombMovement() {
		return TIME_BETWEEN_BOMB_MOVEMENT;
	}

	public static int getNbBonus() {
		return NB_BONUS;
	}

	public static int getTailleMortLargeur() {
		return TAILLE_MORT_LARGEUR;
	}

	public static int getTailleMortHauteur() {
		return TAILLE_MORT_HAUTEUR;
	}

	public static int getTailleExplodeMin() {
		return TAILLE_EXPLODE_MIN;
	}

	public static String getBombadd() {
		return BOMBADD;
	}

	public static String getSpeedup() {
		return SPEEDUP;
	}

	public static String getMovebomb() {
		return MOVEBOMB;
	}

	public static String getBoxe() {
		return BOXE;
	}

	public static String getExplodemore() {
		return EXPLODEMORE;
	}

	public static String getDead() {
		return DEAD;
	}

	public static String getExplodeless() {
		return EXPLODELESS;
	}

	public static String getBombless() {
		return BOMBLESS;
	}

	public static String getSpeedless() {
		return SPEEDLESS;
	}

	public static String getStopbomb() {
		return STOPBOMB;
	}

	public static String getUnboxe() {
		return UNBOXE;
	}

	public static String getLowdmg() {
		return LOWDMG;
	}

	public static String getExplodelessefficient() {
		return EXPLODELESSEFFICIENT;
	}

	public static String getLevel() {
		return LEVEL;
	}

	public static String getWALL() {
		return WALL;
	}

	public static String getGROUND() {
		return GROUND;
	}

	public static String getGrassground() {
		return GRASSGROUND;
	}

	public static String getIndestructible() {
		return INDESTRUCTIBLE;
	}

	public static String getHOUSE() {
		return HOUSE;
	}

	public static String getWOOD() {
		return WOOD;
	}

	public static String getGroundgrasstexas() {
		return GROUNDGRASSTEXAS;
	}

	public static String getGroundtexas() {
		return GROUNDTEXAS;
	}

	public static String getEmpty() {
		return EMPTY;
	}

	public static float getCoeffMax() {
		return COEFF_MAX;
	}

	public static float getExplosionMax() {
		return EXPLOSION_MAX;
	}

	public static float getCoeffMin() {
		return COEFF_MIN;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}
	
	public Game getInstance()
	{
		return this;
	}
}
