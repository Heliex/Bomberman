package Logique;


public class Explosion extends LogiqueComponent{
	
	private static final long serialVersionUID = -1915393897981342327L;
    public final static int TAILLE_EXPLODE_MIN = 2, TAILLE_EXPLODE_MAX = 4, TIME_TO_DRAW=1000;
    private int indiceJoueur;
    private long timerStart;
    
	public Explosion(float f, float g,boolean isDrawable,int indiceJoueur)
	{
		super(f,g,isDrawable);
		this.indiceJoueur = indiceJoueur;
		this.timerStart = System.currentTimeMillis();
	}
	
	public int getIndiceJoueur()
	{
		return this.indiceJoueur;
	}
	
	public void setIndiceJoueur(int indiceJoueur)
	{
		this.indiceJoueur = indiceJoueur;
	}
	
	public long getTimerStart()
	{
		return this.timerStart;
	}
	
	public void setTimerStart(long timer)
	{
		this.timerStart = timer;
	}
	
}
