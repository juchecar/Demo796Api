package com.snapfish.publisher;

import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
//import java.util.Map;
//import java.util.Properties;
//import java.util.Date;
//
import org.json.JSONException;
import org.json.JSONObject;
//import org.json.JSONArray;

public class TestGet796Token {
    public String get796RequestToken(String appid, String apikey, String secretkey)
            throws Exception {
        String url796 = "https://796.com/oauth/token";
        URL url = new URL(url796);
        URLConnection uc = url.openConnection();
        uc.setConnectTimeout(0);
        uc.setReadTimeout(0);
        uc.setDoOutput(true);

        OutputStreamWriter uco = new OutputStreamWriter(uc.getOutputStream());
        Request op = new Request("GET");//("POST");
        op.sign796(appid, apikey, secretkey);
        uco.write(op.toString());// flush the normalized string to url connection.
        uco.close();

        String jsonData796 = Utils.readString(uc.getInputStream());
        try {
            JSONObject jsonRoot = new JSONObject(jsonData796);
            String errno = jsonRoot.getString("errno");
            if (errno.equals("0")) {
                JSONObject jsonData = jsonRoot.getJSONObject("data");
                String access_token = jsonData.getString("access_token");
                //System.out.println("access_token="+ access_token);
                return access_token;
            }
            else {
                System.out.println("errno="+ errno);
                throw new Exception("jsonData796=("+jsonData796+")");
            }
        } catch (JSONException e) {
            System.out.println("Json parse("+jsonData796+") error");
            e.printStackTrace();
            throw e; // rethrow the exception
        }
    }

    public static void main1(String[] args) throws Exception {
        TestGet796Token t = new TestGet796Token();
        String accessToken = t.get796RequestToken(
                            "##YOUR APPID##",
                            "##YOUR APIKEY##",
                            "##YOUR SECRETKEY##");
        System.out.println("get796RequestToken="+ accessToken);
        System.out.println("-------------------------------------");
    }
    
    public static void main10871OK(String[] args) throws Exception {
        TestGet796Token t = new TestGet796Token();
        String accessToken = t.get796RequestToken(
                            "10871",
                            "6baa738b-e048-e4ed-e3a8-bb69-8af405a7",
                            "TF8obUl70bdert4oKArHl24WREvYFMlT588OqkvvGfRXkS2oGotEKkAk7u1p");
        System.out.println("get796RequestToken="+ accessToken);
        System.out.println("-------------------------------------");
    }
    public static void main(String[] args) throws Exception {
        TestGet796Token t = new TestGet796Token();
        String accessToken = t.get796RequestToken(
                            "11038",
                            "fb8e69ed-69d1-0b00-a335-06cf-5018dd22",
                            "SwsvPk5+0OsOrocsKQCakW5FTRvRFJwFspsOrku9FPQFxXj3GoYVfUxwv75s");
        System.out.println("get796RequestToken="+ accessToken);
        System.out.println("-------------------------------------");
    }
    
    public static void mainOK(String[] args) throws Exception {
        TestGet796Token t = new TestGet796Token();
        String accessToken = t.get796RequestToken(
                            "11039",
                            "0e1a319f-8712-cd44-916c-c859-c4efe6d5",
                            "SQggakkt07ZYqY4oLAjGk25LQ07SFM8Dtp8O9km4QvRWyy6oGtARKRJx7ehr");
        System.out.println("get796RequestToken="+ accessToken);
        System.out.println("-------------------------------------");
    }
    
    public static void main11040(String[] args) throws Exception {
        TestGet796Token t = new TestGet796Token();
        String accessToken = t.get796RequestToken(
                            "11040",
                            "5999a1ce-4312-8a3c-75a5-327c-f5cf5251",
                            "HF94bR940e1d9YZwfgickG5HR07SFJQGscgO+E3vFPQGwSzyGtUQLxIh6blv");
        System.out.println("get796RequestToken="+ accessToken);
        System.out.println("-------------------------------------");
    }
    
    
     	
    
}

