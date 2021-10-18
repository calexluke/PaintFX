package com.calexluke;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaintFxLogger {

    /**
     * Handles saving log messages to a text file
     */

    /**
     * Clear log file for a fresh log each run of the program
     */
    public void clearLogFile() {
        // file I/O in new thread
        new Thread(() -> {
            String pathName = Constants.LOGS_FILE_PATH;
            try {
                File myObj = new File(pathName);
                myObj.createNewFile();
                FileWriter myWriter = new FileWriter(pathName);
                myWriter.write("");
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }).start();
    }

    public void writeToLog(String text) {
        // file I/O in new thread
        new Thread(() -> {
            String pathName = Constants.LOGS_FILE_PATH;
            try(FileWriter fw = new FileWriter(pathName, true);
                // buffered writer for performance improvements
                BufferedWriter bw = new BufferedWriter(fw);
                // print writer allows println syntax
                PrintWriter out = new PrintWriter(bw))
            {
                String timeStamp = getCurrentTimeStamp();
                out.println(timeStamp + " " + text);
            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }).start();

    }

    private String getCurrentTimeStamp() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
