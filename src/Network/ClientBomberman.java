package Network;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientBomberman {
	
	private Socket socket;
	private BufferedInputStream reader;
	private BufferedOutputStream output;
	private int port;
	private String host;
	
	public ClientBomberman(String host, int port)
	{
		this.host = host;
		this.port = port;
		try {
			this.socket = new Socket(host,port);
			this.reader = new BufferedInputStream(socket.getInputStream());
			this.output = new BufferedOutputStream(socket.getOutputStream());
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	public Socket getSocket()
	{
		return this.socket;
	}
	
	public void setSocket(Socket s)
	{
		this.socket = s;
	}
	
	public String readMessage()
	{
		String s = null;
		byte[] b = new byte[4096];
		try {
			int stream = reader.read(b);
			s = new String(b,0,stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	
	public void sendMessage(String message) throws IOException
	{
		output.write(message.getBytes());
		output.flush();
	}
}
