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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Biodun
 */
class ExcelAPI {

    /*
     * Cells Constants
     */
    public static final int s_id = 0;
    public static final int about = 1;
    public static final int bio = 2;    
    public static final int education_level = 3;
    public static final int name = 4;
    public static final int age_range = 5;
    public static final int political_view = 6;
    public static final int quotes = 7;
    public static final int religion = 8;
    public static final int relationship_status = 9;
    public static final int album_no = 10;
    public static final int no_pictures = 11;    
    public static final int no_of_groups = 12;
    public static final int no_posts = 13;
    public static final int no_tagged_posts = 14;
    public static final int no_pages = 15;
    public static final int s_gender = 16;
    public static final int p_id = 0;
    public static final int post = 1;
    public static final int post_type = 2;
    public static final int p_gender = 3;

    /**
     * Constructor
     */
    public ExcelAPI() {
    }

    /**
     * This method creates an Excel Workbook and an Excel Sheet
     *
     * @param filename The output filename
     * @param sheetname the name of the sheet
     * @return Returns a new workbook
     */
    public Workbook createExcel(String filename, String sheetname) {
        try {
            FileOutputStream out = new FileOutputStream(filename+".xls");
            Workbook wb = new HSSFWorkbook();
            wb.createSheet(sheetname);
            wb.write(out);
            out.close();
            return wb;
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    /**
     * This method loads an Excel Workbook
     *
     * @param filename The input filename
     * @return Returns an existing workbook
     */
    public Workbook loadExcel(String filename) {
        Workbook wbk = null;
        try {
            FileInputStream in = new FileInputStream(filename+".xls");
            wbk = new HSSFWorkbook(in);

        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        return wbk;
    }

    /**
     * This method initializes an Excel Workbook and an Excel Sheet
     *
     * @param filename The output/input filename
     * @param sheetname the name of the sheet
     * @return Returns a Sheet
     */
    public Sheet initExcel(String filename, String sheetname) {
        Workbook wbk;
        if (!new File(filename+".xls").exists()) {
            wbk = createExcel(filename, sheetname);
        } else {
            wbk = loadExcel(filename);
        }
        Sheet sh;
        sh = wbk.getSheet(sheetname);
        return sh;
    }

    /**
     * This method writes all changes made to a workbook
     *
     * @param filename The output filename
     * @param wbk The input workbook
     * @return Returns true if changes were written to file
     */
    public boolean commitChanges(String filename, Workbook wbk) {
        try {
            FileOutputStream out = new FileOutputStream(filename+".xls");
            wbk.write(out);
            out.close();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * This method creates a new row on a sheet
     *
     * @param sh the sheet to write on
     * @return Returns a new row
     */
    public Row createRow(Sheet sh) {
        int rowNo = sh.getPhysicalNumberOfRows();
        Row rw = sh.createRow(rowNo);
        return rw;
    }

    /**
     * This method writes on a cell in a Row
     *
     * @param rw The row to write on
     * @param cellNo The ordinal number of the cell to write on
     * @param cellValue The value to write on the cell
     */
    public void addCell(Row rw, int cellNo, String cellValue) {
        Cell cl = rw.createCell(cellNo);
        cl.setCellValue(cellValue);
    }

    /**
     * This method creates the headers for the FaceBook statistics file
     *
     * @param sh the sheet to write on
     */
    public void F_Stats_Header(Sheet sh) {
        Row r = createRow(sh);
        addCell(r, s_id, "id");
        addCell(r, about, "about");
        addCell(r, bio, "bio");        
        addCell(r, education_level, "education_level");
        addCell(r, name, "name");
        addCell(r, age_range, "age_range");
        addCell(r, political_view, "political_view");
        addCell(r, quotes, "quotes");
        addCell(r, religion, "religion");
        addCell(r, relationship_status, "relationship_status");
        addCell(r, album_no, "album_no");
        addCell(r, no_pictures, "no_pictures");        
        addCell(r, no_of_groups, "no_of_groups");
        addCell(r, no_posts, "no_posts");
        addCell(r, no_tagged_posts, "no_tagged_posts");
        addCell(r, no_pages, "no_pages");
        addCell(r, s_gender, "gender");
        commitChanges("stats", sh.getWorkbook());
    }

    /**
     * This method creates the headers for the FaceBook posts file
     *
     * @param sh the sheet to write on
     */
    public void F_Posts_Header(Sheet sh) {
        Row r = createRow(sh);
        addCell(r, p_id, "id");
        addCell(r, post, "post");
        addCell(r, post_type, "post_type");
        addCell(r, p_gender, "gender");
        commitChanges("posts", sh.getWorkbook());
    }
    
    /**
     * Write list to file
     *
     * @param filename The output file name
     * @param list The input list
     */
    public void writeToFile(String filename, ArrayList<String> list) {
        try {
            ArrayList<String> existingList = readFromFile(filename);
            for (String id : list) {
                if(!existingList.contains(id)){
                BufferedWriter wr = new BufferedWriter(new FileWriter(filename, true));
                wr.newLine();
                wr.write(id);
                wr.close();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FcbkCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Read from file to list
     *
     * @param filename
     * @return Returns a list of the file data
     */
    public ArrayList<String> readFromFile(String filename) {
        ArrayList<String> list = new ArrayList<String>();
        try {            
            File fs = new File(filename);
            if(!fs.exists())
                fs.createNewFile();            
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
