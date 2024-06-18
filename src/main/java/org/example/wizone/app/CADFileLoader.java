package org.example.wizone.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class CADFileLoader {

    private File cadFile;
    private File svgFile;

    public void loadCADFile(File file) throws IOException {

        if (file.getName().endsWith(".dwg")) {
            this.cadFile = file;
        } else {
            throw new IllegalArgumentException("Invalid file type. Only DWG files are supported.");
        }
    }




    public void loadSVGFile(File file) throws IOException {
        if (file.getName().endsWith(".svg")) {
            this.svgFile = file;
        } else {
            throw new IllegalArgumentException("Invalid file type. Only SVG files are supported.");
        }
    }

    public String getSVGFilePath() {
        if (svgFile != null) {
            return svgFile.getAbsolutePath();
        } else {
            return "";
        }
    }

    public File getCADFile() {
        return cadFile;
    }


    public File getSVGFile() {
        return svgFile;
    }

    public void copyFile(File source, File dest) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destinationChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destinationChannel = new FileOutputStream(dest).getChannel();
            destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } finally {
            if (sourceChannel != null) {
                sourceChannel.close();
            }
            if (destinationChannel != null) {
                destinationChannel.close();
            }
        }
    }
}