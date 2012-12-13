import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.List;

import javax.swing.JOptionPane;

import twitter4j.DirectMessage;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * This class controls access to a users twitter
 * account and will eventually contain methods for 
 * reading and accessing a users timeline, direct messages and @ replies as
 * well as proving methods to allow the user to update their twitter from within
 * the application 
 * @author mshirlaw
 * @date 13 December 2012
 */
public class TwitterManager 
{
	//private instance fields
	private Twitter twitter;
	private final String CONSUMER_KEY = "HERE";
	private final String CONSUMER_SECRET = "HERE";	
	private PrintWriter writer;
	private BufferedReader reader;
	private AccessToken accessToken;
	private User user;
	
	/**
	 * Constructor creates a reader and writer object
	 * to allow persistence of user credentials
	 * @throws IOException
	 */
	
	public TwitterManager() throws IOException
	{
		writer = new PrintWriter(new FileWriter("keyFile",true));
		reader = new BufferedReader(new FileReader("keyFile"));
	}
	
	/**
	 * The authorise method either asks a user to allow access to 
	 * their twitter account or loads an existing CONSUMER_KEY and CONSUMER_SECRET
	 * pair from a file called keyFile 
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
		    accessToken = new AccessToken(key, reader.readLine());

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
			storeAccessToken(accessToken);
		}
		
		//create a reference to the authorised user
		user = twitter.verifyCredentials();
		
		// print the user's screen name - for debugging
		//System.out.println(user.getScreenName());
	}
	
	/**
	 * The tweet method accepts a string and posts this
	 * string to the user's twitter timeline. A message is posted to the 
	 * console to confirm that the status has been updated. 
	 * @param theStatus The string to be posted to twitter
	 * @throws TwitterException
	 */
	public void tweet(String theStatus) throws TwitterException
	{
	    Status status = twitter.updateStatus(theStatus);
	    System.out.println("Successfully updated the status to [" + status.getText() + "].");
	}
	
	/**
	 * The storeAccessToken stores a CONSUMER_KEY and CONSUMER_SECRET in a 
	 * file so that authorisation is not required every time the user wishes
	 * to use the app
	 * @param accessToken The token containing the users CONSUMER_KEY and CONSUMER_SECRET
	 * @throws IOException
	 */
	
	private void storeAccessToken(AccessToken accessToken) throws IOException
	{
	    //store accessToken.getToken() on line 1
		writer.write(accessToken.getToken());
		writer.write("\n"); 
		//store accessToken.getTokenSecret() on line 2	
		writer.write(accessToken.getTokenSecret());
		
		writer.close();
	}	
	
	/**
	 * The getTimeLine method is used to access the user's timeline
	 * Incomplete implementation. Only available for testing
	 * @throws TwitterException 
	 */
	public List<Status> getTimeLine() throws TwitterException
	{		
	    //create a list of the statuses
	    List<Status> statuses = twitter.getHomeTimeline();

	    return statuses;   
	}
	
	/**
	 * The getAtTimeLine method is used to access the last 20
	 * mentions for the authenticating user
	 * @return mentions
	 * @throws TwitterException 
	 */
	public ResponseList<Status> getAtTimeLine() throws TwitterException
	{		
		ResponseList<Status> mentions = null;
		
	    //create a list of the mentions
	    try
	    {
	    	mentions = twitter.getMentionsTimeline();
	    }
	    catch(TwitterException te)
	    {
	    	System.out.println("Failed to get the list of replies");
			te.printStackTrace();
	    }

	    return mentions;   
	}	
	
	/**
	 * The getMessages method is used to access the last 20
	 * mentions for the authenticating user
	 * @return messages
	 * @throws TwitterException 
	 */
	public ResponseList<DirectMessage> getMessages() throws TwitterException
	{		
		ResponseList<DirectMessage> messages = null;
	    
		//create a list of the direct messages
	    try
	    {
	    	messages = twitter.getDirectMessages();
	    }
	    catch(TwitterException te)
	    {
	    	System.out.println("Failed to get the list of direct messages");
			te.printStackTrace();
	    }

	    return messages;   
	}	

	/**
	 * Getter method to return a reference to an 
	 * object which represents the current user
	 * @return user The authenticated user
	 */
	public User getUser() 
	{
		return user;
	}
	
	
	/**
	 * The sendMessage method is used to send a direct message to a user
	 * who is identified by a specific screen name
	 * @param screenName The screen name of the receiver of the direct message
	 * @param text The message to be sent or null if the message failed to be sent
	 * @return The direct message which was sent to the user or 
	 * @throws TwitterException
	 */
	public DirectMessage sendMessage(String screenName, String theMessage) throws TwitterException
	{
		DirectMessage dm = null;
		
		//call the API sendDirectMessage method
		try
		{
			dm = twitter.sendDirectMessage(screenName, theMessage);
		}
		catch(TwitterException te)
		{
            System.out.println("Failed to send a direct message: " + te.getMessage());
			te.printStackTrace();
		}
		
		return dm; 	
	}
}
