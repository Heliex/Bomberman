package Logique;


public class Bonus extends LogiqueComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2494919354680379842L;
	
	public final static int TIME_TO_BONUS_APPEAR = 10000, TIME_BEFORE_DISSAPEAR = 30000;
	public final static String BOMBADD = "Bombe Suppl�mentaire",SPEEDUP="Vitesse sup�rieur",MOVEBOMB="Bombe d�placable";
	public final static String BOXE = "Gant de boxe",EXPLODEMORE="Explosion plus longue", DEAD="Bombe plus efficiente",EXPLODELESS="Explosion diminu�e";
	public final static String BOMBLESS = "Bombe en moins", SPEEDLESS = "Vitesse inf�rieure", STOPBOMB = "Bombe non d�pla�able";
	public final static String UNBOXE = "Fin du bonus de boxe", LOWDMG = "Bombe moins efficiente", EXPLODELESSEFFICIENT = "Explosion efficiente";
	
	public Bonus(int x , int y, boolean isDrawable)
	{
		super(x,y,isDrawable);
	}
	
	public String toString()
	{
		return "X : " + this.getX() + " Y : " + this.getY() ;
	}
	
}
