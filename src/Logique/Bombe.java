package Logique;



public class Bombe extends LogiqueComponent{
	
	private static final long serialVersionUID = -3677888271498285808L;
	public final static int TIME_TO_EXPLODE = 4000, TAILLE_BOMBE = 16 , TIME_BETWEEN_BOMB_MOVEMENT = 250;
	private Explosion explosion;
	private long startTimer ;
	
	public Bombe(int x, int y, boolean isDrawable)
	{
		super(x,y,isDrawable);
		this.startTimer = 0;
		this.explosion = new Explosion(this.getX(),this.getY(),false);
	}
	
	public String toString()
	{
		return "XBomb : " + this.getX() + " YBomb : " + this.getY() ;
	}
	
	public Explosion getExplosion()
	{
		return this.explosion;
	}
	
	public long getStartTimer()
	{
		return this.startTimer;
	}
	
	public long getTempsEcoule()
	{
		return System.currentTimeMillis() - startTimer;
	}
	
	public void startTimer()
	{
		this.startTimer = System.currentTimeMillis();
	}
}
