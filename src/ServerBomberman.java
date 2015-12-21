import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class ServerBomberman implements ActionListener{
	private static ServerSocket server = null;
	private JTextField port = null;
	private Thread threadServer = null;
	private static int NB_CLIENTS_MAX = 4;
	private static int NB_CLIENTS_CONNECTES = 0;
	private static Socket[] clients = new Socket[NB_CLIENTS_MAX];

	public static void main(String[] args)
	{
		// Set the server
		new ServerBomberman();	
	}
	
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
	
	public static void serveurGo()
	{
		while(!server.isClosed())
		{
			try
			{
				clients[NB_CLIENTS_CONNECTES] = server.accept();
				NB_CLIENTS_CONNECTES++;

			}
			catch(IOException io)
			{
				io.printStackTrace();
			}
		}
		
		System.out.println("Le serveur va se fermer");
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		JButton button =(JButton)e.getSource();
		if(button.getText() == "Create")
		{
			if(server == null)
			{
				threadServer = new Thread(){
					public void run()
					{
						try {
							server = new ServerSocket(Integer.parseInt(port.getText()));
							System.out.println("LE SERVEUR VA DEMARRE");
							serveurGo();
							
						} catch (NumberFormatException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				};
				threadServer.start();
				
				
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
	
	public static void sendToAllClient(String message) throws IOException
	{
		OutputStream stream ;
		for(int i = 0 ; i < clients.length; i++)
		{
			Socket socket = clients[i];
			stream = socket.getOutputStream();
			byte[] b = message.getBytes();
			stream.write(b);
			stream.flush();
			
		}
	}
}
