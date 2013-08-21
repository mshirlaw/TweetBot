import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;

import twitter4j.DirectMessage;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
	//private JButton refreshButton;
	//private JButton repliesButton;
	//private JButton messagesButton;

	//size of text area
	private final int COLS = 20;
	private final int ROWS = 25;
	private final int TEXT_WIDTH = 300;
	private final int TEXT_HEIGHT = 500;
	
	private TwitterManager manager; 
	
	private JTextArea home;
	private JTextArea replies;
	private JTextArea messages;
	
	private JTabbedPane tabbedPane;
	private JScrollPane scrollPaneHome;
	private JScrollPane scrollPaneReplies;	
	private JScrollPane scrollPaneMessages;
	
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
	public TwitterView() throws IOException, TwitterException 
	{
		// create an instance of the TwitterManager class and authorise the app
		manager = new TwitterManager();
		manager.authorise();
		
		setSize(TEXT_WIDTH,TEXT_HEIGHT);

		// main panel to hold components
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new BorderLayout());

		// create a label so that the user knows how to use the app
		label = new JLabel("Welcome to TweetBot", JLabel.CENTER);
		label.setPreferredSize(new Dimension(TEXT_WIDTH, TEXT_HEIGHT/6));
		
		// text area for timeline
		home = new JTextArea(ROWS, COLS);
		home.setLineWrap(true);
		home.setWrapStyleWord(true);
		home.setCaretPosition(0);

		// text area for replies
		replies = new JTextArea(ROWS, COLS);
		replies.setLineWrap(true);
		replies.setWrapStyleWord(true);
		replies.setCaretPosition(0);
		
		// text area for messages
		messages = new JTextArea(ROWS, COLS);
		messages.setLineWrap(true);
		messages.setWrapStyleWord(true);
		messages.setCaretPosition(0);

		//set scrolling
		//JScrollPane scrollPane = new JScrollPane(textArea);
		tabbedPane = new JTabbedPane();
		tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
		
		//home tab
		scrollPaneHome = new JScrollPane(home);
		scrollPaneHome.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneHome.getVerticalScrollBar().setValue(0);		
		JComponent tab1 = scrollPaneHome;
		tabbedPane.addTab("Home", tab1);
		tabbedPane.setPreferredSize(new Dimension(TEXT_WIDTH, TEXT_HEIGHT));

		//replies tab
		scrollPaneReplies = new JScrollPane(replies);
		scrollPaneReplies.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneReplies.getVerticalScrollBar().setValue(0);
		JComponent tab2 = scrollPaneReplies;
		tabbedPane.addTab("Replies", tab2);
		tabbedPane.setPreferredSize(new Dimension(TEXT_WIDTH, TEXT_HEIGHT));

		//messages tab
		scrollPaneMessages = new JScrollPane(messages);
		scrollPaneMessages.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneMessages.getVerticalScrollBar().setValue(0);
		JComponent tab3 = scrollPaneMessages;
		tabbedPane.addTab("Messages", tab3);
		tabbedPane.setPreferredSize(new Dimension(TEXT_WIDTH, TEXT_HEIGHT));

		//update timeline when tab is switched
		tabbedPane.addChangeListener(new ChangeListener() 
		{
		    public void stateChanged(ChangeEvent e) 
		    {
		        JTabbedPane source = (JTabbedPane) e.getSource();
		        if (source.getSelectedIndex() == 0)
		        	refreshTimeline();
		        else if (source.getSelectedIndex() == 1)
		        	refreshReplies();
		        else if(source.getSelectedIndex() == 2)
		        	refreshMessages();
		        }
		});
		
		// area to hold buttons
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2,1));

		// create the tweet button
		tweetButton = new JButton("Send Tweet");
		tweetButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				try 
				{
					openTweetWindow();
				}
				catch (IOException e1) 
				{
					e1.printStackTrace();
				} 
				catch (TwitterException e1) 
				{
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.add(tweetButton);
				
		// create the clear button
		clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionClear());
		buttonPanel.add(clearButton);
				
		//build the GUI
		mainPanel.add(label, BorderLayout.NORTH);
		mainPanel.add(tabbedPane, BorderLayout.CENTER);
		//mainPanel.add(scrollPane);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
				
		add(mainPanel);
		refreshTimeline();
	}

	//update the user's timeline
	private void refreshTimeline()
	{
		//System.out.println("\nTimeline for @"+manager.getUser().getScreenName()+":\n");
		try {
			timeline = manager.getTimeLine();
	        home.setText("");
			for (Status status : timeline) {
		    	//System.out.println("@"+status.getUser().getScreenName() + "\n" +status.getText()+"\n");
				home.append("@"+status.getUser().getScreenName() + "\n" +status.getText()+"\n");
				home.append("\n");
				
		    }
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		home.setCaretPosition(0);
		scrollPaneHome.getVerticalScrollBar().setValue(0);
	}
	
	//update the users replies
	private void refreshReplies()
	{
		//System.out.println("\n@replies Timeline for @"+manager.getUser().getScreenName()+":\n");
		try {
			atTimeline = manager.getAtTimeLine();
	        replies.setText("");
			for (Status status : atTimeline) {
				replies.append("@"+status.getUser().getScreenName() + "\n" +status.getText()+"\n");
				replies.append("\n");
		    }
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		replies.setCaretPosition(0);
		scrollPaneReplies.getVerticalScrollBar().setValue(0);
	}
	
	//update the users messages
	private void refreshMessages()
	{
		//System.out.println("\nDirect Messages for @"+manager.getUser().getScreenName()+":\n");
		try {
			messagesTimeline = manager.getMessages();
	        messages.setText("");
			for (DirectMessage message : messagesTimeline) {
				messages.append("@"+message.getSender().getScreenName() + "\n" +message.getText()+"\n");
				messages.append("\n");
		    }
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		messages.setCaretPosition(0);
		scrollPaneMessages.getVerticalScrollBar().setValue(0);
	}
	
	//open the tweet window
	private void openTweetWindow() throws IOException, TwitterException
	{
		TwitterTweetView ttv = new TwitterTweetView();
		ttv.setVisible(true);
		ttv.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ttv.setTitle("Tweet");			
		
		ttv.setLocationRelativeTo(this);
		ttv.setLocation(TEXT_WIDTH, 0);
		//set resize to false
		ttv.setResizable(false);
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
			// clears the text from the box, resets to home tab.
			tabbedPane.setSelectedIndex(0);
			home.setText("");
			replies.setText("");
			messages.setText("");
		}
	}
	
	/**
	 * Action listener class to respond to events initiated when 
	 * the user clicks the "Refresh Timeline" button 
	 * not currently used
	 * @author mshirlaw
	 * @author ridentbyte
	 *
	 */
	private class ActionRefresh implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			System.out.println("\nTimeline for @"+manager.getUser().getScreenName()+":\n");
			try {
				timeline = manager.getTimeLine();
		        home.setText("");
				for (Status status : timeline) {
			    	//System.out.println("@"+status.getUser().getScreenName() + "\n" +status.getText()+"\n");
					home.append("@"+status.getUser().getScreenName() + "\n" +status.getText()+"\n");
					home.append("\n");
					
			    }
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Action listener class to respond to events initiated when 
	 * the user clicks the "Get @replies" button 
	 * not currently used
	 * @author mshirlaw
	 * @author ridentbyte
	 *
	 */
	private class ActionReplies implements ActionListener {

		public void actionPerformed(ActionEvent ae) {
			System.out.println("\n@replies Timeline for @"+manager.getUser().getScreenName()+":\n");
			try {
				atTimeline = manager.getAtTimeLine();
		        replies.setText("");
				for (Status status : atTimeline) {
					replies.append("@"+status.getUser().getScreenName() + "\n" +status.getText()+"\n");
					replies.append("\n");
			    }
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Action listener class to respond to events initiated when 
	 * the user clicks the "Get Direct Messages" button 
	 * not currently used
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
		        messages.setText("");
				for (DirectMessage message : messagesTimeline) {
					messages.append("@"+message.getSender().getScreenName() + "\n" +message.getText()+"\n");
					messages.append("\n");
			    }
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
	}	
}
