package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import MainGame.Game;

public class ThreadClient implements Runnable{
	
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Client client;
	
	public ThreadClient(Socket socket,Client client)
	{
		this.socket = socket;
		this.client = client;
		try {
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while(true)
		{
			// Lecture de donnees venant du server
			try {
				Object o = in.readObject();
				if(o instanceof Integer)
				{
					client.setNumClient((int)o);
				}
				else if(o instanceof Game)
				{
					client.setGame((Game)o);
				}
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void sendObject(Object o)
	{
		try
		{
			out.writeObject(o);
			out.flush();
		}catch(IOException io)
		{
			io.printStackTrace();
		}
		
	}
	
	public Socket getSocket()
	{
		return this.socket;
	}
	
	public void setSocket(Socket socket)
	{
		this.socket = socket;
	}
	
	public ObjectInputStream getIn()
	{
		return this.in;
	}
	
	public void setIn(ObjectInputStream in)
	{
		this.in = in;
	}
	
	public ObjectOutputStream getOut()
	{
		return this.out;
	}
	
	public void setOut(ObjectOutputStream out)
	{
		this.out = out;
	}
}
