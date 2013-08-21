import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import twitter4j.*;


/**
 * This class is used to display a smaller window
 * which can be used to send a tweet. The window
 * contains a text area to enter the tweet and 
 * a button to send the tweet 
 */
public class TwitterTweetView extends JFrame
{
	// private instance fields
	private JPanel mainPanel;
	private JTextArea textArea;
	private JButton tweetButton;
	private JScrollPane scrollPane;

	//to allow us to send tweets
	private TwitterManager manager; 

	//window width and height
	private final int WIDTH = 300;
	private final int HEIGHT = 100;

	
	public TwitterTweetView() throws IOException, TwitterException
	{	
		authorise();
		createGUI();
	}
	
	
	private void authorise() throws IOException, TwitterException
	{
		// create an instance of the TwitterManager class
		manager = new TwitterManager();	
		manager.authorise();
	}
	
	private void createGUI()
	{
		setSize(WIDTH,HEIGHT);
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setCaretPosition(0);
		scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setValue(0);		

		tweetButton = new JButton("Send Tweet");
		tweetButton.addActionListener(new ActionTweet());
		
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(tweetButton, BorderLayout.SOUTH);
		
		add(mainPanel);
		

	}

	/**
	 * Action listener class to respond to events initiated when 
	 * the user clicks a "Tweet" button 
	 * @author mshirlaw
	 *
	 */
	
	//This needs to be re-worked to open a new window for the tweet text
	private class ActionTweet implements ActionListener {
		public void actionPerformed(ActionEvent ae) {			
			try {
				if (textArea.getText().equals("quit")) {
					System.exit(0);
				} else 
				{
					//System.out.println(textArea.getText());
					//avoid empty tweets
					if(!textArea.getText().equals(""))
					{
						manager.tweet(textArea.getText());
					}
					textArea.setText("");
					setVisible(false); //you can't see me!
					dispose(); //Destroy the JFrame object
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
