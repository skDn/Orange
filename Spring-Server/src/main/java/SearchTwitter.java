import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

import java.util.List;

/**
 * Created by yordanyordanov on 24/09/2015.
 */
public class SearchTwitter {
    public static void main(String [] args) {
        String consumerKey = "awpkgjnYx7f69Ye8LRW7LPg0K"; // The application's consumer key
        String consumerSecret = "iwYtjbMuFVOOWpfdnHYq8OpLBQMvElXSlsUGgUrSduNcE3MdvI"; // The application's consumer secret
        String accessToken = "..."; // The access token granted after OAuth authorization
        String accessTokenSecret = "..."; // The access token secret granted after OAuth authorization
        //Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
        Twitter twitter = new TwitterTemplate(consumerKey,consumerSecret);
        SearchResults results = twitter.searchOperations().search("#VineApp");
        List<Tweet> tweetList = results.getTweets();
        for(Tweet tweet : tweetList)
            System.out.println(tweet.getText());
        //System.out.println(results.toString());


    }
}
