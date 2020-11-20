package Logique;



public class Bombe extends LogiqueComponent{
	
	private static final long serialVersionUID = -3677888271498285808L;
	private static int indiceBombe = 0;
	public final static int TIME_TO_EXPLODE = 4000, TAILLE_BOMBE = 16 , TIME_BETWEEN_BOMB_MOVEMENT = 250;
	private int indiceJoueur;
	private int indiceInterne;
	private long timerStart;
	
	public Bombe(float f, float g, boolean isDrawable,int indiceJoueur)
	{
		super(f,g,isDrawable);
		this.indiceJoueur = indiceJoueur;
		this.indiceInterne = indiceBombe;
		this.timerStart = System.currentTimeMillis();
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
	
	public static void augmenterIndice()
	{
		indiceBombe++;
	}
	
	public static void diminuerIndice()
	{
		indiceBombe--;
	}
	
	public static int getIndice()
	{
		return indiceBombe;
	}
	
	public int getIndiceInterne()
	{
		return this.indiceInterne;
	}
	
	public void setIndiceInterne(int indice)
	{
		this.indiceInterne = indice;
	}
	
	public long getTimerStart()
	{
		return this.timerStart;
	}
}
