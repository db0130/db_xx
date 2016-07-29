package com.jsb.util;

import java.util.ResourceBundle;

public class JsbAppResources{
    private static ResourceBundle resource;

    static{
        resource = ResourceBundle.getBundle("JlAppResources");
    }

    public static String getProperty(String key){
        return resource.getString(key);
    }

    public static boolean getBooleanProperty(String key,boolean default_value){
        try{
            return new Boolean(JsbAppResources.getProperty(key)).booleanValue();
        }catch(Exception e){
            return default_value;
        }
    }
}
