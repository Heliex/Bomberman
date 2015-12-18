import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Multiplayer extends BasicGameState{

	private Socket client;
	private BufferedInputStream inputStream;
	private BufferedOutputStream outputStream;
	String commande="";
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		client = null;
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
		// TODO Auto-generated method stub
		if(client == null)
		{
			JFrame frame = new JFrame("Connexion au serveur en ligne");
			frame.setSize(Main.WIDTH, Main.HEIGHT);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JPanel panel = new JPanel();
			JTextField host = new JTextField(25);
			JTextField port = new JTextField(25);
			JButton button = new JButton("Connexion au serveur");
			button.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					if(arg0.getButton() == MouseEvent.BUTTON1)
					{
						Thread t = new Thread(new Runnable(){
							
							public void run()
							{
								try {
									client = new Socket(host.getText(),Integer.parseInt(port.getText()));
									inputStream = new BufferedInputStream(client.getInputStream());
									outputStream = new BufferedOutputStream(client.getOutputStream());
									frame.dispose();
								} catch (NumberFormatException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (UnknownHostException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
						t.start();
						
					}
				}
				
				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseClicked(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			panel.add(button);
			panel.add(host);
			panel.add(port);
			frame.add(panel);
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
	
	
	@Override
	public void keyPressed(int key, char c)
	{
		switch(key)
		{
			case Input.KEY_UP : 
				commande = "UP"; 
			try {
				outputStream.write(commande.getBytes());
				outputStream.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			break;
			
			case Input.KEY_ESCAPE:
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		
		}
	}

}
