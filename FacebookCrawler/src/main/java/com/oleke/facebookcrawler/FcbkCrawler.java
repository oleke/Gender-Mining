/*
 * Copyright 2014 Biodun.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oleke.facebookcrawler;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Biodun
 */
public class FcbkCrawler {

    private static FacebookClient client;
    private ArrayList<String> friendsList;
    private ThreadGroup tGroup;

    /**
     * Constructor
     */
    public FcbkCrawler() {

    }

    /**
     * Constructor
     *
     * @param accessToken FaceBook Access Token
     */
    public FcbkCrawler(String accessToken) {
        client = new DefaultFacebookClient(accessToken);
        friendsList = new ArrayList<String>();
        tGroup = new ThreadGroup("getFriends");
    }

    /**
     * Set the accessToken value
     *
     * @param accessToken FaceBook Access Token
     */
    public void setFacebookClient(String accessToken) {
        client = new DefaultFacebookClient(accessToken);
    }

    /**
     * Gets the FaceBook Client
     *
     * @return returns the a Client Session
     */
    public FacebookClient getFacebookClient() {
        return client;
    }

    /**
     * Get the friends of a user
     *
     * @param id The FaceBook user id for the getFriends request The user with
     * starting id is 'me'
     */
    public void getFriends(String id) {
        try {
            //Create a temporary string arraylist
            ArrayList<String> temp;
            temp = new ArrayList<String>();
            //Initialize a facebook graph explorer request
            Connection<User> myFriends;
            myFriends = client.fetchConnection(id + "/friends", User.class);
            //Create an Iterator to traverse the list
            Iterator<List<User>> iterator = myFriends.iterator();
            //Loop the list
            while (iterator.hasNext()) {
                for (User u : iterator.next()) {
                    //add the user id to the friends's list
                    if (!friendsList.contains(u.getId())) {
                        friendsList.add(u.getId());
                        //add the new user id to the temporary list
                        temp.add(u.getId());
                    }
                }
            }

            //Recursive Call to get Friends of friends
            for (final String i : temp) {
                Thread t = new Thread(tGroup, new Runnable() {
                    public void run() {
                        getFriends(i);
                        Thread.currentThread().interrupt();
                    }
                });

                t.start();
            }

        } catch (Exception e) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Get the Crawled Friends List
     *
     * @return Returns an array of Friends List
     */
    public ArrayList<String> getFriendsList() {
        return friendsList;
    }

    /**
     * Returns a ThreadGroup Associated with the GetFriends Method Call
     *
     * @return a Thread Group
     */
    public ThreadGroup getTGroup() {
        return tGroup;
    }

    public void writeToFile(String filename, ArrayList<String> list) {
        try {
            for (String id : list) {
                BufferedWriter wr = new BufferedWriter(new FileWriter(filename, true));
                wr.newLine();
                wr.write(id);
                wr.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(FcbkCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<String> readFromFile(String filename) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line = "";
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(FcbkCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

}
