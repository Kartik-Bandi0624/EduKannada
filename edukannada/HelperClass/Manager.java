package com.example.edukannada.HelperClass;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Manager {
    // Sharedpref file name
    public static final String PREF_NAME = "EduKannada-User";
    // All Shared Preferences Keys
    public static String User_id = "userid";
    // User name (make variable public to access from outside)
    public static String emailid = "email_id";
    // typeof user  (make variable public to access from outside)
    public static String typeofuser = "typeofuser";
    public static String refNo = "refNo";
    public static String Username = "username";
    public static String password = "password";
    public static String remember_Me = "remember_Me";
    public static String isUserLoggedin = "isUserLoggedin";
    public static String BGName = "BGName";
    public static String caste = "caste";
    public static String mobile_no = "mobile_no";
    public static String referred_by = "referred_by";
    public static String NoOfRequest = "NoOfRequest";
    public static String displayWidth = "displayWidth";
    public static String displayHeight = "displayHeight";
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;


    // Constructor
    public Manager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String user_id, String email, String typeofuser, String refno, String BGName, String caste, String mobile_no, String refered_by) {
        // Storing login value as TRUE

        // Storing name in pref
        editor.putString(User_id, user_id);

        // Storing email in pref
        editor.putString(emailid, email);
        editor.putString(Manager.typeofuser, typeofuser);
        editor.putString(refNo, refno);
        editor.putString(Manager.BGName, BGName);
        editor.putString(Manager.caste, caste);
        editor.putString(Manager.mobile_no, mobile_no);
        editor.putString(Manager.referred_by, refered_by);
        Log.d("user refno", refno);
        // commit changes
        editor.commit();
    }

    //to store username and password if user select remember me
    public void loginPreference(String username, String password, boolean rememberme, boolean isUserLoggedin) {

        editor.putString(Username, username);
        editor.putString(Manager.password, password);
        editor.putBoolean(Manager.remember_Me, rememberme);
        editor.putBoolean(Manager.isUserLoggedin, isUserLoggedin);
        editor.commit();
    }

    public void clear() {

        editor.remove(User_id);
        editor.remove(emailid);
        editor.remove(typeofuser);
        editor.remove(refNo);
        editor.remove(BGName);
        editor.remove(caste);
        editor.remove(mobile_no);
        editor.remove(referred_by);

        editor.commit();
    }

    public void setIsUserLoggedinStatus(boolean flag) {
        editor.putBoolean(isUserLoggedin, flag);
        editor.commit();
    }

    public void setDisplayMatrics(int width, int height) {
        editor.putInt(displayWidth, width);
        editor.putInt(displayHeight, height);
        editor.commit();
    }

    public void NoofRequest(int request) {
        editor.putInt(NoOfRequest, request);
        editor.commit();
    }

}
