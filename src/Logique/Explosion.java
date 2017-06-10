package Logique;


public class Explosion extends LogiqueComponent{
	
	private static final long serialVersionUID = -1915393897981342327L;
    public final static int TAILLE_EXPLODE_MIN = 2, TAILLE_EXPLODE_MAX = 4;
    
	public Explosion(int x, int y,boolean isDrawable)
	{
		super(x,y,isDrawable);
	}
	
}
