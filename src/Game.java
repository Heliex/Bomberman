
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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


public class Game extends BasicGameState{
	
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
	private boolean isMoving = false,isGameOver = false ;
	private boolean isLevelFinished;
	private int direction = 0;
	private Player p ;
	private Case[][] plateau;
	private Image wall,ground,indestructible_wall,groundGrass, house, wood, groundGrassTexas,groundTexas,hud,timer;
	private SpriteSheet sheet, bombSheet,explosionSheet,bonusSheet,deadSheet,groundSheet,numbers;
	private Image[] compteur = new Image[10];
	private LinkedList<Bonus> bonus ;
	private long tempsExecution = 0,tempsAuLancement = 0,timerStart=0;
	private Random rand ;
	private Sound bonusSound,bombExplode,background;
	// This is method is called when windows is opening
	@Override
	public void init(GameContainer gc,StateBasedGame game) throws SlickException
	{
		isLevelFinished = false;
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
		rand = new Random();
		tempsAuLancement = Bomb.getTime();
		// Initialize Player
		p = new Player(sheet,0,0,TAILLE_CASE,deadSheet);
		// Initialize board
		plateau = new Case[NB_CASE_HAUTEUR][NB_CASE_LARGEUR];
		bonus = new LinkedList<Bonus>();
		
		// Initialize song
		bonusSound = new Sound("sons/ramasserBonus.wav");
		bombExplode = new Sound("sons/bombExplode.wav");
		background = new Sound("sons/builtToFall.wav");
		
		// Intialize level
		File level = new File("niveaux/" + LEVEL + LEVEL_START+ ".txt");
		initLevel(level);
		
		// Initialize timer
		timerStart = Bomb.getTime();
		
		NB_BOMB_AVAILABLE = NB_BOMB_AT_START;
		COEFF_DEPLACEMENT = COEFF_MIN;
		TAILLE_EXPLOSION = TAILLE_EXPLODE_MIN;
		CURRENT_TIME = TIMEGAME;
		min = ((TIMEGAME/TIMER) % 3600) / 60;
		s = (TIMEGAME/TIMER) % 60 ;
	}
	
	// This method is called on some interval
	@Override
	public void render(GameContainer gc,StateBasedGame game, Graphics g) throws SlickException // Render
	{
		if(!background.playing())
		{
			background.play();
		}
		g.setBackground(new Color(255,255,255,.5f));
		// Draw Board
		drawBoard(g);
		// Draw All Bombe And Explosion
		drawArray(p.getBombe(),g);
		drawExplosion(p.getBombe(),g);
		drawBonus(bonus,g);
		
		// Check if bomberman walk on bonus
		checkBonus(p);
		tempsExecution = Bomb.getTime();
		// Draw Animation
		if(p.isDeadDrawable())
		{
			g.drawAnimation(p.getDead(), p.getX(), p.getY());
			if(p.getDead().getFrame() == 3)
			{
				LIFE_AVAILABLE--;
				init(gc, game);
			}
			
		}
		else
		{
			g.drawAnimation(p.getAnimation(direction + ( isMoving ? 4 : 0)), p.getX(), p.getY());
		}
		
		if(isLevelFinished)
		{
			LEVEL_START++;
			init(gc,game);
		}
		
		// Draw Hud and timer
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
		
		// Draw count on timer
	}
	
