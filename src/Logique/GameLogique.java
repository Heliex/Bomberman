package Logique;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

public class GameLogique implements Serializable{


	private static final long serialVersionUID = -6138274667424057646L;
	private Case[][] plateau;
	private Player[] players ;
	private Explosion[][] explosions;
	private long timerAtStart ;
	
	public static final int NB_CASE_LARGEUR = 20, NB_CASE_HAUTEUR = 20, NB_PLAYERS = 4;
	public static int LEVEL = 1;
	
	public static final int offSetCollisionHorizontal = 18, offSetCollisionVertical = 32;
	
	public GameLogique()
	{
		this.plateau = new Case[NB_CASE_HAUTEUR][NB_CASE_LARGEUR];
		this.players = new Player[NB_PLAYERS];
		this.explosions = new Explosion[GameLogique.NB_PLAYERS][Player.NB_BOMBE_MAX];
		// Lecture de fichier pour alimenter les cases.
		timerAtStart = System.currentTimeMillis();
		initLevel("niveaux/niveau" + LEVEL + ".txt");
		initialiserJoueurs();
	}
	
	
	public Case[][] getPlateau()
	{
		return this.plateau;
	}
	
	public void setPlateau(Case[][] newPlateau)
	{
		this.plateau = newPlateau;
	}
	
	public Player[] getPlayers()
	{
		return this.players;
	}
	
	public void setPlayers(Player[] newPlayers)
	{
		this.players = newPlayers;
	}
	
