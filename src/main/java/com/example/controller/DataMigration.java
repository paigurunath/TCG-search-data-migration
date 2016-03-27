package com.example.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@RestController
@EnableAutoConfiguration
public class DataMigration {

	@RequestMapping("/")
	String home() {
		return "Hello World!";
	}

	@RequestMapping(value="/readUrl", method=RequestMethod.POST)
	public String readUrl(@RequestBody JsonNode jsonNode ) {
		try {
			System.out.println(jsonNode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "SUCCESS";
	}
	
	@RequestMapping(value="/readUrlString", method=RequestMethod.POST)
	public String readUrlString(@RequestBody String jsonNode ) {
		try {
			
			String responseStr = "";
			String GET_URL = "http://localhost:9200/website/blog/123";
		    URL obj = new URL(GET_URL);
	        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	        con.setRequestMethod("GET");
	      //  con.setRequestProperty("User-Agent", USER_AGENT);
	        int responseCode = con.getResponseCode();
	        System.out.println("GET Response Code :: " + responseCode);
	        if (responseCode == HttpURLConnection.HTTP_OK) { // success
	            BufferedReader in = new BufferedReader(new InputStreamReader(
	                    con.getInputStream()));
	            String inputLine;
	            StringBuffer response = new StringBuffer();
	 
	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }
	            in.close();
	 
	            // print result
	            System.out.println("------------------------------------------------------------------------------------");
	            responseStr = response.toString();
	            System.out.println(responseStr);
	            
	            System.out.println("------------------------------------------------------------------------------------");
	        }
	        
	        
			Object things = new Gson().fromJson(responseStr, Object.class);
		    List keys = new ArrayList();
		    collectAllTheKeys(keys, things);
		    
		    Iterator iter1 = keys.listIterator();
		    
		    while(iter1.hasNext()) {
		    	System.out.println(iter1.next());
		    }
		    
		    
		} catch(com.google.gson.JsonParseException e) {
			e.printStackTrace();
			return "String parsing failed";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "SUCCESS";
	}
	
	
	void collectAllTheKeys(List keys, Object o)
	  {
	    Collection values = null;
	    if (o instanceof Map)
	    {
	      Map map = (Map) o;
	      keys.addAll(map.keySet()); // collect keys at current level in hierarchy
	      values = map.values();
	    }
	    else if (o instanceof Collection)
	      values = (Collection) o;
	    else // nothing further to collect keys from
	      return;

	    for (Object value : values)
	      collectAllTheKeys(keys, value);
	  }
}
