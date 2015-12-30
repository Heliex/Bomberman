package Network;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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

import MainGame.Main;

public class ConnexionToServer extends BasicGameState implements ActionListener{
	
	JFrame frame = null;
	JPanel panel = null;
	JTextField host = null,port = null;
	JLabel hostLabel = null, portLabel = null;
	JButton connect = null;
	ClientBomberman client = null;
	boolean estAffiche = true;
	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		// TODO Auto-generated method stub
		frame = new JFrame("Connexion au serveur");
		frame.setSize(Main.WIDTH, Main.HEIGHT);
		panel = new JPanel();
		connect = new JButton("Connexion");
		hostLabel = new JLabel("Adresse IP/Nom du serveur :");
		portLabel = new JLabel("Port du serveur :");
		host = new JTextField(20);
		port = new JTextField(20);
		panel.add(hostLabel);
		panel.add(host);
		panel.add(portLabel);
		panel.add(port);
		panel.add(connect);
		frame.add(panel);
		connect.addActionListener(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		if(estAffiche)
		frame.setVisible(true);
		if(client != null)
		{
			//System.out.println(client.readMessage());
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(((JButton)e.getSource()).getText() == "Connexion")
		{
			estAffiche = false;
			frame.setVisible(false);
			client = new ClientBomberman(host.getText(), Integer.parseInt(port.getText()));
		}
	}
	
	boolean isVisible()
	{
		return frame.isVisible();
	}
	
	@Override
	public void keyPressed(int key, char c)
	{
		String commande = null;
		switch(key)
		{
		case Input.KEY_UP:
			commande = "UP";
			break;
		}
		
		try {
			client.sendMessage(commande);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
