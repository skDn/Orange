package com.hmkcode;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmkcode.social.Social;
import com.hmkcode.vo.Content;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Sending POST to GCM" );
        
        //String apiKey = "AIzaSyB8azikXJKi_NjpWcVNJVO0dGFG1WuNJlg";
        String apiKey = "AIzaSyBn-oewJHt5BQsSsjtfy8bvXDpnnWT6LBE";
        Content content = createContent();
        
        POST2GCM.post(apiKey, content);
    }
    
    public static Content createContent(){
		
		Content c = new Content();

        c.addRegId("APA91bG7sfY7V-x4I2RXd9-BuE1tV1x_yuZFjFhocfLJJ_U1s1veFNfMqllVHxXVXayNZDlRIiYrswHsB7ZuI9YIJPCSYFRfa8h-myRAq_S-FLTidlIedWp6odMYbkDZ9NZmP0sSvlWT");

        String info = Social.getTwitterData("london");

        c.createData("test", info);
		
		return c;
	}
}
