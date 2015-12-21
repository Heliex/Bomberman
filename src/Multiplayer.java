import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Multiplayer extends BasicGameState implements ActionListener{
	private String host;
	private int port;
	JTextField hostField,portField;
	private Socket client = null;
	Thread threadClient = null;
	BufferedInputStream reader = null;
	
	public Multiplayer()
	{
	}
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
		// TODO Auto-generated method stub
		if(client == null)
		{
			JFrame frame = new JFrame("Configuration de la connexion au serveur");
			JPanel panel = new JPanel();
			JLabel hostLabel = new JLabel("Adresse IP/ Nom du serveur");	
			hostField = new JTextField(15);
			JLabel portLabel = new JLabel("Port");
			portField = new JTextField(15);
			JButton button = new JButton("Connexion");
			button.addActionListener(this);
			panel.add(hostLabel);
			panel.add(hostField);
			panel.add(portLabel);
			panel.add(portField);
			panel.add(button);
			frame.add(panel);
			frame.setSize(Main.WIDTH, Main.HEIGHT);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			
		}
	}
	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 2;
	}
	
	
	public String read()
	{
		String s = null;
		if(client != null)
		{
			byte[] b = new byte[4096];
			try {
				reader = new BufferedInputStream(client.getInputStream());
				int stream = reader.read();
				s = new String(b,0,stream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return s;
	}
	
	public String getHost()
	{
		return this.host;
	}
	
	public int getPort()
	{
		return this.port;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton button = (JButton)e.getSource();
		if(button.getText() == "Connexion")
		{
			try
			{
				host = hostField.getText();
				port = Integer.parseInt(portField.getText());
				client = new Socket(host,port);
			}
			catch(IOException io)
			{
				io.printStackTrace();
			}
		}
	}
}
