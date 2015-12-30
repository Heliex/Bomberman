package Network;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientBomberman {
	
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private int port;
	private String host;
	
	public ClientBomberman(String host, int port)
	{
		this.host = host;
		this.port = port;
		try {
			this.socket = new Socket(host,port);
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeObject(Object obj)
	{
		try
		{
			out.writeObject(obj);
			out.flush();
		}
		catch(IOException io)
		{
			io.printStackTrace();
		}
	}
	public Object readObject()
	{
		Object o = null;
		try
		{
			o = in.readObject();
		}
		catch(IOException io)
		{
			io.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return o;
	}
	
	public int getPort()
	{
		return this.port;
	}
	
	public void setPort(int port)
	{
		this.port = port;
	}
	
	public String getHost()
	{
		return this.host;
	}
	
	public void setHost(String host)
	{
		this.host = host;
	}
}
