package hack.controllers;

/**
 * Created by yordanyordanov on 03/10/2015.
 */

import java.util.List;

import hack.social.SearchTwitter;
import hack.social.SearchTwitterImp;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestSocial {


    @RequestMapping("/greeting")
    public List<Tweet> greeting() {
        String consumerKey = "awpkgjnYx7f69Ye8LRW7LPg0K"; // The application's consumer key
        String consumerSecret = "iwYtjbMuFVOOWpfdnHYq8OpLBQMvElXSlsUGgUrSduNcE3MdvI"; // The application's consumer secret

        SearchTwitter searchTwitter = new SearchTwitterImp(consumerKey, consumerSecret);
        return searchTwitter.getTweetsFor("#Vineapp");
    }
}


