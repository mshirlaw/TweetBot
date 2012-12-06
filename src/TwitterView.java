import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;
import twitter4j.TwitterException;

/**
 * 
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
	 *  
	 * @throws IOException
	 * @throws TwitterException
	 */
	public TwitterView() throws IOException, TwitterException
	{
		manager = new TwitterManager();
		manager.authorise();	

		setSize(300,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainPanel = new JPanel();
		buttonPanel = new JPanel();
		
		mainPanel.setLayout(new GridLayout(3,1));
		buttonPanel.setLayout(new GridLayout(1,2));
		
		label = new JLabel("Enter a tweet or type \"quit\" to exit: ", JLabel.CENTER);
		textField = new JTextField();
		
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
