package com.gempukku.lotro.cards.downloader;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class SetInformationDownloader {
    public static void main(String[] args) throws Exception {
        for (int i = 1; i < 20; i++) {
            String setNo = fillWithZeroes(String.valueOf(i), 2);
            File destination = new File("c:\\lotr\\" + setNo);
            destination.mkdirs();

            int j = 1;
            while (true) {
                try {
                    String cardNo = fillWithZeroes(String.valueOf(j), 3);
                    String pageAddress = "http://lotrtcgdb.com/pages/LOTR" + setNo + cardNo + ".html";
                    System.out.println("Page address: " + pageAddress);
                    FileOutputStream fos = new FileOutputStream(new File(destination, "LOTR" + setNo + cardNo + ".html"));
                    try {
                        InputStream is = new URL(pageAddress).openStream();
                        try {
                            IOUtils.copyLarge(is, fos);
                        } finally {
                            is.close();
                        }
                    } finally {
                        fos.close();
                    }
                    j++;
                } catch (FileNotFoundException exp) {
                    break;
                }
            }
        }
    }

    private static String fillWithZeroes(String no, int charsCount) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < charsCount - no.length(); i++)
            sb.append("0");
        sb.append(no);
        return sb.toString();
    }
}
