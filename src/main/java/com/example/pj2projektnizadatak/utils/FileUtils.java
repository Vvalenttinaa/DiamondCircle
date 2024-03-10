package com.example.pj2projektnizadatak.utils;

import com.example.pj2projektnizadatak.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class FileUtils {

    private static final String brojIgreStr = System.getProperty("user.dir") + File.separator + "brojIgre.txt";

    public static void appendStrToFile(String fileName, String str) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
            out.write(str);
            out.close();
        } catch (IOException e) {
            StartApp.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
        }
    }

    public static void replaceStringInFile(File f1, String str1, String str2) {
        try {
            //  System.out.println("Trazi " + str1);
            //  System.out.println("Dodaje " + str2);
            List<String> lines = new ArrayList<String>();
            String line = null;
            //       File f1 = new File("d:/new folder/t1.htm");
            FileReader fr = new FileReader(f1);
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                if (line.contains(str1)) {
                    line = line.replace(str1, str2);
                    //                System.out.println(line);
                }
                lines.add(line + "\n");
            }
            fr.close();
            br.close();

            FileWriter fw = new FileWriter(f1);
            BufferedWriter out = new BufferedWriter(fw);
            for (String s : lines)
                out.write(s);
            out.flush();
            out.close();
        } catch (Exception ex) {
            StartApp.logger.log(Level.SEVERE, ex.fillInStackTrace().toString());
        }
    }

    public static int readBrojIgre() {
        File file = new File(brojIgreStr);
        int brojIgre = 0;
        if (file.exists()) {
            try {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String line;
                if ((line = br.readLine()) != null) {
                    brojIgre = Integer.valueOf(line);
                    brojIgre++;
                }
                fr.close();
                br.close();

                FileWriter fw = new FileWriter(file);
                BufferedWriter out = new BufferedWriter(fw);
                out.write(String.valueOf(brojIgre));
                out.flush();
                out.close();

            } catch (FileNotFoundException e) {
                StartApp.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
            } catch (IOException e) {
                StartApp.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
            }
        }
        return brojIgre;
    }
}
