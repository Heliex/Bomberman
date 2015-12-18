import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Main extends StateBasedGame{

	public static final int MENU = 0;
	public static final int GAME_SOLO = 1;
	public static final int GAME_MULTIPLAYER = 2;
	public static final int OPTIONS = 3;
	public static final int EXIT = 4;
	
	public static final int WIDTH = Game.NB_CASE_LARGEUR * Game.TAILLE_CASE;
	public static final int HEIGHT = Game.NB_CASE_HAUTEUR * Game.TAILLE_CASE;
	public static final int FPS = 60;
	public static double version = 1.0;
	public static String titre = "Bomberman - v" + version;
	
	public Main(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			AppGameContainer app = new AppGameContainer(new Main(titre));
			app.setDisplayMode(WIDTH,HEIGHT, false);
			app.setTargetFrameRate(60);
			app.setShowFPS(false);
			app.start();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		// TODO Auto-generated method stub
		this.addState(new Menu());
		this.addState(new Game());
		this.addState(new Multiplayer());
	}

}
