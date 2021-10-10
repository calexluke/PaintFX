package com.calexluke;

import java.io.*;

public class PaintFxLogger {

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
                out.println(text);
            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }).start();

    }
}
