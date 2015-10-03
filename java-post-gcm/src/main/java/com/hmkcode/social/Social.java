package com.hmkcode.social;

import twitter4j.*;
import twitter4j.auth.AccessToken;

public class Social{
    public static void main(String [] args){
        getTwitterData("london");
    }

    public static String getTwitterData(String topic){
        AccessToken AT = new AccessToken("2943518925-6A4Zc8T7mEwmdwp5AsyWqPTmUxRovz71mOreHRS","0Q1tfFjUywtSmk4FhLyqMtriZ83sdCHK5wl6seOwAdLfc");
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer("CFCowfMEVVnrmDqf4XPtZzrLr", "wR4Z74vs4RjMl5WB54oBd5yNd9vmkWCflHV1vcL9WCxT6weuVt");
        twitter.setOAuthAccessToken(AT);

        try{
            String tweetResult = "";
            double latitude = 0;
            double longitude = 0;
            Query query = new Query("#" + topic);
            QueryResult result = twitter.search(query);
            for (Status status : result.getTweets()) {
                try {
                    for(int j = 0; j<4; j++) {
                        latitude += status.getPlace().getBoundingBoxCoordinates()[0][j].getLatitude();
                        longitude += status.getPlace().getBoundingBoxCoordinates()[0][j].getLongitude();
                    }
                    latitude /=4;
                    longitude /=4;
                    tweetResult.concat(String.format("latitude: %f\n", latitude));
                    System.out.println("latitude: " + latitude);
                    tweetResult.concat(String.format("longitude: %f\n", longitude));
                    System.out.println("longitude: "+ longitude);
                    tweetResult.concat(status.getPlace().getFullName() + "\n");
                    System.out.println(status.getPlace().getFullName() + "\n");
                    tweetResult.concat(status.getText());
                    System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
                    return tweetResult;
                }
                catch(Exception e){
                    //System.out.println("+++++ NQMA LOCATIONS BRAT +++++++");
                }
            }
        }
        catch(TwitterException e){
            System.out.println("An error!");
        }

        return "NO RESPONSE FROM SOCIAL NETWORK!";
    }
}