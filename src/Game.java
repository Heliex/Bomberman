
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class Game extends BasicGameState{
	
	// All static and no changing variables.
	public final static int LARGEUR_SPRITE = 18, HAUTEUR_SPRITE = 32;
	public final static int TAILLE_BOMB = 18;
	public final static int TAILLE_EXPLOSION = 19;
	public final static int TAILLE_CASE = 32;
	public final static int NB_BOMB_AT_START = 3;
	public final static int NB_CASE_HAUTEUR = 19, NB_CASE_LARGEUR = 25;
	public final static int OFFSET_VERTICAL = 30, OFFSET_HORIZONTAL = 16;
	public final static int TIME_TO_EXPLODE = 4000;
	
	// All static but modifiable variables.
	
	public static int NB_BOMB_AVAILABLE= 3,NB_BOMB_ON_BOARD = 0;
	public static int NB_OBJECTIVE = 3;
	
	// Other variables
	private boolean isMoving = false;
	private int direction = 0;
	private Player p ;
	private Case[][] plateau;
	private Image wall,ground,indestructible_wall,groundGrass;
	private SpriteSheet sheet, bombSheet,explosionSheet;
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
		explosionSheet = new SpriteSheet("images/Explosions.png",TAILLE_EXPLOSION,TAILLE_EXPLOSION);
		// Initialize Images
		wall = new Image("images/Wall.png");
		ground = new Image("images/Groundsecond.png");
		groundGrass = new Image("images/Groundone.png");
		indestructible_wall = new Image("images/Indestructible_wall.png");
		// Initialize Player
		p = new Player(sheet,0,0,TAILLE_CASE);
		// Initialize plateau
		plateau = new Case[NB_CASE_HAUTEUR][NB_CASE_LARGEUR];
		// Intialize level
		File level = new File("niveaux/niveau1.txt");
		initLevel(level);
		
	}
	
	// This method is called on some interval
	@Override
	public void render(GameContainer gc,StateBasedGame game, Graphics g) throws SlickException
	{
		g.setBackground(new Color(255,255,255,.5f));
		drawBoard(g);
		drawArray(p.getBombe(),g);
		drawExplosion(p.getBombe(),g);
		// Draw Animation 
		g.drawAnimation(p.getAnimation(direction + ( isMoving ? 4 : 0)), p.getX(), p.getY());
		
		// Go throught the bombArray to redraw it
		
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
				case 0 :
					p.setY(p.getY() - delta * .1f); 	
				break;
				case 1 : 
					p.setX(p.getX() - delta * .1f); 
				break;
				case 2 : 
					p.setY(p.getY() + delta * .1f); 
				break;
				case 3 : 
					p.setX(p.getX() + delta * .1f); 
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
		case Input.KEY_UP : this.direction = 0 ; this.isMoving = true; break;
		case Input.KEY_LEFT : this.direction = 1 ; this.isMoving = true; break;
		case Input.KEY_DOWN : this.direction = 2 ; this.isMoving = true; break;
		case Input.KEY_RIGHT : this.direction = 3 ; this.isMoving = true; break;
		case Input.KEY_B: 
			if(NB_BOMB_ON_BOARD < NB_BOMB_AVAILABLE && p.getBombe().size() < NB_BOMB_AVAILABLE)
			{
				new Thread(new Runnable(){
					public void run()
					{
							Bomb b = new Bomb(bombSheet,p.getX(),p.getY(),new Explosion(p.getX(),p.getY()+TAILLE_CASE/2,explosionSheet));
							p.getBombe().add(b);
							NB_BOMB_ON_BOARD++;
						
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
			case 0 :
			// If we're in the board And a case is not a wall or an indestructible wall
			if(p.getY() - delta*0.1f > 0 
					&& getCaseFromCoord(p.getX(),p.getY()).getType() != "WALL" 
					&& getCaseFromCoord(p.getX() + OFFSET_HORIZONTAL, p.getY()).getType() != "WALL"
					&& getCaseFromCoord(p.getX(),p.getY()).getType() != "INDESTRUCTIBLE"
					&& getCaseFromCoord(p.getX() + OFFSET_HORIZONTAL, p.getY()).getType() != "INDESTRUCTIBLE"
					)
				canMove = true;
			break;
			
			case 1 :
			if(p.getX() - delta*0.1f > 0 
					&& getCaseFromCoord(p.getX(),p.getY()+2).getType() != "WALL" 
					&& getCaseFromCoord(p.getX(), p.getY() + OFFSET_VERTICAL).getType() != "WALL"
					&& getCaseFromCoord(p.getX(),p.getY()+2).getType() != "INDESTRUCTIBLE"
					&& getCaseFromCoord(p.getX(), p.getY() + OFFSET_VERTICAL).getType() != "INDESTRUCTIBLE"
					)
				canMove = true;
			break;
			
			case 2 :
			if(p.getY() + TAILLE_CASE< NB_CASE_HAUTEUR * TAILLE_CASE 
					&& getCaseFromCoord(p.getX(), p.getY() + OFFSET_VERTICAL).getType() !="WALL" 
					&& getCaseFromCoord(p.getX() + OFFSET_HORIZONTAL, p.getY() + OFFSET_VERTICAL).getType() != "WALL"
					&& getCaseFromCoord(p.getX(), p.getY() + OFFSET_VERTICAL).getType() !="INDESTRUCTIBLE"
					&& getCaseFromCoord(p.getX() + OFFSET_HORIZONTAL, p.getY() + OFFSET_VERTICAL).getType() != "INDESTRUCTIBLE"
					)
				canMove = true;
			break;
			
			case 3 :
			if(p.getX() + LARGEUR_SPRITE < NB_CASE_LARGEUR * TAILLE_CASE 
					&& getCaseFromCoord(p.getX() + OFFSET_HORIZONTAL, p.getY()+2).getType() != "WALL" 
					&& getCaseFromCoord(p.getX() + OFFSET_HORIZONTAL, p.getY() + OFFSET_VERTICAL).getType() != "WALL"
					&& getCaseFromCoord(p.getX() + OFFSET_HORIZONTAL, p.getY()+2).getType() != "INDESTRUCTIBLE"
					&& getCaseFromCoord(p.getX() + OFFSET_HORIZONTAL, p.getY() + OFFSET_VERTICAL).getType() != "INDESTRUCTIBLE"
					)
				canMove = true;
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
				if(x >= (c.getX()*TAILLE_CASE) && x <= (c.getX() * TAILLE_CASE) + TAILLE_CASE && y >= (c.getY()*TAILLE_CASE) && y <= (c.getY() * TAILLE_CASE) + TAILLE_CASE)
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
								type="GROUND";
								break;
							case '1':
								type="GRASSGROUND";
								break;
							case '2':
								type="WALL";
								break;
							case '3':
								type ="INDESTRUCTIBLE";
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
				case "WALL":
					toDraw = wall;
					break;
				case "GROUND":
					toDraw = ground;
					break;
				case "GRASSGROUND":
					toDraw = groundGrass;
					break;
				case "INDESTRUCTIBLE":
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
	private void drawArray(ArrayList<Bomb> array,Graphics g)
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
	
	private void drawExplosion(ArrayList<Bomb> array,Graphics g)
	{
		for(int i = 0 ; i< array.size(); i++)
		{
			if(array.get(i) != null)
			{
				if(array.get(i).getExplosion() != null)
				{
					if(array.get(i).getExplosion().getAnimation(i).getFrame() == 4 && array.get(i).getExplosion().isDrawable())
					{
						if(array.size() > (NB_BOMB_AVAILABLE - NB_BOMB_ON_BOARD))
						{
							array.remove((NB_BOMB_AVAILABLE - NB_BOMB_ON_BOARD));
							if(NB_BOMB_ON_BOARD > 0)
							{
								NB_BOMB_ON_BOARD--;
							}
						}	
					}
					else if(array.get(i).getExplosion().isDrawable())
					{
						g.drawAnimation(array.get(i).getExplosion().getAnimation()[0], array.get(i).getXBomb(),array.get(i).getYBomb());
						checkExplode(array.get(i).getXBomb() - TAILLE_EXPLOSION, array.get(i).getYBomb());
						g.drawAnimation(array.get(i).getExplosion().getAnimation()[1], array.get(i).getXBomb() - TAILLE_EXPLOSION, array.get(i).getYBomb());
						checkExplode(array.get(i).getXBomb() - TAILLE_EXPLOSION * 2,array.get(i).getYBomb());
						g.drawAnimation(array.get(i).getExplosion().getAnimation()[2], array.get(i).getXBomb() - TAILLE_EXPLOSION * 2, array.get(i).getYBomb());
						checkExplode(array.get(i).getXBomb() + TAILLE_EXPLOSION, array.get(i).getYBomb());
						g.drawAnimation(array.get(i).getExplosion().getAnimation()[3], array.get(i).getXBomb() + TAILLE_EXPLOSION, array.get(i).getYBomb());
						checkExplode(array.get(i).getXBomb() + TAILLE_EXPLOSION * 2, array.get(i).getYBomb());
						g.drawAnimation(array.get(i).getExplosion().getAnimation()[4], array.get(i).getXBomb() + TAILLE_EXPLOSION * 2, array.get(i).getYBomb());
						checkExplode(array.get(i).getXBomb(), array.get(i).getYBomb() - TAILLE_EXPLOSION);
						g.drawAnimation(array.get(i).getExplosion().getAnimation()[5], array.get(i).getXBomb(), array.get(i).getYBomb() - TAILLE_EXPLOSION);
						checkExplode(array.get(i).getXBomb(), array.get(i).getYBomb() - TAILLE_EXPLOSION * 2);
						g.drawAnimation(array.get(i).getExplosion().getAnimation()[6], array.get(i).getXBomb(), array.get(i).getYBomb() - TAILLE_EXPLOSION * 2);
						checkExplode(array.get(i).getXBomb(), array.get(i).getYBomb() + TAILLE_EXPLOSION);
						g.drawAnimation(array.get(i).getExplosion().getAnimation()[7], array.get(i).getXBomb(), array.get(i).getYBomb() + TAILLE_EXPLOSION);
						checkExplode(array.get(i).getXBomb(), array.get(i).getYBomb() + TAILLE_EXPLOSION * 2 );
						g.drawAnimation(array.get(i).getExplosion().getAnimation()[8], array.get(i).getXBomb(),array.get(i).getYBomb() + TAILLE_EXPLOSION * 2 );
					}
				}
			}
		}
	}
	
	private void checkExplode(float x, float y)
	{
		if(x >= 0 && y >= 0 && x < NB_CASE_LARGEUR * TAILLE_CASE && y < NB_CASE_HAUTEUR * TAILLE_CASE)
		{
			Case c = getCaseFromCoord(x, y);
			if(c.getType() == "WALL")
			{
				c.setType("GROUND");
			}
		}
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 1;
	}
}
