import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Main{

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			AppGameContainer app = new AppGameContainer(new Game("Bomberman"));
			app.setDisplayMode(Game.NB_CASE_LARGEUR * Game.TAILLE_CASE, Game.NB_CASE_HAUTEUR * Game.TAILLE_CASE, false);
			app.setTargetFrameRate(60);
			app.start();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
