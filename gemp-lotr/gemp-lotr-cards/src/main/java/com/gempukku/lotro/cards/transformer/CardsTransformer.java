package com.gempukku.lotro.cards.transformer;

import com.gempukku.lotro.cards.packs.RarityReader;
import com.gempukku.lotro.cards.packs.SetRarity;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class CardsTransformer {
    private static final String format = "jpg";

    public static void main(String[] args) throws IOException {
        RarityReader reader = new RarityReader();
        final SetRarity setRarity = reader.getSetRarity("1");
        transformSet(setRarity);
    }

    private static void transformSet(SetRarity setRarity) throws IOException {
        for (String card : setRarity.getTengwarCards())
            downloadCard(card);

        for (String card : setRarity.getCardsOfRarity("R"))
            downloadCard(card);

        for (String card : setRarity.getCardsOfRarity("U"))
            downloadCard(card);

        for (String card : setRarity.getCardsOfRarity("C"))
            downloadCard(card);

        for (String card : setRarity.getCardsOfRarity("P"))
            downloadCard(card);

    }

    private static BufferedImage resizeBufferedImage(BufferedImage image, Dimension resultImageSize) {
        BufferedImage resultImage = new BufferedImage(resultImageSize.width, resultImageSize.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D gr = (Graphics2D) resultImage.getGraphics();
        try {
            gr.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            gr.drawImage(image, 0, 0, resultImageSize.width, resultImageSize.height, null);
        } finally {
            gr.dispose();
        }
        return resultImage;
    }

    private static void downloadCard(String card) throws IOException {
        int firstNonDigitIndex = getFirstNonDigitIndex(card);
        final String setNo = card.substring(0, firstNonDigitIndex);
        final String cardAfterSetNo = card.substring(firstNonDigitIndex + 1);
        final String address = "http://lotrtcgdb.com/images/LOTR" + fillWithZeroes(setNo, 2) + (cardAfterSetNo.endsWith("T") ? fillWithZeroes(cardAfterSetNo, 4) : fillWithZeroes(cardAfterSetNo, 3)) + ".jpg";
        System.out.println("Address: " + address);

        URL url = new URL(address);
        BufferedImage img = ImageIO.read(url);
        int maxDim = Math.max(img.getWidth(), img.getHeight());
        int borderWidth = maxDim / 30;
        Graphics gr = img.getGraphics();
        gr.setColor(Color.BLACK);
        for (int i = 0; i < borderWidth; i++)
            gr.drawRect(i, i, img.getWidth() - (2 * i) - 1, img.getHeight() - (2 * i) - 1);

        ImageWriter imageWriter = ImageIO.getImageWritersByFormatName(format).next();
        try {
            File folder = new File("/realhome/msciesinski/Desktop/lotr-images");
            folder.mkdirs();
            OutputStream outputFileStream = new FileOutputStream(new File(folder, card + "." + format));
            try {
                ImageOutputStream output = ImageIO.createImageOutputStream(outputFileStream);
                imageWriter.setOutput(output);
                try {
                    final ImageWriteParam defaultWriteParam = imageWriter.getDefaultWriteParam();
                    defaultWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    defaultWriteParam.setCompressionQuality(0.9f);
                    imageWriter.write(null, new IIOImage(img, null, null), defaultWriteParam);
                } finally {
                    output.close();
                }
            } finally {
                outputFileStream.close();
            }
//            OutputStream smallOutputFileStream = new FileOutputStream(new File(folder, card + "s." + format));
//            try {
//                ImageOutputStream output = ImageIO.createImageOutputStream(smallOutputFileStream);
//                imageWriter.setOutput(output);
//                try {
//                    final ImageWriteParam defaultWriteParam = imageWriter.getDefaultWriteParam();
//                    defaultWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
//                    defaultWriteParam.setCompressionQuality(0.9f);
//                    imageWriter.write(null, new IIOImage(resizeBufferedImage(img, new Dimension(img.getWidth()/2, img.getHeight()/2)), null, null), defaultWriteParam);
//                } finally {
//                    output.close();
//                }
//            } finally {
//                smallOutputFileStream.close();
//            }
        } finally {
            imageWriter.dispose();
        }
    }

    private static int getFirstNonDigitIndex(String string) {
        final char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++)
            if (!Character.isDigit(chars[i]))
                return i;
        return -1;
    }


    private static String fillWithZeroes(String no, int charsCount) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < charsCount - no.length(); i++)
            sb.append("0");
        sb.append(no);
        return sb.toString();
    }
}
