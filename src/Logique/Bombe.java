package Logique;



public class Bombe extends LogiqueComponent implements Runnable{
	
	private static final long serialVersionUID = -3677888271498285808L;
	public final static int TIME_TO_EXPLODE = 4000, TAILLE_BOMBE = 16 , TIME_BETWEEN_BOMB_MOVEMENT = 250;
	private Explosion explosion;
	private Player player;
	private int indiceBombe;
	
	public Bombe(float f, float g, boolean isDrawable, Player player, int indiceBombe)
	{
		super(f,g,isDrawable);
		this.indiceBombe = indiceBombe;
		this.player = player;
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
	
	public Player getPlayer()
	{
		return this.player;
	}
	
	public int getIndiceBombe()
	{
		return this.indiceBombe;
	}
	
	@Override
	synchronized public void  run() {
		
		long timerStart = System.currentTimeMillis();
		while(true)
		{
			if(System.currentTimeMillis() - timerStart > TIME_TO_EXPLODE)
			{
				this.setDrawable(false);
				player.setNbBombeAvailable(player.getNBombeAvailable() + 1);
				player.setNbBombeOnBoard(player.getNbBombeOnBoard() - 1);
				player.getListeBombes()[indiceBombe] = null;
				break;
				
			}
		}
		
	}
}
