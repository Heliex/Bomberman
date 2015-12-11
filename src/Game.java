
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Game extends BasicGame{
	
	// All static and no changing variables.
	public final static int LARGEUR_SPRITE = 16, HAUTEUR_SPRITE = 32;
	public final static int TAILLE_CASE = 32;
	public final static int NB_CASE_HAUTEUR = 16, NB_CASE_LARGEUR = 25;
	
	// All static but modifiable variables.
	public static int NB_BOMBE_AT_START = 3;
	public static int NB_OBJECTIV = 3;
	
	// Other variables
	private boolean isMoving = false;
	private int direction = 0;
	private Player p ;
	private Case[][] plateau;
	public Game(String name)
	{
		super(name);
	}
	
	// This is method is called when windows is opening
	@Override
	public void init(GameContainer gc) throws SlickException
	{
		// Initialize SpriteSheet
		SpriteSheet sheet = new SpriteSheet("images/Deplacement.png",16,32);
		p = new Player(sheet,0,0,0);
		// Initialize plateau
		plateau = new Case[NB_CASE_HAUTEUR][NB_CASE_LARGEUR];
		// Intialize level
		File f = new File("niveaux/niveau1.txt");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String line;
			String type = "";
			int i = 0;
			while((line = reader.readLine()) != null)
			{
				for(int j = 0 ; j < line.length(); j++)
				{
					if(line.charAt(j) == '1')
					{
						type="MUR";
					}
					else
					{
						type="VIDE";
					}
					plateau[i][j] = new Case(j,i,type);
				}
				i++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException io) {
			// TODO Auto-generated catch block
			io.printStackTrace();
		}
	}
	
	// This method is called on some interval
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		g.setBackground(new Color(0,0,0,.5f));
		for(int i = 0 ; i < NB_CASE_HAUTEUR; i++)
		{
			for(int j = 0 ; j < NB_CASE_LARGEUR ; j++)
			{
				g.drawRect(plateau[i][j].getX() * TAILLE_CASE, plateau[i][j].getY() * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE);
			}
		}
		g.drawAnimation(p.getAnimation(direction + ( isMoving ? 4 : 0)), p.getX(), p.getY());
		
		//Carré BasDroite
		g.drawRect(p.getX() + 8, p.getY() + 16, 8, 8);
		
		//Carré HautDroite
		g.drawRect(p.getX() + 8, p.getY(),8,8);
		
		//Carré HautGauche 
		g.drawRect(p.getX(), p.getY(), 8, 8);
		//Carré BasGauche
		g.drawRect(p.getX(), p.getY()+16, 8, 8);
	}
	
	// Check difference between last render and now
	@Override
	public void update(GameContainer gc, int delta) throws SlickException
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
			case 0 :
			// Si on dépasse pas du plateau	
			if(p.getY() - delta*0.1f > 0 && getCaseFromCoord(p.getX(),p.getY()).getType() != "MUR" && getCaseFromCoord(p.getX() + 16, p.getY()).getType() != "MUR")
				canMove = true;
			break;
			// Si on dépasse pas du plateau
			case 1 :
			if(p.getX() - delta*0.1f > 0 && getCaseFromCoord(p.getX(),p.getY()).getType() != "MUR" && getCaseFromCoord(p.getX(), p.getY() + 22).getType() != "MUR")
				canMove = true;
			break;
			
			case 2 :
			// Si on dépasse pas du plateau
			if(p.getY() + TAILLE_CASE< NB_CASE_HAUTEUR * TAILLE_CASE && getCaseFromCoord(p.getX(), p.getY() + 22).getType() !="MUR" && getCaseFromCoord(p.getX() + 16, p.getY() + 22).getType() != "MUR")
				canMove = true;
			break;
			
			case 3 :
			// Si on dépasse pas du plateau
			if(p.getX() + LARGEUR_SPRITE < NB_CASE_LARGEUR * TAILLE_CASE &&  getCaseFromCoord(p.getX() + 16, p.getY()).getType() != "MUR" && getCaseFromCoord(p.getX() + 16, p.getY() + 22).getType() != "MUR")
				canMove = true;
			break;
		}
		return canMove;
	}
	
	public Case getCaseFromCoord(float x, float y)
	{
		for(int i = 0 ; i < NB_CASE_HAUTEUR ; i++)
		{
			for(int j = 0 ; j < NB_CASE_LARGEUR ; j++)
			{
				Case c = plateau[i][j];
				if(x >= (c.getX()*TAILLE_CASE) && x <= (c.getX() * TAILLE_CASE) + TAILLE_CASE && y >= (c.getY()*TAILLE_CASE) && y <= (c.getY() * TAILLE_CASE) + TAILLE_CASE)
				{
					System.out.println(c);
					return c;
				}
			}
		}
		return null;
	}
}

