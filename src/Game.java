
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
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class Game extends BasicGameState{
	
	// All static and no changing variables.
	public final static int UP = 0, LEFT = 1, DOWN = 2, RIGHT = 3;
	public final static int LARGEUR_SPRITE = 18, HAUTEUR_SPRITE = 32;
	public final static int TAILLE_BOMB = 16;
	public final static int TAILLE_CASE = 32;
	public final static int NB_BOMB_AT_START = 3,NB_BOMB_MAXI = 5;
	public final static int NB_CASE_HAUTEUR = 19, NB_CASE_LARGEUR = 25;
	public final static int OFFSET_VERTICAL_Y_LEFTORRIGHT = 29, OFFSET_HORIZONTAL_X_LEFTORRIGHT = 16;
	public final static int TIME_TO_EXPLODE = 4000, TIME_TO_BONUS_APPEAR = 20000, TIME_BEFORE_DISSAPEAR = 30000;
	public final static int NB_BONUS = 7;
	public final static String BOMBADD = "Bombe Supplémentaire",SPEEDUP="Vitesse supérieur",MOVEBOMB="Bombe déplacable";
	public final static String BOXE = "Ganx de boxe",EXPLODEMORE="Explosion plus longue", GROWDMG="Bombe plus efficiente",EXPLODELESS="Explosion diminuée";
	public final static String WALL = "WALL",GROUND= "GROUND", GRASSGROUND = "GRASSGROUND",INDESTRUCTIBLE ="INDESTRUCTIBLE";
	public final static float COEFF_MAX = 0.25f;
	// All static but modifiable variables.
	
	public static int NB_BOMB_AVAILABLE= 3,NB_BOMB_ON_BOARD = 0;
	public static int NB_OBJECTIVE = 3;
	public static float COEFF_DEPLACEMENT = 0.10f;
	
	// Other variables
	private boolean isMoving = false;
	private int direction = 0;
	private Player p ;
	private Case[][] plateau;
	private Image wall,ground,indestructible_wall,groundGrass;
	private SpriteSheet sheet, bombSheet,explosionSheet,bonusSheet;
	private LinkedList<Bonus> bonus ;
	private long tempsExecution = 0,tempsAuLancement = 0;
	private Random rand ;
	//private static BombermanAudioPlayer audioPlayer;

	public Game()
	{
		
	}
	
	// This is method is called when windows is opening
	@Override
	public void init(GameContainer gc,StateBasedGame game) throws SlickException
	{
		// Initialize SpriteSheet
		sheet = new SpriteSheet("images/Deplacements.png",LARGEUR_SPRITE,HAUTEUR_SPRITE);
		bombSheet = new SpriteSheet("images/Bombe.png",TAILLE_BOMB,TAILLE_BOMB);
		explosionSheet = new SpriteSheet("images/Explosions.png",TAILLE_BOMB,TAILLE_BOMB);
		// Initialize Images
		wall = new Image("images/Wall.png");
		ground = new Image("images/Groundsecond.png");
		groundGrass = new Image("images/Groundone.png");
		indestructible_wall = new Image("images/Indestructible_wall.png");
		bonusSheet = new SpriteSheet("images/Bonus.png",TAILLE_BOMB,TAILLE_BOMB);
		rand = new Random();
		tempsAuLancement = Bomb.getTime();
		// Initialize Player
		p = new Player(sheet,0,0,TAILLE_CASE);
		// Initialize plateau
		plateau = new Case[NB_CASE_HAUTEUR][NB_CASE_LARGEUR];
		bonus = new LinkedList<Bonus>();
		// Intialize level
		File level = new File("niveaux/niveau1.txt");
		initLevel(level);
		
	}
	
	// This method is called on some interval
	@Override
	public void render(GameContainer gc,StateBasedGame game, Graphics g) throws SlickException
	{
		g.setBackground(new Color(255,255,255,.5f));
		// Draw Board
		drawBoard(g);
		// Draw All Bombe And Explosion
		drawArray(p.getBombe(),g);
		drawExplosion(p.getBombe(),g);
		drawBonus(bonus,g);
		// Draw Animation 
		g.drawAnimation(p.getAnimation(direction + ( isMoving ? 4 : 0)), p.getX(), p.getY());
		checkBonus(p);
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
						bonus.add(new Bonus(EXPLODEMORE,bonusSheet.getSprite(4,0)));
						break;
					case 5:
						bonus.add(new Bonus(GROWDMG,bonusSheet.getSprite(5, 0)));
						break;
					case 6:
						bonus.add(new Bonus(EXPLODELESS,bonusSheet.getSprite(6, 0)));
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
		tempsExecution = Bomb.getTime();
		
		// Check if bomberman walk on bonus
		
	}
	
	// Check difference between last render and now
	@Override
	public void update(GameContainer gc,StateBasedGame game, int delta) throws SlickException
	{
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
	
	@Override
	public void keyPressed(int key, char c)
	{
		switch(key)
		{
		case Input.KEY_UP : this.direction = UP ; this.isMoving = true; break;
		case Input.KEY_LEFT : this.direction = LEFT ; this.isMoving = true; break;
		case Input.KEY_DOWN : this.direction = DOWN ; this.isMoving = true; break;
		case Input.KEY_RIGHT : this.direction = RIGHT ; this.isMoving = true; break;
		case Input.KEY_B:
			if(p.getBombe().size() < NB_BOMB_AVAILABLE)
			{
				new Thread(new Runnable(){
					public void run()
					{
						Case c = getCaseFromCoord(p.getX()+TAILLE_CASE/2, p.getY()+TAILLE_CASE - (TAILLE_CASE/2));
						if(c.getType() != "WALL" && c.getType() != "INDESTRUCTIBLE")
						{
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
		// For collision i did make 4 squares, represent each edges of sprites to Bomberman.
		boolean canMove = false;
		switch(direction)
		{
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
								type =INDESTRUCTIBLE;
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
					default:
						toDraw = ground;
						break;
					}
					g.drawImage(toDraw,plateau[i][j].getRealX(),plateau[i][j].getRealY());
			}
		}
	}
	
	// Method for droing the bombArray on graphics
	private void drawArray(LinkedList<Bomb> array,Graphics g)
	{
		for(int i = 0 ; i < array.size(); i++)
		{
			if(array.get(i) != null)
			{
				if (Bomb.getTime() - array.get(i).getFirstTime()  > TIME_TO_EXPLODE)
				{
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
						g.drawAnimation(array.get(i).getExplosion().getAnimation()[0], array.get(i).getXBomb(),array.get(i).getYBomb());
						canRemove(array.get(i).getXBomb(),array.get(i).getYBomb() + TAILLE_CASE);
						g.drawAnimation(array.get(i).getExplosion().getAnimation()[1], array.get(i).getXBomb() - TAILLE_BOMB, array.get(i).getYBomb());
						g.drawAnimation(array.get(i).getExplosion().getAnimation()[2], array.get(i).getXBomb() - TAILLE_BOMB * 2, array.get(i).getYBomb());
						
						canRemove(array.get(i).getXBomb()+ TAILLE_CASE,array.get(i).getYBomb());
						g.drawAnimation(array.get(i).getExplosion().getAnimation()[3], array.get(i).getXBomb() + TAILLE_BOMB, array.get(i).getYBomb());
						g.drawAnimation(array.get(i).getExplosion().getAnimation()[4], array.get(i).getXBomb() + TAILLE_BOMB * 2, array.get(i).getYBomb());
						canRemove(array.get(i).getXBomb(),array.get(i).getYBomb() - TAILLE_CASE);
						g.drawAnimation(array.get(i).getExplosion().getAnimation()[5], array.get(i).getXBomb(), array.get(i).getYBomb() - TAILLE_BOMB);
						g.drawAnimation(array.get(i).getExplosion().getAnimation()[6], array.get(i).getXBomb(), array.get(i).getYBomb() - TAILLE_BOMB * 2);
						
						canRemove(array.get(i).getXBomb() - TAILLE_CASE,array.get(i).getYBomb());
						g.drawAnimation(array.get(i).getExplosion().getAnimation()[7], array.get(i).getXBomb(), array.get(i).getYBomb() + TAILLE_BOMB);
						g.drawAnimation(array.get(i).getExplosion().getAnimation()[8], array.get(i).getXBomb(),array.get(i).getYBomb() + TAILLE_BOMB * 2 );
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
	
	private void canRemove(float x, float y)
	{
		Case c = getCaseFromCoord(x, y);
		if(c != null && c.getType() == WALL)
		{
			plateau[c.getY()][c.getX()].setType(GROUND);;
		}
	}
	
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
				switch(bonus.get(i).getNom())
				{
					case BOMBADD:
						if(NB_BOMB_AVAILABLE < NB_BOMB_MAXI)
						NB_BOMB_AVAILABLE++;
					break;
					
					case SPEEDUP:
						if(COEFF_DEPLACEMENT < COEFF_MAX)
						COEFF_DEPLACEMENT+= 0.05f;
					break;
						
					case MOVEBOMB:
					break;
					
					case BOXE:
					break;
					
					case EXPLODEMORE:
					break;
					
					case GROWDMG:
					break;
					
					case EXPLODELESS:
					break;
				}
			}
		}
	}
	
	public Case getCaseFromIndice(int x,int y)
	{
		if(x >= 0 && x < NB_CASE_LARGEUR && y >=0 && y < NB_CASE_HAUTEUR)
			return plateau[y][x];
		return null;
	}
}
