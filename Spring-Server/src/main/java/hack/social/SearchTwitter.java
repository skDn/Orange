package hack.social;

import org.springframework.social.twitter.api.Tweet;

import java.util.List;

/**
 * Created by yordanyordanov on 03/10/2015.
 */
public interface SearchTwitter {
    public List<Tweet> getTweetsFor(String topic);
}
