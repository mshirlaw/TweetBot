import java.io.IOException;
import javax.swing.JFrame;
import twitter4j.TwitterException;

/**
 * The main class creates an instances of the 
 * TwitterView GUI class and waits for user input.
 * @author mshirlaw
 *
 */
public class TwitterMain
{
	
	/**
	 * Main method - creates an instance of the main GUI
	 * @param args
	 * @throws TwitterException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException, TwitterException 
	{
		try
		{
			TwitterView tv = new TwitterView();

			tv.setVisible(true);
			tv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			tv.setTitle("Tweetbot");			
			
			//set resize to false
			tv.setResizable(false);
	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
