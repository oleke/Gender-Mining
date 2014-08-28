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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private static final int s_id = 0;
    private static final int about = 1;
    private static final int bio = 2;
    private static final int birthday = 3;
    private static final int education_level = 4;
    private static final int name = 5;
    private static final int age_range = 6;
    private static final int political_view = 7;
    private static final int quotes = 8;
    private static final int religion = 9;
    private static final int relationship_status = 10;
    private static final int album_no = 11;
    private static final int no_pictures = 12;
    private static final int no_of_games = 13;
    private static final int no_of_groups = 14;
    private static final int no_posts = 15;
    private static final int no_tagged_posts = 16;
    private static final int no_pages = 17;
    private static final int s_gender = 18;
    private static final int p_id = 0;
    private static final int post = 1;
    private static final int p_gender = 2;

    /**
     * Constructor
     */
    public ExcelAPI() {
    }

    /**
     * This Constructor initializes an Excel Sheet for Writing
     *
     * @param filename The output/input filename
     * @param sheetname the name of the sheet
     * @return Returns a Sheet for Writing
     */
    public Sheet ExcelAPI(String filename, String sheetname) {
        Sheet initExcel = initExcel(filename, sheetname);
        return initExcel;
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
            FileOutputStream out = new FileOutputStream(filename);
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
            FileInputStream in = new FileInputStream(filename);
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
        if (!new File(filename).exists()) {
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
            FileOutputStream out = new FileOutputStream(filename);
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
        int rowNo = sh.getPhysicalNumberOfRows() - 1;
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
        addCell(r, birthday, "birthday");
        addCell(r, education_level, "education_level");
        addCell(r, name, "name");
        addCell(r, age_range, "age_range");
        addCell(r, political_view, "political_view");
        addCell(r, quotes, "quotes");
        addCell(r, religion, "religion");
        addCell(r, relationship_status, "relationship_status");
        addCell(r, album_no, "album_no");
        addCell(r, no_pictures, "no_pictures");
        addCell(r, no_of_games, "no_of_games");
        addCell(r, no_of_groups, "no_of_groups");
        addCell(r, no_posts, "no_posts");
        addCell(r, no_tagged_posts, "no_tagged_posts");
        addCell(r, no_pages, "no_pages");
        addCell(r, s_gender, "gender");
        commitChanges("stats.xls", sh.getWorkbook());
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
        addCell(r, p_gender, "gender");
        commitChanges("posts.xls", sh.getWorkbook());
    }

}
