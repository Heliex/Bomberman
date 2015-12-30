package Network;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import Graphique.Bomb;
import Graphique.Player;
import MainGame.Game;

public class Multiplayer extends BasicGameState implements ActionListener{
	ClientBomberman client = null;
	Player[] players = null;
	Game currentGame = null;
	String commande = null;
	JTextField host,port;
	JFrame frame;
	int numPlayer;
	boolean isStarted = false, isInit = false;
	long TIMER_START = 0 , TIMER_EXECUTION = 0, TICK = 10000;
	public Multiplayer()
	{
		
		
	}
	@Override
	public void init(GameContainer gc, StateBasedGame arg1) throws SlickException {
	}
	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		if(client == null)
		{
			frame = new JFrame("Configuration du serveur");
			JPanel panel = new JPanel();
			JButton button = new JButton("Connexion");
			JLabel label = new JLabel("Adresse IP/Nom du serveur :");
			JLabel label2 = new JLabel("Port :");
			host = new JTextField(25);
			port = new JTextField(25);
			panel.add(label);
			panel.add(host);
			panel.add(label2);
			panel.add(port);
			panel.add(button);
			button.addActionListener(this);
			frame.add(panel);
			frame.setSize(800, 600);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}
		else if(currentGame != null && !isInit)
		{
			System.out.println("Je passe ici");
			currentGame.init(gc, game);
			TIMER_START = Bomb.getTime();
			isInit = true;
		}
		else if(currentGame != null)
		{
			currentGame.render(gc, game, g);
		}
		
		if(TIMER_EXECUTION - TIMER_START > TICK && currentGame != null && isInit)
		{
			client.writeObject(currentGame);
			TIMER_START = Bomb.getTime();
			currentGame = (Game)client.readObject();
		}
		TIMER_EXECUTION = Bomb.getTime();
	}
	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		if(currentGame != null && isInit)
			currentGame.update(arg0, arg1, arg2);
		
	}
	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 2;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(((JButton)e.getSource()).getText().equals("Connexion"))
		{
			client = new ClientBomberman(host.getText(),Integer.parseInt(port.getText()));
			currentGame = (Game)client.readObject();
			frame.setVisible(false);
		}
	}
	
	@Override
	public void keyPressed(int key, char c)
	{
		switch(key)
		{
		case Input.KEY_UP:
			commande = "UP";
			break;
		
		case Input.KEY_DOWN:
			commande = "DOWN";
			break;
		case Input.KEY_LEFT:
			commande = "LEFT";
			break;
		case Input.KEY_RIGHT:
			commande = "RIGHT";
			break;
		case Input.KEY_B:
			commande = "BOMBE";
			break;
		}
		if(commande != null && client != null)
		{
			//client.writeString(commande);
		}
	}
	
	@Override
	public void keyReleased(int key,char c)
	{
		//client.writeString("STOP");
	}
}
