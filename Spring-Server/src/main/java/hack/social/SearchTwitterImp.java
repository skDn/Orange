package hack.social;

import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

import java.util.List;

/**
 * Created by yordanyordanov on 03/10/2015.
 */
public class SearchTwitterImp implements SearchTwitter{

    private Twitter twitter;

    public SearchTwitterImp(String key, String secret){
        this.twitter = new TwitterTemplate(key,secret);
    }

    @Override
    public List<Tweet> getTweetsFor(String topic) {
//        String consumerKey = "awpkgjnYx7f69Ye8LRW7LPg0K"; // The application's consumer key
//        String consumerSecret = "iwYtjbMuFVOOWpfdnHYq8OpLBQMvElXSlsUGgUrSduNcE3MdvI"; // The application's consumer secret
//        String accessToken = "..."; // The access token granted after OAuth authorization
//        String accessTokenSecret = "..."; // The access token secret granted after OAuth authorization
//        //Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
        //Twitter twitter = new TwitterTemplate(consumerKey,consumerSecret);
        SearchResults results = this.twitter.searchOperations().search(topic);
        List<Tweet> tweetList = results.getTweets();
        return tweetList;
    }
}