	/**
	 * Permet d'initialiser le niveau avec une chaine de caractères qui le contient sous forme de chiffres.
	 * @param file
	 */
	public void initLevel(String file)
	{
		
		try {
			File f = new File(file);
			int r ;
			BufferedReader reader = new BufferedReader(new FileReader(f));
			for(int i = 0 ; i < NB_CASE_HAUTEUR ; i++)
			{
				for(int j = 0 ; j < NB_CASE_LARGEUR; j++)
				{
					if((r = reader.read()) != -1)
					{
						String typeCase = Case.EMPTY;
						char c = (char)r;
						switch(c)
						{
						case '0':
							typeCase = Case.EMPTY;
							break;
						case '2':
							typeCase = Case.WALL;
							break;
							
						case '3':
							typeCase = Case.INDESTRUCTIBLE;
							break;
						case '8':
							typeCase = Case.DECOR;
							break;
						}
						plateau[i][j] = new Case(j,i,typeCase);
					}
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public long getTimerAtStart()
	{
		return this.timerAtStart;
	}
	
	/**
	 * Initialise les joueurs
	 */
	public void initialiserJoueurs()
	{
		players[0] = new Player(0,0,true,0,Player.RIGHT);
		players[1] = new Player( (NB_CASE_LARGEUR - 1) * Case.TAILLE_CASE,0,true,1,Player.LEFT);
		players[2] = new Player(0,(NB_CASE_HAUTEUR  - 1 )* Case.TAILLE_CASE,true,2,Player.RIGHT);
		players[3] = new Player((NB_CASE_LARGEUR  - 1 )* Case.TAILLE_CASE ,(NB_CASE_HAUTEUR - 1)* Case.TAILLE_CASE,true,3,Player.LEFT);
	}
	
	/**
	 * Place un joueur sur le plateau
	 * @param x
	 * @param y
	 * @param indiceJoueur
	 */
	public void placerJoueur(float x,float y,int indiceJoueur)
	{
		if(players[indiceJoueur] != null)
		{
			players[indiceJoueur].setX(x);
			players[indiceJoueur].setY(y);
		}
	}
	
	/**
	 * Permet de diriger un joueur dans une direction
	 * @param direction
	 * @param indiceJoueur
	 */
	public void dirigerJoueur(int direction,int indiceJoueur)
	{
		if(players[indiceJoueur] != null)
		{
			players[indiceJoueur].setDirection(direction);
		}
	}
	
	/**
	 * Permet de déplacer un joueur dans une direction donnée et avec son coefficient de déplacement
	 * @param indiceJoueur
	 * @param direction
	 */
	public void deplacerJoueur(int indiceJoueur, int direction)
	{
		if(players[indiceJoueur] != null)
		{
			if(peuxBouger(indiceJoueur,direction)) // Test si le joueur peux bouger
			{
				dirigerJoueur(indiceJoueur,direction); // Met le joueur dans la bonne direction
				switch(direction) // Switch qui gère la mise à jour des coordonées du joueur
				{
					case Player.UP:
						placerJoueur((float)players[indiceJoueur].getX(), players[indiceJoueur].getY() - (Player.VITESSE*players[indiceJoueur].getCoeffDeplacement()), indiceJoueur);
					break;
					
					case Player.LEFT:
						placerJoueur(players[indiceJoueur].getX() - (Player.VITESSE*players[indiceJoueur].getCoeffDeplacement()), players[indiceJoueur].getY(), indiceJoueur);
					break;
						
					case Player.DOWN:
						placerJoueur(players[indiceJoueur].getX(), players[indiceJoueur].getY() + (Player.VITESSE*players[indiceJoueur].getCoeffDeplacement()), indiceJoueur);
					break;
					
					case Player.RIGHT:
						placerJoueur(players[indiceJoueur].getX() + (Player.VITESSE*players[indiceJoueur].getCoeffDeplacement()), players[indiceJoueur].getY(), indiceJoueur);
					break;
				}
			}
		}
	}
	
	/**
	 * 
	 * @param indiceJoueur
	 * @param direction
	 * @return un booléen qui dit si le joueur peux se déplacer ou non
	 */
	public boolean peuxBouger(int indiceJoueur,int direction)
	{
		boolean canMove = false;
		Player p = players[indiceJoueur];
		// Ne pas oublier de rajouter le fait que selon la direction appuyée
		// La gestion du déplacement change, et, de fait il faut prendre en compte la direction voulue
		// pour gérer les collisions (Surtout sur le test de coordonnées = MUR)
		// Récuperer le centre des coordonnées du bomberman
		
		// currentPosMiddlePoint
		float currentBombermanHorizontalMiddle = p.getX()  + offSetCollisionHorizontal/2;
		float currentBombermanVerticalMiddle = p.getY() + offSetCollisionVertical/2;
		
		Point middle = new Point(currentBombermanHorizontalMiddle,currentBombermanVerticalMiddle);
		switch(direction)
		{
			case Player.UP:
				// Gestion des collisions vers le haut
				if(		middle.getY() - offSetCollisionVertical/2 -  (p.getCoeffDeplacement()*Player.VITESSE)  >  0 &&
						getCaseFromCoord(middle.getX() - offSetCollisionHorizontal/2 ,middle.getY() - offSetCollisionVertical/2 - (p.getCoeffDeplacement()*Player.VITESSE)).getType() != Case.WALL &&
						getCaseFromCoord(middle.getX() - offSetCollisionHorizontal/2, middle.getY() - offSetCollisionVertical/2 - (p.getCoeffDeplacement()*Player.VITESSE)).getType() != Case.INDESTRUCTIBLE &&
						getCaseFromCoord(middle.getX(), middle.getY() - offSetCollisionVertical/2 - (p.getCoeffDeplacement()*Player.VITESSE)).getType() != Case.WALL &&
						getCaseFromCoord(middle.getX(), middle.getY() - offSetCollisionVertical/2 - (p.getCoeffDeplacement()*Player.VITESSE)).getType() != Case.INDESTRUCTIBLE
				   )
					{
						canMove=true;
					}
			break;
			
			case Player.LEFT:
				// Gestion des collision vers la gauche
				if( middle.getX() - offSetCollisionHorizontal/2 - (p.getCoeffDeplacement()*Player.VITESSE) > 0 &&
					getCaseFromCoord(middle.getX() - offSetCollisionHorizontal/2 - (p.getCoeffDeplacement()*Player.VITESSE) ,middle.getY() - offSetCollisionVertical/2 +1).getType() != Case.WALL &&
					getCaseFromCoord(middle.getX() - offSetCollisionHorizontal/2 - (p.getCoeffDeplacement()*Player.VITESSE) ,middle.getY() - offSetCollisionVertical/2 +1).getType() != Case.INDESTRUCTIBLE &&
					getCaseFromCoord(middle.getX() - offSetCollisionHorizontal/2 - (p.getCoeffDeplacement()*Player.VITESSE) ,middle.getY() + offSetCollisionVertical/2 -1).getType() != Case.WALL &&
					getCaseFromCoord(middle.getX() - offSetCollisionHorizontal/2 - (p.getCoeffDeplacement()*Player.VITESSE) ,middle.getY() + offSetCollisionVertical/2 -1).getType() != Case.INDESTRUCTIBLE)
				{
					canMove = true;
				}
				break;
			
			case Player.DOWN:
				// Gestion des collisions vers le bas
				if(		middle.getY() + offSetCollisionVertical/2 +  (p.getCoeffDeplacement()*Player.VITESSE) < (NB_CASE_HAUTEUR * Case.TAILLE_CASE - 1) &&
						getCaseFromCoord(middle.getX() - offSetCollisionHorizontal/2 ,middle.getY() + offSetCollisionVertical/2 + (p.getCoeffDeplacement()*Player.VITESSE)).getType() != Case.WALL &&
						getCaseFromCoord(middle.getX() - offSetCollisionHorizontal/2, middle.getY() + offSetCollisionVertical/2 + (p.getCoeffDeplacement()*Player.VITESSE)).getType() != Case.INDESTRUCTIBLE &&
						getCaseFromCoord(middle.getX(), middle.getY() + offSetCollisionVertical/2 + (p.getCoeffDeplacement()*Player.VITESSE)).getType() != Case.WALL &&
						getCaseFromCoord(middle.getX(), middle.getY() + offSetCollisionVertical/2 + (p.getCoeffDeplacement()*Player.VITESSE)).getType() != Case.INDESTRUCTIBLE
				   )
					{
						canMove=true;
					}
				break;
		
			case Player.RIGHT:
				// Gestion des collision vers la gauche
				if( middle.getX() + offSetCollisionHorizontal/2 + p.getCoeffDeplacement() < ( NB_CASE_LARGEUR * Case.TAILLE_CASE - 1)  &&
					getCaseFromCoord(middle.getX() + offSetCollisionHorizontal/2 + (p.getCoeffDeplacement()*Player.VITESSE) ,middle.getY() - offSetCollisionVertical/2 +1).getType() != Case.WALL &&
					getCaseFromCoord(middle.getX() + offSetCollisionHorizontal/2 + (p.getCoeffDeplacement()*Player.VITESSE) ,middle.getY() - offSetCollisionVertical/2 +1).getType() != Case.INDESTRUCTIBLE &&
					getCaseFromCoord(middle.getX() + offSetCollisionHorizontal/2 + (p.getCoeffDeplacement()*Player.VITESSE) ,middle.getY() + offSetCollisionVertical/2 -1).getType() != Case.WALL &&
					getCaseFromCoord(middle.getX() + offSetCollisionHorizontal/2 + (p.getCoeffDeplacement()*Player.VITESSE) ,middle.getY() + offSetCollisionVertical/2 -1).getType() != Case.INDESTRUCTIBLE)
				{
					canMove = true;
				}
				break;
			
		}
		
		return canMove;
	}
	
	/**
	 * Permet de poser une bombe pour le joueur 
	 * @param indiceJoueur
	 */
	public void poserBombe(final int indiceJoueur)
	{
		if(players[indiceJoueur].getNBombeAvailable() > 0 && players[indiceJoueur].getNbBombeOnBoard() < players[indiceJoueur].getNbBombePosable()) // Si le joueur dispose d'une bombe et qu'il ne dépasse pas son quota de bombe posable en simultanée
		{
			if(players[indiceJoueur].getListeBombes()[players[indiceJoueur].getNbBombeOnBoard()] == null || !players[indiceJoueur].getListeBombes()[players[indiceJoueur].getNbBombeOnBoard()].isBlocked())
			{
				Bombe b = new Bombe(players[indiceJoueur].getX(),players[indiceJoueur].getY(),true,indiceJoueur);
				players[indiceJoueur].getListeBombes()[players[indiceJoueur].getNbBombeOnBoard()] = b;
				players[indiceJoueur].setNbBombeOnBoard(players[indiceJoueur].getNbBombeOnBoard() + 1);
				players[indiceJoueur].setNbBombeAvailable(players[indiceJoueur].getNBombeAvailable() - 1);
				
			}
		}
	}
	
	public void effacerBombe(final int indiceJoueur,int indiceBombe)
	{

		if(players[indiceJoueur].getListeBombes()[indiceBombe] != null && players[indiceJoueur].getListeBombes()[indiceBombe].isBlocked())
		{
			players[indiceJoueur].getListeBombes()[indiceBombe] = null ;
			players[indiceJoueur].setNbBombeAvailable(players[indiceJoueur].getNBombeAvailable() + 1);
			players[indiceJoueur].setNbBombeOnBoard(players[indiceJoueur].getNbBombeOnBoard() - 1);
				
		}
	}
	
	public void creerExplosion(final int indiceJoueur,int indiceBombe,float x, float y)
	{
		Explosion e = new Explosion(x,y,true,indiceJoueur);
		if(this.explosions[indiceJoueur][indiceBombe] == null)
		{
			this.explosions[indiceJoueur][indiceBombe] = e;
		}	
	}
	
	public void effacerExplosion(final int indiceJoueur,int indiceExplosion)
	{
		if(this.explosions[indiceJoueur][indiceExplosion] != null)
		{
			this.explosions[indiceJoueur][indiceExplosion] = null;
		}
	}
	
	/**
	 * Renvoie une case à partir d'un X et d'un Y réel
	 * @param f
	 * @param g
	 * @return
	 */
	public Case getCaseFromCoord(float f, float g)
	{
		Case c = null;
		for(int i = 0 ; i < GameLogique.NB_CASE_HAUTEUR ; i++)
		{
			for(int j = 0; j < GameLogique.NB_CASE_LARGEUR; j++)
			{
				float xCase = plateau[i][j].getX() * Case.TAILLE_CASE;
				float yCase = plateau[i][j].getY() * Case.TAILLE_CASE;
				
				if(f >= xCase  && f <= xCase+Case.TAILLE_CASE && g >= yCase && g <= yCase + Case.TAILLE_CASE)
				{
					c = plateau[i][j];
					break;
				}
			}
		}
		return c;
	}
	
	public String toString()
	{
		StringBuilder bd = new StringBuilder();
		
		for(int i = 0 ; i < GameLogique.NB_CASE_LARGEUR; i++)
		{
			for(int j = 0 ; j < GameLogique.NB_CASE_HAUTEUR ; j++)
			{
				bd.append(plateau[i][j].toString() + "\n");
			}
		}
		
		for(int i = 0 ; i < GameLogique.NB_PLAYERS ; i++)
		{
			bd.append(players[i].toString() + "\n");
		}
		
		return bd.toString();
		
	}
	
	public boolean equals(GameLogique gl)
	{
		boolean equals = true;
		for(int i = 0 ; i < NB_CASE_HAUTEUR; i++)
		{
			for(int j = 0 ; j < NB_CASE_LARGEUR; j++)
			{
				if(!plateau[i][j].equals(gl.getPlateau()[i][j]))
				{
					equals = false;
					break;
				}
			}
		}
		
		for(int i = 0 ; i < NB_PLAYERS; i++)
		{
			if(!players[i].equals(gl.getPlayers()[i]))
			{
				equals = false;
				break;
			}
		}
		
		return equals;
	}
	
	public Explosion[][] getExplosions()
	{
		return this.explosions;
	}
	
	public void setExplosions(Explosion[][] explosion)
	{
		this.explosions = explosion;
	}

}
