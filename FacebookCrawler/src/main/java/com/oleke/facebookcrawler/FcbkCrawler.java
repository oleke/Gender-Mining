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
import com.restfb.types.Album;
import com.restfb.types.Group;
import com.restfb.types.Page;
import com.restfb.types.Post;
import com.restfb.types.User;
import com.restfb.types.User.Education;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author Biodun
 */
public class FcbkCrawler {

    private FacebookClient client;
    private ArrayList<String> friendsList;
    private ArrayList<String> crawled;
    private ThreadGroup tGroup;
    private int noAlbums = 0;
    private int noPictures = 0;
    private final String _15_19 = "15-19";
    private final String _20_24 = "20-24";
    private final String _25_29 = "25-29";
    private final String _30_34 = "30-34";
    private final String _35_39 = "35-39";
    private final String _40_44 = "40-44";
    private final String _45_49 = "45-49";
    private final String _50_54 = "50-54";
    private final String _55_59 = "55-59";
    private final String _60_64 = "60-64";
    private final String _65_69 = "65-69";
    private final String _70_74 = "70-74";
    private final String _75_79 = "75-79";
    private final String _80_84 = "80-84";
    private final String _85 = "85>";

    /**
     * Constructor
     */
    public FcbkCrawler() {

    }

    /**
     * Constructor
     *
     * @param accessToken FaceBook Access Token
     * @param existingList A list of users in database
     * @param crawled A list of crawled users
     */
    public FcbkCrawler(String accessToken, ArrayList<String> existingList, ArrayList<String> crawled) {
        this.client = new DefaultFacebookClient(accessToken);
        this.friendsList = existingList;
        this.crawled = crawled;
        this.tGroup = new ThreadGroup("getFriends");
    }

    /**
     * Gets the Crawled Users
     *
     * @return returns the list of crawled users
     */
    public ArrayList<String> getCrawled() {
        return crawled;
    }

    /**
     * Set the accessToken value
     *
     * @param accessToken FaceBook Access Token
     */
    public void setFacebookClient(String accessToken) {
        this.client = new DefaultFacebookClient(accessToken);
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
            while (iterator.hasNext()) {
                List<User> friends = iterator.next();
                //Loop the list            
                for (User u : friends) {
                    //add the user id to the friends's list
                    if (!friendsList.contains(u.getId())) {
                        friendsList.add(u.getId());
                        //add the new user id to the temporary list
                        temp.add(u.getId());
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
            }

        } catch (Exception e) {
            //Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, e);
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

    /**
     * Get User Details
     *
     * @param id
     * @return Returns a User with the details
     */
    public User getUserDetails(String id) {
        User u = client.fetchObject(id, User.class);
        return u;
    }

    /**
     * This method returns the age of the user
     *
     * @param dob Date of Birth
     * @return the Age of the user
     */
    public int getAge(Date dob) {
        if (dob != null) {
            Date current_date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("Y");
            int year_of_birth = Integer.parseInt(sdf.format(dob));
            int current_year = Integer.parseInt(sdf.format(current_date));
            return current_year - year_of_birth;
        } else {
            return 0;
        }
    }

    /**
     * This method returns the age range the User falls into
     *
     * @param age The age of the user
     * @return The age range
     */
    public String getAgeRange(int age) {
        if (age >= 15 && age <= 19) {
            return _15_19;
        } else if (age >= 20 && age <= 24) {
            return _20_24;
        } else if (age >= 25 && age <= 29) {
            return _25_29;
        } else if (age >= 30 && age <= 34) {
            return _30_34;
        } else if (age >= 35 && age <= 39) {
            return _35_39;
        } else if (age >= 40 && age <= 44) {
            return _40_44;
        } else if (age >= 45 && age <= 49) {
            return _45_49;
        } else if (age >= 50 && age <= 54) {
            return _50_54;
        } else if (age >= 55 && age <= 59) {
            return _55_59;
        } else if (age >= 60 && age <= 64) {
            return _60_64;
        } else if (age >= 65 && age <= 69) {
            return _65_69;
        } else if (age >= 70 && age <= 74) {
            return _70_74;
        } else if (age >= 75 && age <= 79) {
            return _75_79;
        } else if (age >= 80 && age <= 84) {
            return _80_84;
        } else if (age > 85) {
            return _85;
        } else {
            return "NULL";
        }
    }

    /**
     * User Educational Level
     *
     * @param education
     * @return Returns the user's educational level
     */
    public String getEducationLevel(List<Education> education) {
        if (!education.isEmpty()) {
            return education.get(education.size() - 1).getType();
        }
        return "NULL";
    }

    /**
     * Gets a User's album
     *
     * @param id user id
     */
    private void getAlbums(String id) {
        Connection<Album> albums = client.fetchConnection(id + "/albums", Album.class);
        Iterator<List<Album>> iterator = albums.iterator();        
        int no_pics = 0;
        while(iterator.hasNext()){
            List<Album> data = iterator.next();
            this.noAlbums+=data.size();
            for (Album al : data) {
            if (al.getCount() != null) {
                this.noPictures += al.getCount();
            }
        }
        }                              
        
    }

    /**
     * Get No of Albums
     *
     * @param id User id
     * @return Returns the number of albums
     */
    public int getNoAlbums(String id) {
        getAlbums(id);
        return noAlbums;
    }

    /**
     * Gets the total number of pictures uploaded by a user
     *
     * @param id user id
     * @return
     */
    public int getNoPictures(String id) {
        getAlbums(id);
        return noPictures;
    }

    /**
     * Get No of groups the user belong to
     *
     * @param id user id
     * @return the Number of groups
     */
    public int getNoGroups(String id) {
        Connection<Group> groups = client.fetchConnection(id + "/groups", Group.class);
        Iterator<List<Group>> iterator =groups.iterator();
        int size = 0;
        while(iterator.hasNext()){
            List<Group> data = iterator.next();
            size+=data.size();
        }
        return size;
    }

    /**
     * No of User Posts
     *
     * @param id user id
     * @return Returns the no of posts
     */
    public int getNoPosts(String id) {
        Connection<Post> posts = client.fetchConnection(id + "/posts", Post.class);
        Iterator<List<Post>> iterator = posts.iterator();
        int size = 0;
        while(iterator.hasNext()){
            List<Post> data = iterator.next();
            size+=data.size();
        }
        return size;
    }

    /**
     * Gets User Posts
     *
     * @param id user id
     * @return Returns an array of user posts
     */
    public ArrayList<Post> getPosts(String id) {        
        ArrayList<Post> data = new ArrayList<Post>();
        Connection<Post> posts = client.fetchConnection(id + "/posts", Post.class);
        Iterator<List<Post>> iterator = posts.iterator();
        while (iterator.hasNext()) {
            List<Post> data1 = iterator.next();
            for (Post p : data1)
                data.add(p);
        }

        return data;
    }

    /**
     * Gets the number of posts a user was tagged in
     *
     * @param id user id
     * @return Returns the number of tagged posts
     */
    public int getNoTaggedPosts(String id) {
        Connection<Post> posts = client.fetchConnection(id + "/tagged", Post.class);
        List<Post> data = posts.getData();
        return data.size();
    }

    /**
     * Gets the No of Pages
     *
     * @param id user id
     * @return Returns the number of pages a user likes
     */
    public int getNoPages(String id) {
        Connection<Page> pages = client.fetchConnection(id + "/likes", Page.class);
        List<Page> data = pages.getData();
        return data.size();
    }

    public void crawlUser(String id, Sheet sh) {

    }
}
