package com.hmkcode.social;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Social{
    public static void main(String [] args){
        List <String> input = new ArrayList<String>();
        input.add("crime");
        input.add("fire");
        input.add("fight");
        input.add("carcrash");
        getTwitterData(input);
    }

    public static String getTwitterData(List <String> topics){
        AccessToken AT = new AccessToken("2943518925-6A4Zc8T7mEwmdwp5AsyWqPTmUxRovz71mOreHRS","0Q1tfFjUywtSmk4FhLyqMtriZ83sdCHK5wl6seOwAdLfc");
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer("CFCowfMEVVnrmDqf4XPtZzrLr", "wR4Z74vs4RjMl5WB54oBd5yNd9vmkWCflHV1vcL9WCxT6weuVt");
        twitter.setOAuthAccessToken(AT);

        List <String> jsons = new ArrayList();
        try {
            Map<String, Object> tweetResult = new HashMap();
            double latitude = 0;
            double longitude = 0;
            for (int i = 0; i < topics.size(); i++) {
                Query query = new Query("#" + topics.get(i));
                QueryResult result = twitter.search(query);

                for (Status status : result.getTweets()) {
                    try {
                        for (int j = 0; j < 4; j++) {
                            latitude += status.getPlace().getBoundingBoxCoordinates()[0][j].getLatitude();
                            longitude += status.getPlace().getBoundingBoxCoordinates()[0][j].getLongitude();
                        }
                        latitude /= 4;
                        longitude /= 4;
                        List<Double> location = new ArrayList();
                        location.add(latitude);
                        location.add(longitude);
                        tweetResult.put("location", location);
                        tweetResult.put("message", status.getText());


                        System.out.println("latitude: " + latitude);
                        System.out.println("longitude: " + longitude);
                        System.out.println(status.getPlace().getFullName() + "\n");
                        System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());

                        Gson gson = new GsonBuilder().create();

                        //retun first working json
                        jsons.add(gson.toJson(tweetResult));
                    } catch (Exception e) {
                        //System.out.println("+++++ NQMA LOCATIONS BRAT +++++++");
                    }
                }
            }
        }
        catch(TwitterException e){
            System.out.println("An error!");
        }

        if(jsons.size() > 0){
            return jsons.get(0);
        }

        return "NO RESPONSE FROM SOCIAL NETWORK!";
    }
}