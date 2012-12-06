import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import javax.swing.JOptionPane;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * 
 * @author mshirlaw
 *
 */
public class TwitterManager 
{
	//private instance fields
	private Twitter twitter;
	private final String CONSUMER_KEY = "[YOUR CONSUMER_KEY GOES HERE]";
	private final String CONSUMER_SECRET = "[YOUR CONSUMER_SECRET GOES HERE]";	
	private PrintWriter writer;
	private BufferedReader reader;
	
	/**
	 * Constructor creates a reader and writer object
	 * to allow persistence of user credentials
	 * @throws IOException
	 */
	
	public TwitterManager() throws IOException
	{
		twitter = null;
		writer = new PrintWriter(new FileWriter("keyFile",true));
		reader = new BufferedReader(new FileReader("keyFile"));
	}
	
	/**
	 * The authorise method 
	 * @throws TwitterException
	 * @throws IOException
	 */
	
	public void authorise() throws TwitterException, IOException
	{
		String key = reader.readLine();
		
		//test if the credential's exist in the keyFile
		if(key != null)
		{
			TwitterFactory factory = new TwitterFactory();
		    AccessToken accessToken = new AccessToken(key, reader.readLine());

		    //System.out.println(accessToken.getToken());
		    //System.out.println(accessToken.getTokenSecret());
		    
		    twitter = factory.getInstance();
		    twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		    twitter.setOAuthAccessToken(accessToken);

		    reader.close();
		}
		else
		{
				
			// The factory instance is re-useable and thread safe.
			twitter = TwitterFactory.getSingleton();
			twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
			RequestToken requestToken = twitter.getOAuthRequestToken();
			AccessToken accessToken = null;
			
			while (accessToken == null) 
			{
				Desktop.getDesktop().browse(URI.create(requestToken.getAuthorizationURL()));
				String pin = JOptionPane.showInputDialog(null,"Enter the PIN to authorise your account [PIN]:");
				try
				{
					if(pin.length() > 0)
					{
						accessToken = twitter.getOAuthAccessToken(requestToken, pin);
					}
					else
					{
						accessToken = twitter.getOAuthAccessToken();
					}
				} 
				catch (TwitterException te) 
				{
					if(te.getStatusCode() == 401)
					{
						System.out.println("Unable to get the access token.");
					}
					else
					{
						te.printStackTrace();
					}
				}
			}
			//persist to the accessToken for future reference.
			storeAccessToken(twitter.verifyCredentials().getId() , accessToken);
		}
	}
	
	/**
	 * 
	 * @param theStatus
	 * @throws TwitterException
	 */
	public void tweet(String theStatus) throws TwitterException
	{
	    Status status = twitter.updateStatus(theStatus);
	    System.out.println("Successfully updated the status to [" + status.getText() + "].");
	}
	
	/**
	 * 
	 * @param l
	 * @param accessToken
	 * @throws IOException
	 */
	
	private void storeAccessToken(long l, AccessToken accessToken) throws IOException
	{
	    //store accessToken.getToken() on line 1
		writer.write(accessToken.getToken());
		writer.write("\n"); 
		//store accessToken.getTokenSecret() on line 2	
		writer.write(accessToken.getTokenSecret());
		
		writer.close();
	}	
	
}
