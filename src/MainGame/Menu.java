package MainGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Menu extends BasicGameState implements KeyListener{

	private StateBasedGame game;
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		this.game = arg1;
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		g.setColor(Color.white);
		g.drawString("Bomberman - Explode Everywhere", 50, 10);
		g.drawString("1. Start/Load game", 50, 100);
		g.drawString("2. Multiplayer Game / Internet", 50, 120);
		g.drawString("3. Multiplayer Game / Local", 50, 140);
		g.drawString("3. Options", 50, 160);
		g.drawString("4. Rules", 50, 180);
		g.drawString("5. Exit", 50, 200);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void keyReleased(int key, char c) {
	    switch(key) {
	    case Input.KEY_1:
	    case Input.KEY_NUMPAD1:
	        game.enterState(Main.GAME_SOLO);
	        break;
	    case Input.KEY_2:
	    case Input.KEY_NUMPAD2:
	        // TODO: Implement later
	    	//game.enterState(Main.GAME_MULTIPLAYER);
	        break;
	    case Input.KEY_3:
	    case Input.KEY_NUMPAD3:
	        // TODO: Implement later
	        break;
	        
	    case Input.KEY_4:
	    case Input.KEY_NUMPAD4:
	    	break;
	    
	    case Input.KEY_5:
	    case Input.KEY_NUMPAD5:
	    	System.exit(0);
	    default:
	        break;
	    }
	}
}
