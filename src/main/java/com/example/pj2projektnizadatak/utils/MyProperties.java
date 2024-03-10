package com.example.pj2projektnizadatak.utils;

import com.example.pj2projektnizadatak.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class MyProperties {
    String filePath = "src/main/resources/config.properties";
   static Properties pros;

    public MyProperties() {

        pros = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            pros.load(fileInputStream);
        } catch (IOException e) {
            StartApp.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
        }
    }

    public static Properties getProperties(){
        return pros;
    }
}
