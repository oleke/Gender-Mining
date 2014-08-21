package com.oleke.facebookcrawler;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;


/**
 * Hello world!
 *
 */
public class App 
{
    private static final String accessToken = "CAACEdEose0cBAJKZCSyATvUHIf9WhQZCOdmn2Y0ZAXnzZCaIYZCLbMYcg7fKki0oZAHbzsNszJpHNBQSyXk2yZAYllEMn0I1XIo03GyDY8nZBqUcxt59o5ZCIGKUClNufGB8H83DMuZCinZBFRYcObnDykLWbY0qirZC4sZCtO7NnSBwEETAYjiC6ZBZBGYfpDqh1KOTXN1fxH4QDkLJZByiQM1uXeZB22rlSjHNZBm0YZD";
    static ArrayList<String> finalList = new ArrayList<String>();
    static FacebookClient client = new DefaultFacebookClient(accessToken);  
    static int counter =1;  
    static int ThreadCounter=0;
    static boolean working = true;
    public static void main( String[] args )
    {    
        //System.out.println("Program Starting...");
        //getFriends("me");      
        //while(working){
        //if(Thread.activeCount()<2){
        //working = false;
        //System.out.println("Done");
        //System.out.println(finalList.size()+ " Users Crawled");
        //}
        //}
        //User fetchObject = client.fetchObject("me/friends/",User.class);
        //System.out.println(fetchConnection.toString());
        //User fetchObject = client.fetchObject("me",User.class);   
        //list of people to crawl
        //crawl each
        //write result in csv or excel format
        //System.out.println(fetchObject.toString());
        Workbook createExcel = null;
        if(!new File("stats.xls").exists()){
        createExcel = createExcel("stats.xls");
        }
        else{
            try {
                FileInputStream in = new FileInputStream("stats.xls");
                createExcel = new HSSFWorkbook(in);
            } catch (Exception ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Sheet sh = createExcel.getSheetAt(0);
        int rNo = sh.getPhysicalNumberOfRows();
        Row createRow = sh.createRow(rNo);
        String[] texts = {"Hello","World","This","IS","POI"};
        for(int i=0;i<5;i++){
            Cell createCell = createRow.createCell(i);
            createCell.setCellValue(texts[i]);
        }
        try {
            FileOutputStream out = new FileOutputStream("stats.xls");
            createExcel.write(out);
            out.close();
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private static Workbook createExcel(String filename){        
        try {
            FileOutputStream out = new FileOutputStream(filename);
            Workbook wb = new HSSFWorkbook();
            Sheet createSheet = wb.createSheet("Facebook Statistics");
            wb.write(out);
            out.close();
            return wb;
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }
    
    private void toCSV(){
    }
    private static void getFriends(String id){
        try{        
        ArrayList<String> temp = new ArrayList<String>();
        Connection<User> myFriends = client.fetchConnection(id+"/friends",User.class);      
        Iterator<List<User>> iterator = myFriends.iterator();
        while(iterator.hasNext()){
           for(User u:iterator.next()){               
               temp.add(u.getId());               
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
                //System.out.println(counter+" "+id);
                System.out.println(counter +" users crawled");                
                counter+=1;               
                
            }
        }
    }
}

