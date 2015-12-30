package Network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GestionClient extends Thread{
	
	private Socket socket;
	private boolean isDisplayed = false;
	private BufferedOutputStream out = null;
	private BufferedInputStream in = null;
	
	public GestionClient(Socket socket) throws IOException
	{
		this.socket = socket;
		this.out = new BufferedOutputStream(socket.getOutputStream());
		this.in = new BufferedInputStream(socket.getInputStream());
	}
	
	public void run()
	{
		while(true)
		{
			System.out.println(readMessage());	
		}
	}
	
	
	public void sendMessage(String message) throws IOException
	{
		out.write(message.getBytes());
		out.flush();
	}
	
	public String readMessage()
	{
		String s = null;
		byte[] b = new byte[4096];
		try
		{
			int stream = in.read();
			s = new String(b,0,stream);
		}
		catch(IOException io)
		{
			io.printStackTrace();
		}
		
		return s;
	}
	
	public Socket getSocket()
	{
		return this.socket;
	}

}
