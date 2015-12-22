import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class ServerBomberman implements ActionListener{
	private  ServerSocket server = null;
	private  JTextField port = null;
	private  int NB_CLIENTS_MAX = 4;
	private  int NB_CLIENTS_CONNECTES = 0;
	
	public ServerBomberman()
	{
		JFrame frame = new JFrame("Configuration du serveur");
		JPanel panel = new JPanel();
		JButton button = new JButton("Create");
		JButton exitButton = new JButton("Stop");
		JLabel label = new JLabel("Sélectionnez le port d'écoute du serveur");
		port = new JTextField(25);
		panel.add(label);
		panel.add(port);
		panel.add(button);
		panel.add(exitButton);
		button.addActionListener(this);
		exitButton.addActionListener(this);
		frame.add(panel);
		frame.setSize(Main.WIDTH,Main.HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
					
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		JButton button =(JButton)e.getSource();
		if(button.getText() == "Create")
		{
			if(server == null)
			{
			
				try {
					server = new ServerSocket(Integer.parseInt(port.getText()));
					Thread t = new Thread(new Test(server));
					t.start();
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}else if(button.getText() == "Stop")
		{
			try {
				server.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	private class Test implements Runnable
	{
		private ServerSocket serverSocket ;
		private Socket socket;
		
		public Test(ServerSocket s)
		{
			this.serverSocket = s;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try
			{
				while(true)
				{
					if(NB_CLIENTS_CONNECTES < NB_CLIENTS_MAX && !serverSocket.isClosed())
					{
						socket = serverSocket.accept();
						System.out.println("Le client numéro : " + NB_CLIENTS_CONNECTES + " est co");
						NB_CLIENTS_CONNECTES++;
						socket.close();
					}
					else
					{
						serverSocket.close();
					}
				}
			}
			catch(IOException io)
			{
				io.printStackTrace();
			}
		}
		
	}
}
