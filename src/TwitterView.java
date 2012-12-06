import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;
import twitter4j.TwitterException;

/**
 * The TwitterView class creates a simple 
 * GUI for the Twitter client. This GUI will be expanded over time
 * as the application is developed further.
 * @author mshirlaw
 *
 */
public class TwitterView extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	//private instance fields
	private JPanel mainPanel;
	private JPanel buttonPanel;
	private JLabel label;
	private JTextField textField;
	private JButton tweetButton;
	private JButton clearButton;
	private TwitterManager manager;

	/**
	 *  The constructor creates a GUI which consists of 
	 *  a text field and two buttons. The user can enter a tweet 
	 *  into the text field and clear the text or post it 
	 *  to twitter.
	 * @throws IOException
	 * @throws TwitterException
	 */
	public TwitterView() throws IOException, TwitterException
	{
		//create an instance of the TwitterManager class and authorise the app
		manager = new TwitterManager();
		manager.authorise();	

		//set the size of the window and default close operation
		setSize(300,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainPanel = new JPanel();
		buttonPanel = new JPanel();
		
		mainPanel.setLayout(new GridLayout(3,1));
		buttonPanel.setLayout(new GridLayout(1,2));
		
		//create a label so that the user knows how to use the app
		label = new JLabel("Enter a tweet or type \"quit\" to exit: ", JLabel.CENTER);
		textField = new JTextField();
		
		//create the tweet button and set an action listener which calls the tweet method
		tweetButton = new JButton("Send Tweet");
		tweetButton.addActionListener(new ActionListener() {	   
			@Override
			public void actionPerformed(ActionEvent ae) {
				try 
				{
					if(textField.getText().equals("quit"))
					{
						System.exit(0);
					}
					else
					{
						manager.tweet(textField.getText());
						textField.setText("");
					}
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
	    }); 
		
		//create the clear button and an action listener that resets the text to an empty string
		clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {	   
			@Override
			public void actionPerformed(ActionEvent ae) {
				textField.setText("");
			}
	    }); 
		
		buttonPanel.add(tweetButton);
		buttonPanel.add(clearButton);
		buttonPanel.setVisible(true);
		
		mainPanel.add(label);
		mainPanel.add(textField);
		mainPanel.add(buttonPanel);
			
		mainPanel.setVisible(true);
		
		add(mainPanel);
	}
}
