import java.io.FileInputStream;

import javazoom.jl.player.advanced.AdvancedPlayer;


public class BombermanAudioPlayer extends Thread{

	private String fileLocation;
	private AdvancedPlayer player;
	
	public BombermanAudioPlayer(String fileName)
	{
		this.fileLocation = fileName;
	}
	
	public void run()
	{
		try
		{
			FileInputStream buff = new FileInputStream(fileLocation);
			player = new AdvancedPlayer(buff);
			player.play();
		}
		catch(Exception io)
		{
			io.printStackTrace();
		}
	}
	
	public void close()
	{
		player.close();
		this.interrupt();
	}
	
	public AdvancedPlayer getPlayer()
	{
		return this.player;
	}
}
