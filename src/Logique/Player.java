package Logique;




public class Player extends LogiqueComponent {
	
	
	// COnstantes
	public final static int UP = 0, LEFT = 1 , DOWN = 2 , RIGHT = 3 ;
	public final static int NB_BOMBE_AT_START = 3, NB_BOMBE_MAX = 5;
	public final static int NB_LIFE_AT_START = 5;
	public final static int VITESSE = 2;
	public final static float COEFF_MIN = 1.5f;
	public static int TAILLE_EXPLOSION = 2;
	
	// variable privées
	private static final long serialVersionUID = 357065401577665717L;
	private int numJoueur;
	private boolean isMoving;
	private int direction;
	private float coeffDeplacement;
	private Bombe[] bombes = new Bombe[NB_BOMBE_MAX];
	
	// Variables
	private int nbBombeAvailable ,nbBombeOnBoard, lifeAvailable, nbBombesPosable;
	
	public Player(int x, int y, boolean isDrawable, int numJoueur, int direction)
	{
		super(x,y,isDrawable);
		this.numJoueur = numJoueur;
		this.direction = direction;
		this.isMoving = false;
		this.nbBombeAvailable=NB_BOMBE_AT_START;
		this.nbBombeOnBoard = 0;
		this.nbBombesPosable = NB_BOMBE_MAX;
		this.lifeAvailable = NB_LIFE_AT_START;
		this.coeffDeplacement = COEFF_MIN;
	}
	
	public String toString()
	{
		return "Position du joueur : X " + this.getX() + " Y : " + this.getY() + " IsMoving ? : " + isMoving ;
	}
	
	
	// Getters & Setters
	public int getNumJoueur()
	{
		return this.numJoueur;
	}
	
	public void setNumJoueur(int n)
	{
		this.numJoueur = n;
	}
	
	public boolean isMoving()
	{
		return this.isMoving;
	}
	
	public void setMoving(boolean move)
	{
		this.isMoving = move;
	}
	
	public int getDirection()
	{
		return this.direction;
	}
	
	public void setDirection(int direction)
	{
		this.direction = direction;
	}
	
	public float getCoeffDeplacement()
	{
		return this.coeffDeplacement;
	}
	
	// A enlever peut-être
	public void setCoeffDeplacement(int coeff)
	{
		
		this.coeffDeplacement = coeff;
		
	}
	public Bombe[] getListeBombes()
	{
		return this.bombes;
	}
	
	public void setListeBombes(Bombe[] newArray)
	{
		this.bombes = newArray;
	}
	
	public int getNBombeAvailable()
	{
		return this.nbBombeAvailable;
	}
	
	public void setNbBombeAvailable(int nb)
	{
		this.nbBombeAvailable = nb;
	}
	
	public int getNbBombeOnBoard()
	{
		return this.nbBombeOnBoard;
	}
	
	public void setNbBombeOnBoard(int nb)
	{
		this.nbBombeOnBoard = nb;
	}
	
	public int getNbLifeAvailable()
	{
		return this.lifeAvailable;
	}
	
	public void setNbLifeAvailable(int nb)
	{
		this.lifeAvailable = nb;
	}
	
	public int getNbBombePosable()
	{
		return this.nbBombesPosable;
	}
	
	public void setNbBombePosable(int nb)
	{
		this.nbBombesPosable = nb;
	}
	
	public boolean equals(Player p)
	{
		boolean equals = true;
		if((getX() != p.getX() && getY() == p.getY()) || (getX() == p.getX() && getY() != p.getY()) || (getX() != p.getX() && getY() == p.getY()))
		{
			equals = false;
		}
		if(isMoving() != p.isMoving())
		{
			equals = false;
		}
		if(getNumJoueur() != p.getNumJoueur())
		{
			equals = false;
		}
		
		return equals;
	}	
}
