#TweetBot
A simple twitter client using the twitter4j API.

#About
The twitter4j API is available at http://twitter4j.org. This repository includes the complete eclipse project as well as the required .jar libraries.

#Important
Before you are able to use this client you must create an app at http://dev.twitter.com
You must then copy the CONSUMER_KEY and CONSUMER_SECRET into appropriate variables in the TwitterManager.java class. You will then need to authorise the application to post on your behalf. 
In oder to compile and run this application you must do the following:
- Create a new eclipse project called TweetBot
- Copy the lib folder to the TweetBot folder in your workspace
- Copy the java source files to the src folder in your project
- Right click your project > refresh
- Right click your project > Build Path > Configure Build Path
- Choose "Java Build Path"
- Choose "Add jars" and add the twitter4j-core-3.0.2.jar libary to your project
- Build and run 

NB: When pushing changes to the TwitterManager class - remove sensitive information in the CONSUMER_KEY and CONSUMER_SECRET

#Future updates
This is a work in progress and will be updated with new features shortly. It is intended that in the future this will become a fully featured twitter client including:
- the ability to access and display a user's timeline 
- the ability to read and display a user's @ replies
- the ability to access and display a user's DMs

#Known bugs
- 