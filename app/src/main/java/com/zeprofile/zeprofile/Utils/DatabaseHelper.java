/*
 * Created by Hang on 18-4-12 下午3:19
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 18-4-12 下午1:41
 */

package com.zeprofile.zeprofile.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Patterns;

import com.zeprofile.zeprofile.Login;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "zeprofile.db";
    public static final String TABLE_NAME = "user";
    public static final String COL_1 = "email";
    public static final String COL_2 = "lastName";
    public static final String COL_3 = "firstName";
    public static final String COL_4 = "password";
    public static final String COL_5 = "resetCode";         // Just for testing / demo
    public static final String COL_6 = "homeAddress";
    public static final String COL_7 = "deliveryAddress";
    public static final String COL_8 = "locationContinous";
    public static final String COL_9 = "locationAddress";
    public static final String COL_10 = "duration";
    public static final String COL_11 = "recipient";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table " + TABLE_NAME + "(" + COL_1 + " text primary key," + COL_2 + " text," + COL_3 + " text," + COL_4 + " text," + COL_5 + " text," + COL_6 + " text," + COL_7 + " text," + COL_8 + " text," + COL_9 + " text," + COL_10 + " text," + COL_11 + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    // Insert a new user profile in database
    public boolean insertNewUser(String email, String lastName, String firstName, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_1, email);
        contentValues.put(DatabaseHelper.COL_2, lastName);
        contentValues.put(DatabaseHelper.COL_3, firstName);
        contentValues.put(DatabaseHelper.COL_4, password);
        long ins = db.insert(TABLE_NAME, null, contentValues);
        if (ins == -1) return false;
        else return true;
    }

    // Check if the email address is valid (basic ckeck)
    public final static boolean isValidEmail(String targetEmail) {
        if (targetEmail == null) return false;
        else return Patterns.EMAIL_ADDRESS.matcher(targetEmail).matches();
    }

    // Check if the email exists (true - email is used / false - email is not used)
    public boolean isUsedEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + DatabaseHelper.TABLE_NAME + " where " + DatabaseHelper.COL_1 + "=?", new String[]{email});
        if (cursor.getCount() > 0) return true;
        else return false;
    }

    // Check the email and password for login
    public boolean checkPasswordForLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + DatabaseHelper.TABLE_NAME + " where " + DatabaseHelper.COL_1 + "=? and " + DatabaseHelper.COL_4 + "=?", new String[]{email, password});
        if (cursor.getCount() > 0) return true;
        else return false;
    }

    // Update new password
    public boolean updatePassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("update " + DatabaseHelper.TABLE_NAME + " set " + DatabaseHelper.COL_4 + "=? where " + DatabaseHelper.COL_1 + "=?", new String[]{password, email});
        if (cursor.getCount() > 0) return false;
        else return true;
    }

    // Clear the reset code of a specific account
    public boolean clearResetCode(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("update " + DatabaseHelper.TABLE_NAME + " set " + DatabaseHelper.COL_5 + "= NULL where " + DatabaseHelper.COL_1 + "=?", new String[]{email});
        if (cursor.getCount() > 0) return false;
        else return true;
    }

    // Save the reset code in database * just for test / demo
    public boolean saveResetCode(String email, String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("update " + DatabaseHelper.TABLE_NAME + " set " + DatabaseHelper.COL_5 + "=? where " + DatabaseHelper.COL_1 + "=?", new String[]{code, email});
        if (cursor.getCount() > 0) return false;
        else return true;
    }

    // Check the reset code * just for test / demo
    public boolean checkResetCode(String email, String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + DatabaseHelper.TABLE_NAME + " where " + DatabaseHelper.COL_1 + "=? and " + DatabaseHelper.COL_5 + "=?", new String[]{email, code});
        if (cursor.getCount() > 0) return true;
        else return false;
    }

    // Getter
    public String getUserInfo(String email, String option) {
        SQLiteDatabase db = this.getReadableDatabase();
        String res = "";
        Cursor cursor;
        if (email != null) { // debug
            cursor = db.rawQuery("Select * from " + DatabaseHelper.TABLE_NAME + " where " + DatabaseHelper.COL_1 + "=?", new String[]{email});
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    res = cursor.getString(cursor.getColumnIndex(option));
                }
                cursor.close();
            }
        }
        return res;
    }

    // Setter
    public boolean updateUserInfo(String email, String option, String newValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        if (email != null) { // debug
            cursor = db.rawQuery("update " + DatabaseHelper.TABLE_NAME + " set " + option + "=? where " + DatabaseHelper.COL_1 + "=?", new String[]{newValue, email});
            //Log.d("*******cursor******","count="+cursor.getCount());
            if (cursor.getCount() > 0) return true;
            else return false;
        } else return false;
    }
}

