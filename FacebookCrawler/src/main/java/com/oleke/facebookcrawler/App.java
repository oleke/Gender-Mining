package com.oleke.facebookcrawler;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Account;
import com.restfb.types.Group;
import com.restfb.types.Post;
import com.restfb.types.User;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import sun.awt.AWTCharset;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final String accessToken = "CAACEdEose0cBAARrqMId4YeJOHpTMeWZBTR3SXWUkk4uhPJZBYaxFSeza3duqGfAGr0UCV2Uz86XOSP5cKIR7o0Mq22HZBha9rFx0lXfpiR5vVigVRROPBD3rQ9gLzLtaW3DzZCYY4cfnqO7S08AOFQZBDvE8lDQoYpgAQHtmJAV8hTU93FgAkreYsQnmfRrA8fgr8PpgvvagfhjPODHlukZBtIpfNqmcZD";
    private static final int iterations =10;
    static ArrayList<String> finalList = new ArrayList<String>();
    static FacebookClient client = new DefaultFacebookClient(accessToken);  
    static int counter =1;
    static int level = 0;
    static int ThreadCounter=0;
    public static void main( String[] args )
    {    
        System.out.println("Program Starting...");
        getFriends("me");              
        //User fetchObject = client.fetchObject("me/friends/",User.class);
        //System.out.println(fetchConnection.toString());
        //User fetchObject = client.fetchObject("me",User.class);   
        //list of people to crawl
        //crawl each
        //write result in csv or excel format
        //System.out.println(fetchObject.toString());
    }
    
    
    private void toExcel(){
        
    }
    
    private void toCSV(){
    }
    private static void getFriends(String id){
        try{
        //System.out.println(id);
        ArrayList<String> temp = new ArrayList<String>();
        Connection<User> myFriends = client.fetchConnection(id+"/friends",User.class);      
        Iterator<List<User>> iterator = myFriends.iterator();
        while(iterator.hasNext()){
           for(User u:iterator.next()){               
               temp.add(u.getId());
               System.out.println(counter+" "+id);
               counter+=1;               
           }
        }
            loadFinalList(temp);           

                for(final String i:temp){
                    Thread t = new Thread(new Runnable() {

                    public void run() {
                        ThreadCounter+=1;
                        int myno = ThreadCounter;
                        System.out.println("Thread "+ThreadCounter +" started");
                        getFriends(i);
                        Thread.currentThread().interrupt();
                        System.out.println("Thread "+myno +" stopped");
                    }
                });
                    t.start();
                }
                
        }
        catch(Exception e){
            
        }
    }
    
    private static void loadFinalList(ArrayList<String> temp){
        for(String id:temp){
            if(!finalList.contains(id)){
                finalList.add(id);      
                System.out.println(finalList.size() +" users crawled");                
            }
        }
    }
}

