package Graphique;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import Logique.Case;

public class CaseUI {

	private Case c;
	SpriteSheet sheet;
	Image[] sol;
	public static final int WALL = 0, GROUND = 1, GRASSGROUND=2, INDESTRUCTIBLE=3, HOUSE = 4, WOOD = 5, GROUNDGRASSTEXAS=6, GROUNDTEXAS=7, EMPTY = 8;
	
	public CaseUI(Case c)
	{
		this.c = c;
		this.sol = new Image[10];
		try {
			this.sheet = new SpriteSheet("images/Grounds.png",Case.TAILLE_CASE,Case.TAILLE_CASE);
			this.sol[WALL] = sheet.getSubImage(3*Case.TAILLE_CASE, 0, Case.TAILLE_CASE, Case.TAILLE_CASE);
			this.sol[GROUND] = sheet.getSubImage(Case.TAILLE_CASE, 0, Case.TAILLE_CASE, Case.TAILLE_CASE);
			this.sol[GRASSGROUND] = sheet.getSubImage(0, 0, Case.TAILLE_CASE, Case.TAILLE_CASE);
			this.sol[INDESTRUCTIBLE] = sheet.getSubImage(2*Case.TAILLE_CASE, 0, Case.TAILLE_CASE, Case.TAILLE_CASE);
			this.sol[HOUSE] = sheet.getSubImage(0, 1*Case.TAILLE_CASE, Case.TAILLE_CASE, Case.TAILLE_CASE);
			this.sol[WOOD] = sheet.getSubImage(1, 1*Case.TAILLE_CASE, Case.TAILLE_CASE, Case.TAILLE_CASE);
			this.sol[GROUNDGRASSTEXAS] = sheet.getSubImage(2*Case.TAILLE_CASE, 1, Case.TAILLE_CASE, Case.TAILLE_CASE);
			this.sol[GROUNDTEXAS] = sheet.getSubImage(3*Case.TAILLE_CASE, 1, Case.TAILLE_CASE, Case.TAILLE_CASE);
			this.sol[EMPTY] = sheet.getSubImage(Case.TAILLE_CASE, 0, Case.TAILLE_CASE, Case.TAILLE_CASE);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Image getImage(String typeCase)
	{
		Image image = null;
		switch(typeCase)
		{
			case Case.WALL:
				image = sol[WALL];
				break;
				
			case Case.GROUND:
				image = sol[GROUND];
				break;
				
			case Case.GRASSGROUND:
				image = sol[GRASSGROUND];
				break;
				
			case Case.INDESTRUCTIBLE:
				image = sol[INDESTRUCTIBLE];
				break;
				
			case Case.HOUSE:
				image = sol[HOUSE];
				break;
			
			case Case.WOOD:
				image = sol[WOOD];
				break;
			
			case Case.GROUNDGRASSTEXAS:
				image = sol[GROUNDGRASSTEXAS];
				break;
			
			case Case.GROUNDTEXAS:
				image = sol[GROUNDTEXAS];
				break;
				
			case Case.EMPTY:
				image = sol[EMPTY];
				break;
				
			default:
				image = sol[EMPTY];
				break;
			
		}
		return image;
	}
	
	public Case getCase()
	{
		return c;
	}
}
