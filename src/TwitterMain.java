import java.io.IOException;
import twitter4j.TwitterException;

/**
 * 
 * @author mshirlaw
 *
 */
public class TwitterMain 
{
	/**
	 * 
	 * @param args
	 * @throws TwitterException
	 * @throws IOException
	 */
	public static void main(String[] args) throws TwitterException, IOException 
	{
		TwitterView tv = new TwitterView();
		tv.setVisible(true);
	}
}