	// Game logic
	@Override
	public void update(GameContainer gc,StateBasedGame game, int delta) throws SlickException
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
		// If player is in explosion
		if(p.isInExplosion())
		{
			p.setIsDeadDrawable(true);
		}
		// Add random bonus on map at every interval
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
		// Check if you can move before move
		if(canMove(p,direction,delta))
		{
			if(this.isMoving)
			{
				switch(direction)
				{
				case UP :
					p.setY(p.getY() - delta * COEFF_DEPLACEMENT); 	
				break;
				case LEFT : 
					p.setX(p.getX() - delta * COEFF_DEPLACEMENT); 
				break;
				case DOWN : 
					p.setY(p.getY() + delta * COEFF_DEPLACEMENT); 
				break;
				case RIGHT : 
					p.setX(p.getX() + delta * COEFF_DEPLACEMENT); 
				break;
				}
			}
		}
		
			
	}
	
	// Catch key pressed events
	@Override
	public void keyPressed(int key, char c)
	{
		switch(key)
		{
		case Input.KEY_UP :
		case Input.KEY_Z :
			this.direction = UP ; 
			this.isMoving = true; 
			break;
		case Input.KEY_LEFT :
		case Input.KEY_Q:
			this.direction = LEFT ; 
			this.isMoving = true; 
			break;
		case Input.KEY_DOWN : 
		case Input.KEY_S:
			this.direction = DOWN ; 
			this.isMoving = true; 
			break;
		case Input.KEY_RIGHT :
		case Input.KEY_D:
			this.direction = RIGHT ; 
			this.isMoving = true; 
			break;
			
		case Input.KEY_B:
			if(p.getBombe().size() < NB_BOMB_AVAILABLE)
			{
				new Thread(new Runnable(){
					public void run()
					{
						Case c = getCaseFromCoord(p.getX()+TAILLE_CASE/2, p.getY()+TAILLE_CASE - (TAILLE_CASE/2));
						if(c.getType() != "WALL" && c.getType() != "INDESTRUCTIBLE" && !c.hasBombe())
						{
							plateau[c.getY()][c.getX()].setHasBombe(true);
							Bomb b = new Bomb(bombSheet,c.getRealX()+TAILLE_BOMB/2,c.getRealY()+TAILLE_BOMB/2,new Explosion(c.getRealX()+TAILLE_BOMB/2,c.getRealY()+TAILLE_BOMB/2,explosionSheet));
							p.getBombe().add(b);
						}
					}
				}).start();
			}
			break;
		case Input.KEY_ESCAPE:
			System.exit(0);
			break;
		case Input.KEY_X:
		    if(p.isOnBomb() && p.canMoveBomb())
		    {
		    	Bomb bomb = p.getBombFromCoord(p.getX(), p.getY());
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
	
	@Override
	public void keyReleased(int key, char c)
	{
		this.isMoving = false;
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
	
	
	
	
	
	private void initLevel(File f)
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
	private void drawBoard(Graphics g)
	{
		Image toDraw = null;
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
					if(toDraw != null)
					g.drawImage(toDraw,plateau[i][j].getRealX(),plateau[i][j].getRealY());
					else
					{
						g.setColor(new Color(240, 128, 0));
						g.fillRect(plateau[i][j].getRealX(),plateau[i][j].getRealY() , TAILLE_CASE, TAILLE_CASE);
					}
			}
		}
	}
	
	// Draw bomb
	private void drawArray(LinkedList<Bomb> array,Graphics g)
	{
		for(int i = 0 ; i < array.size(); i++)
		{
			if(array.get(i) != null)
			{
				if (Bomb.getTime() - array.get(i).getFirstTime()  > TIME_TO_EXPLODE)
				{
					bombExplode.play();
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
	
	// Draw explosion
	private void drawExplosion(LinkedList<Bomb> array,Graphics g)
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
	
	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 1;
	}
	
	// Can remove wall ( when a bomb explode)
	private void canRemove(float x, float y)
	{
		Case c = getCaseFromCoord(x, y);
		if(c != null && c.getType() == WALL)
		{
			plateau[c.getY()][c.getX()].setType(GROUND);
		}
	}
	
	// Drawing bonus
	private void drawBonus(LinkedList<Bonus> bonus , Graphics g)
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
	private void checkBonus(Player p)
	{
		Case c = getCaseFromCoord(p.getX() + OFFSET_HORIZONTAL_X_LEFTORRIGHT/2, p.getY() + OFFSET_VERTICAL_Y_LEFTORRIGHT/2);
		for(int i = 0 ; i < bonus.size() ; i++)
		{
			Case toCompare = getCaseFromIndice(bonus.get(i).getX(),bonus.get(i).getY());
			if(c.equals(toCompare) && toCompare.hasBonus)
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
}
