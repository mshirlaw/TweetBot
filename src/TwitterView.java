import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;

import twitter4j.DirectMessage;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * The TwitterView class creates a simple GUI for the Twitter client. This GUI
 * will be expanded over time as the application is developed further.
 * 
 * @author mshirlaw
 * @author ridentbyte
 * 
 */
public class TwitterView extends JFrame {
	private static final long serialVersionUID = 1L;

	// private instance fields
	private JPanel mainPanel;
	private JPanel buttonPanel;
	private JLabel label;
	
	private JButton tweetButton;
	private JButton clearButton;
	private JButton refreshButton;
	private JButton repliesButton;
	private JButton messagesButton;

	//size of text area
	private final int COLS = 20;
	private final int ROWS = 25;
	private final int TEXT_WIDTH = 300;
	private final int TEXT_HEIGHT = 500;
	
	private TwitterManager manager;
	private JTextArea textArea;
	private List<Status> timeline;
	private ResponseList<Status> atTimeline;
	private ResponseList<DirectMessage> messagesTimeline;

	/**
	 * The constructor creates a GUI which consists of a text field and two
	 * buttons. The user can enter a tweet into the text field and clear the
	 * text or post it to twitter.
	 * 
	 * @throws IOException
	 * @throws TwitterException
	 */
	public TwitterView() throws IOException, TwitterException {

		// attempt to load the Nimbus style for the JFrame
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		// Uses gridbaglayout
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);

		// create an instance of the TwitterManager class and authorise the app
		manager = new TwitterManager();
		manager.authorise();

		// main panel to hold components
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		// create a label so that the user knows how to use the app
		label = new JLabel("Welcome to TweetBot",
				JLabel.CENTER);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		mainPanel.add(label, c);

		// text area for user to enter tweet
		textArea = new JTextArea(ROWS, COLS);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(textArea); 
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(TEXT_WIDTH, TEXT_HEIGHT));
		
		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 1;
		c.gridy = 1;
		mainPanel.add(scrollPane, c);

		// area to hold buttons
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_END;
		c.gridx = 0;
		c.gridy = 1;
		mainPanel.add(buttonPanel, c);

		// create a refresh timeline button for testing
		// really need to modify the GUI to support this feature
		// this is mostly just here for testing purposes
		refreshButton = new JButton("Refresh Timeline");
		refreshButton.addActionListener(new ActionRefresh());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		buttonPanel.add(refreshButton, c);
		
		// create a "get @replies" button for testing
		// really need to modify the GUI to support this feature
		// this is mostly just here for testing purposes
		repliesButton = new JButton("Get @replies");
		repliesButton.addActionListener(new ActionReplies());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		buttonPanel.add(repliesButton, c);
		
		// create a "get direct messages" button for testing
		// really need to modify the GUI to support this feature
		// this is mostly just here for testing purposes
		messagesButton = new JButton("Get Direct Messages");
		messagesButton.addActionListener(new ActionMessages());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		buttonPanel.add(messagesButton, c);

		// create the clear button
		clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionClear());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		buttonPanel.add(clearButton, c);
				
		// create the tweet button
		tweetButton = new JButton("Send Tweet");
		tweetButton.addActionListener(new ActionTweet());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		buttonPanel.add(tweetButton, c);
		
		
		add(mainPanel);
	}

	// Puts actions into own classes and methods for neatness.
	
	/**
	 * Action listener class to respond to events initiated when 
	 * the user clicks the "Tweet" button 
	 * @author mshirlaw
	 * @author ridentbyte
	 *
	 */
	private class ActionTweet implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			// Checks what text is entered that acts as required.
			
			try {
				if (textArea.getText().equals("quit")) {
					System.exit(0);
				} else {
					//avoid empty tweets
					if(!textArea.getText().equals(""))
					{
						manager.tweet(textArea.getText());
					}
					textArea.setText("");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Action listener class to respond to events initiated when 
	 * the user clicks the "Clear" button
	 * @author mshirlaw
	 * @author ridentbyte
	 *
	 */
	private class ActionClear implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			// clears the text from the box.
			textArea.setText("");
		}
	}
	
	/**
	 * Action listener class to respond to events initiated when 
	 * the user clicks the "Refresh Timeline" button 
	 * @author mshirlaw
	 * @author ridentbyte
	 *
	 */
	private class ActionRefresh implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			// currently prints the user's timeline to stdout
			// need to think about where and how this should be displayed in the GUI
			System.out.println("\nTimeline for @"+manager.getUser().getScreenName()+":\n");
			try {
				timeline = manager.getTimeLine();
				for (Status status : timeline) {
			    	//System.out.println("@"+status.getUser().getScreenName() + "\n" +status.getText()+"\n");
					textArea.append("@"+status.getUser().getScreenName() + "\n" +status.getText()+"\n");
					textArea.append("\n");
					
			    }
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Action listener class to respond to events initiated when 
	 * the user clicks the "Get @replies" button 
	 * @author mshirlaw
	 * @author ridentbyte
	 *
	 */
	private class ActionReplies implements ActionListener {

		public void actionPerformed(ActionEvent ae) {
			// currently prints the user's @replies to stdout
			// needs to be displayed in the GUI eventually
			System.out.println("\n@replies Timeline for @"+manager.getUser().getScreenName()+":\n");
			try {
				atTimeline = manager.getAtTimeLine();
				for (Status status : atTimeline) {
			        System.out.println("@"+status.getUser().getScreenName() + "\n" +status.getText()+"\n");
			    }
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Action listener class to respond to events initiated when 
	 * the user clicks the "Get Direct Messages" button 
	 * @author mshirlaw
	 * @author ridentbyte
	 *
	 */
	private class ActionMessages implements ActionListener {

		public void actionPerformed(ActionEvent ae) {
			// currently prints the user's Direct Messages to stdout
			// needs to be displayed in the GUI eventually
			System.out.println("\nDirect Messages for @"+manager.getUser().getScreenName()+":\n");
			try {
				messagesTimeline = manager.getMessages();
				for (DirectMessage message : messagesTimeline) {
			        System.out.println("@"+message.getSender().getScreenName() + "\n" +message.getText()+"\n");
			    }
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
	}

	
	
}
