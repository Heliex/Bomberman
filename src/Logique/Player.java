package Logique;




public class Player extends LogiqueComponent {
	
	// variable privées
	private static final long serialVersionUID = 357065401577665717L;
	private int numJoueur;
	private boolean isMoving;
	private int direction;
	private int coeff_deplacement = 3;
	private Bombe[] bombes = new Bombe[NB_BOMBE_MAX];
	
	// COnstantes
	public final static int UP = 0, LEFT = 1 , DOWN = 2 , RIGHT = 3 ;
	public final static int NB_BOMBE_AT_START = 1, NB_BOMBE_MAX = 5;
	public final static float COEFF_MAX = 0.25f, COEFF_MIN = 0.10f;
	public static int TAILLE_EXPLOSION = 2;
	
	// Variables
	private int nbBombeAvailable ,nbBombeOnBoard, lifeAvailable, nbBombesPosable;
	
	public Player(int x, int y, boolean isDrawable, int numJoueur, int direction)
	{
		super(x,y,isDrawable);
		this.numJoueur = numJoueur;
		this.direction = direction;
		this.isMoving = false;
		this.nbBombeAvailable=1;
		this.nbBombeOnBoard = 0;
		this.nbBombesPosable = 2;
		this.lifeAvailable = 5;
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
	
	public int getCoeffDeplacement()
	{
		return this.coeff_deplacement;
	}
	
	// A enlever peut-être
	public void setCoeffDeplacement(int coeff)
	{
		
		this.coeff_deplacement = coeff;
		
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
