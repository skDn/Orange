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

        c.addRegId("APA91bEzPADr-jC2GdER7br0CworIUeeVN1R8Q_of_VJFaoOa3U43TLWvgSkHzZX2EfYxFgoyobv7BlWt8DCvmbXzOAmjsN_1ZogioIOs6JJ5660OsIoU_W32eVBX9WvkLWdMVsDhFuH");

        String info = Social.getTwitterData("london");

        c.createData("test", info);
		
		return c;
	}
}
