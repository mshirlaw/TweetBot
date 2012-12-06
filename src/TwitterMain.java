import java.io.IOException;

import javax.swing.JFrame;

import twitter4j.TwitterException;

/**
 * The main class creates an intances of the 
 * TwitterView GUI class and waits for user input.
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
	public static void main(String[] args) throws IOException, TwitterException 
	{
		try {
		TwitterView tv = new TwitterView();
		tv.setVisible(true);
		tv.pack();
		tv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tv.setTitle("Tweetbot");
		/*
		 * setResizable might not show the window properly. 
		 * Comment this line out if so.
		*/
		tv.setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
