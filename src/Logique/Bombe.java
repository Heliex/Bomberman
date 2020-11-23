package Logique;



public class Bombe extends LogiqueComponent{
	
	private static final long serialVersionUID = -3677888271498285808L;
	public final static int TIME_TO_EXPLODE = 4000, TAILLE_BOMBE = 16 , TIME_BETWEEN_BOMB_MOVEMENT = 250;
	private int indiceJoueur;
	private long timerStart;
	private boolean isBlocked;
	
	
	public Bombe(float f, float g, boolean isDrawable,int indiceJoueur)
	{
		super(f,g,isDrawable);
		this.indiceJoueur = indiceJoueur;
		this.timerStart = System.currentTimeMillis();
		this.isBlocked = true;
	}
	
	public String toString()
	{
		return "XBomb : " + this.getX() + " YBomb : " + this.getY() ;
	}
	
	
	public int getIndiceJoueur()
	{
		return this.indiceJoueur;
	}
	
	public void setIndiceJoueur(int indice)
	{
		this.indiceJoueur = indice;
	}
	
	public long getTimerStart()
	{
		return this.timerStart;
	}
	
	public boolean isBlocked()
	{
		return this.isBlocked;
	}
	
	public void setBlocked(boolean blocked)
	{
		this.isBlocked = blocked;
	}
}
