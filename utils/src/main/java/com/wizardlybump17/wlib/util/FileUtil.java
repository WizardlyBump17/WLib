package com.wizardlybump17.wlib.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class FileUtil {

    public static void deleteFolder(File folder) {
        if (!folder.exists())
            return;

        final File[] files = folder.listFiles();
        if (files == null) {
            folder.delete();
            return;
        }
        for (File file : files)
            deleteFolder(file);

        folder.delete();
    }

    public static void write(File file, String string) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(string);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
