import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BombermanServeur {
	
	private int port;
	private ServerSocket serveur ;
	public static int NB_CLIENT = 0, NB_CLIENT_MAX = 4;
	private Player players[] = new Player[NB_CLIENT_MAX];
	BufferedInputStream reader ;
	BufferedOutputStream writer;
	public static void main(String[] args)
	{
		new BombermanServeur();
	}
	
	
	public BombermanServeur()
	{
		this.port = 4444;
		
			try {
				this.serveur = new ServerSocket(port);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while(!serveur.isClosed())
			{
				Thread t  = new Thread(new Runnable(){
					
					public void run()
					{
						try {
							Socket client = serveur.accept();
							reader = new BufferedInputStream(client.getInputStream());
							writer = new BufferedOutputStream(client.getOutputStream());
							if(NB_CLIENT < NB_CLIENT_MAX)
							{
								System.out.println(getMessage());
								NB_CLIENT++;
								System.out.println("Vous êtes les client n° : " + NB_CLIENT);
							}
							if(NB_CLIENT == NB_CLIENT_MAX)
							{
								System.out.println("Le serveur va s'arrêter");
								serveur.close();
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					}
				});
				t.start();
			}
	}
	
	public String getMessage() throws IOException
	{
		byte[] b = new byte[4096];
		int stream = reader.read(b);
		String s = new String(b,0,stream);
		return s;
	}
	
}
