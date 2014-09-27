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

import com.restfb.types.Post;
import com.restfb.types.User;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class App {

    private static final String accessToken = "CAACEdEose0cBABLeQZAWNbmZAoK3MB9Vp0xAkdJTIwEqgdLgMHa8hqJ4Gv0hif5jVEUk1gUBFyNKX906VHsSf8Cm3MBUZABZAZB5cc8f7hVKc557JdKSJFCgGz320No1RstS9ManiSpv0NDPo0srZCLNqhZCkfDrXyUfP7JpbwoImDRAoXCIQTrrCwTKDtJAaseoWyrjqnAqtmKVZAUsa7J21wHG39MVNfcZD";
    private static final String dbFile = "db.txt";
    private static final String crawledFile = "crawled.txt";
    private static final String statFile = "stats";
    private static final String postFile = "posts2";

    public static void main(String[] args) {
        ExcelAPI xcel = new ExcelAPI();
        Sheet stats = xcel.initExcel(statFile, "Facebook Statistics");
        Sheet posts = xcel.initExcel(postFile, "Facebook Posts");
        if (stats.getPhysicalNumberOfRows() == 0) {
            xcel.F_Stats_Header(stats);
        }
        if (posts.getPhysicalNumberOfRows() == 0) {
            xcel.F_Posts_Header(posts);
        }

        FcbkCrawler fb = new FcbkCrawler(accessToken, xcel.readFromFile(dbFile), xcel.readFromFile(crawledFile));
        fb.getFriends("me");
        System.out.println("Building Crawler Database...");
        while (fb.getTGroup().activeCount() > 0) {
        }
        System.out.println("Finished");
        xcel.writeToFile(dbFile, fb.getFriendsList());
        fb.getFriendsList().remove(0);
        //fb.getCrawled().remove(0);
        System.out.println("Crawling User Data in Progress...");
        int i = 0;
        for (String id : fb.getFriendsList()) {
            if (!fb.getCrawled().contains(id)) {
                System.out.println(i + 1 + " Crawling user " + id);
                try {
                    User userDetails = fb.getUserDetails(id);
                    int age = fb.getAge(userDetails.getBirthdayAsDate());
                    String ageRange = fb.getAgeRange(age);
                    String educationLevel = fb.getEducationLevel(userDetails.getEducation());
                    int noAlbums = fb.getNoAlbums(id);
                    int noGroups = fb.getNoGroups(id);
                    int noPages = fb.getNoPages(id);
                    int noPictures = fb.getNoPictures(id);
                    int noPosts = fb.getNoPosts(id);
                    int noTaggedPosts = fb.getNoTaggedPosts(id);
                    List<Post> user_posts = fb.getPosts(id);
                    Row u_stats = xcel.createRow(stats);
                    xcel.addCell(u_stats, ExcelAPI.about, userDetails.getAbout());
                    xcel.addCell(u_stats, ExcelAPI.age_range, ageRange);
                    xcel.addCell(u_stats, ExcelAPI.album_no, Integer.toString(noAlbums));
                    xcel.addCell(u_stats, ExcelAPI.bio, userDetails.getBio());
                    xcel.addCell(u_stats, ExcelAPI.education_level, educationLevel);
                    xcel.addCell(u_stats, ExcelAPI.name, userDetails.getName());
                    xcel.addCell(u_stats, ExcelAPI.no_of_groups, Integer.toString(noGroups));
                    xcel.addCell(u_stats, ExcelAPI.no_pages, Integer.toString(noPages));
                    xcel.addCell(u_stats, ExcelAPI.no_pictures, Integer.toString(noPictures));
                    xcel.addCell(u_stats, ExcelAPI.no_posts, Integer.toString(noPosts));
                    xcel.addCell(u_stats, ExcelAPI.no_tagged_posts, Integer.toString(noTaggedPosts));
                    xcel.addCell(u_stats, ExcelAPI.political_view, userDetails.getPolitical());
                    xcel.addCell(u_stats, ExcelAPI.quotes, userDetails.getQuotes());
                    xcel.addCell(u_stats, ExcelAPI.relationship_status, userDetails.getRelationshipStatus());
                    xcel.addCell(u_stats, ExcelAPI.religion, userDetails.getReligion());
                    xcel.addCell(u_stats, ExcelAPI.s_gender, userDetails.getGender());
                    xcel.addCell(u_stats, ExcelAPI.s_id, userDetails.getId());

                    for (Post p : user_posts) {
                        Row u_posts = xcel.createRow(posts);
                        xcel.addCell(u_posts, ExcelAPI.p_id, userDetails.getId());
                        xcel.addCell(u_posts, ExcelAPI.post, p.getMessage());
                        if (!"status".equals(p.getType())) {
                            xcel.addCell(u_posts, ExcelAPI.post, p.getDescription());
                        }
                        xcel.addCell(u_posts, ExcelAPI.post_type, p.getType());
                        xcel.addCell(u_posts, ExcelAPI.p_gender, userDetails.getGender());
                    }

                    fb.getCrawled().add(id);
                } catch (Exception ex) {

                }
                i++;
            }
        }
        System.out.println("Writing to File");
        xcel.commitChanges(statFile, stats.getWorkbook());
        xcel.commitChanges(postFile, posts.getWorkbook());
        xcel.writeToFile(crawledFile, fb.getCrawled());
        System.out.println("Completed!");

    }
}
