package com.example.edukannada.HelperClass;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GlobalServerUrl {

//    public static String url = "https://edukannada.com/";
//    public static String imageurl = "http://edukannada.com/";

    //Local url for working
    public static String url="http://192.168.43.236:8081/Sangamsathi/sangamsathirest/userservice/";
//    public static String imageurl = "http://192.168.43.236:8081/Sangamsathi/";


    // paytm live credential
    public static String merchantMid = "eQPwjY09017354852087";

    // paytm testing credential
    // public static String merchantMid = "qJSLMW37059091869761";


    public static String INDUSTRY_TYPE_ID = "Retail";
    public static String CHANNLE_ID = "APP";
    public static String WEBSITE = "DEFAULT";
    public static String CALLBACK_URL = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=";

    public String hashCal(String type, String str) {
        byte[] hashseq = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashseq);
            byte[] messageDigest = algorithm.digest();

            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1) hexString.append("0");
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException nsae) {
        }
        return hexString.toString();
    }
}
