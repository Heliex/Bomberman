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
	private long timerAtStart ;
	
	public static final int NB_CASE_LARGEUR = 20, NB_CASE_HAUTEUR = 20, NB_PLAYERS = 4;
	public static int LEVEL = 1;
	
	public GameLogique()
	{
		this.plateau = new Case[NB_CASE_HAUTEUR][NB_CASE_LARGEUR];
		this.players = new Player[NB_PLAYERS];
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
		players[3] = new Player((NB_CASE_LARGEUR  - 1 )* Case.TAILLE_CASE,(NB_CASE_HAUTEUR - 1)* Case.TAILLE_CASE,true,3,Player.LEFT);
	}
	
	/**
	 * Place un joueur sur le plateau
	 * @param x
	 * @param y
	 * @param indiceJoueur
	 */
	public void placerJoueur(int x,int y,int indiceJoueur)
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
						placerJoueur(players[indiceJoueur].getX(), players[indiceJoueur].getY() - players[indiceJoueur].getCoeffDeplacement(), indiceJoueur);
					break;
					
					case Player.LEFT:
						placerJoueur(players[indiceJoueur].getX() - players[indiceJoueur].getCoeffDeplacement(), players[indiceJoueur].getY(), indiceJoueur);
					break;
						
					case Player.DOWN:
						placerJoueur(players[indiceJoueur].getX(), players[indiceJoueur].getY() + players[indiceJoueur].getCoeffDeplacement(), indiceJoueur);
					break;
					
					case Player.RIGHT:
						placerJoueur(players[indiceJoueur].getX() + players[indiceJoueur].getCoeffDeplacement(), players[indiceJoueur].getY(), indiceJoueur);
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
		switch(direction)
		{
		// TODO : Implémenter la gestion correcte des collissions.
			case Player.UP:
				if(p.getY() - p.getCoeffDeplacement() >= 0 && getCaseFromCoord(p.getX(), p.getY() - p.getCoeffDeplacement()) != null && getCaseFromCoord(p.getX(),p.getY() - p.getCoeffDeplacement()).getType() != Case.WALL) // && la position voulue n'est pas un mur)
				{
					canMove = true;
				}
			break;
			
			case Player.LEFT:
				if(p.getX() - p.getCoeffDeplacement() >= 0 && getCaseFromCoord(p.getX() - p.getCoeffDeplacement(), p.getY()) != null && getCaseFromCoord(p.getX() - p.getCoeffDeplacement(), p.getY()).getType() != Case.WALL && getCaseFromCoord(p.getX() - p.getCoeffDeplacement(), p.getY()).getType() != Case.INDESTRUCTIBLE ) // && la position voulue n'est pas un mur
				{
					canMove= true;
				}
				break;
			
			case Player.DOWN:
				if(p.getY() +p.getCoeffDeplacement() < (GameLogique.NB_CASE_HAUTEUR - 1) * Case.TAILLE_CASE && getCaseFromCoord(p.getX(), p.getY() + p.getCoeffDeplacement()) != null && getCaseFromCoord(p.getX(), p.getY() + p.getCoeffDeplacement()).getType() != Case.WALL && getCaseFromCoord(p.getX(), p.getY() + p.getCoeffDeplacement()).getType() != Case.INDESTRUCTIBLE) // && la position voulue n'est pas un mur
				{	
					canMove= true;
				}
				break;
		
			case Player.RIGHT:
				if(p.getX() + p.getCoeffDeplacement() < (GameLogique.NB_CASE_LARGEUR - 1) * Case.TAILLE_CASE && getCaseFromCoord(p.getX() + p.getCoeffDeplacement(),p.getY()) != null && getCaseFromCoord(p.getX() + p.getCoeffDeplacement(), p.getY() ).getType() != Case.WALL && getCaseFromCoord(p.getX() + p.getCoeffDeplacement(), p.getY()).getType() != Case.INDESTRUCTIBLE) // && la position voulue n'est pas un mur
				{
					canMove= true;
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
			players[indiceJoueur].getListeBombes()[players[indiceJoueur].getNbBombeOnBoard()] = new Bombe(players[indiceJoueur].getX(),players[indiceJoueur].getY(),true);
			players[indiceJoueur].getListeBombes()[players[indiceJoueur].getNbBombeOnBoard()].startTimer();
			players[indiceJoueur].setNbBombeAvailable(players[indiceJoueur].getNBombeAvailable() - 1);
			players[indiceJoueur].setNbBombeOnBoard(players[indiceJoueur].getNbBombeOnBoard() + 1);
		}
	}
	
	/**
	 * Permet de retirer un bombe liée au joueur
	 * @param indiceJoueur
	 */
	public void retirerBombe(int indiceJoueur)
	{
		if(players[indiceJoueur] != null)
		{
			players[indiceJoueur].getListeBombes()[players[indiceJoueur].getNbBombeOnBoard() - 1 ] = null;
			players[indiceJoueur].setNbBombeAvailable(players[indiceJoueur].getNBombeAvailable() + 1);
			players[indiceJoueur].setNbBombeOnBoard(players[indiceJoueur].getNbBombeOnBoard() - 1);
		}
	}
	
	/**
	 * Renvoie une case à partir d'un X et d'un Y réel
	 * @param x
	 * @param y
	 * @return
	 */
	public Case getCaseFromCoord(int x, int y)
	{
		Case c = null;
		for(int i = 0 ; i < GameLogique.NB_CASE_HAUTEUR ; i++)
		{
			for(int j = 0; j < GameLogique.NB_CASE_LARGEUR; j++)
			{
				int xCase = plateau[i][j].getX() * Case.TAILLE_CASE;
				int yCase = plateau[i][j].getY() * Case.TAILLE_CASE;
				
				if(x >= xCase  && x <= xCase+Case.TAILLE_CASE && y >= yCase && y <= yCase + Case.TAILLE_CASE)
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
	

}
