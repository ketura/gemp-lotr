package com.gempukku.lotro.images;

import com.gempukku.lotro.images.recipe.ImageGenerationException;
import com.gempukku.lotro.images.recipe.JSONImageRecipe;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ImageGenerator {
    private Properties properties;
    private JSONImageRecipe jsonImageRecipe;

    public ImageGenerator(Properties properties, File recipeFile) {
        this.properties = properties;
        this.jsonImageRecipe = new JSONImageRecipe(recipeFile);
    }

    public void generateImagesFromCardsFile(File file) {
        File output = new File(properties.getProperty("output.folder"));
        output.mkdirs();
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(file)) {
            final JSONObject cardsObject = (JSONObject) parser.parse(reader);
            for (Map.Entry<String, JSONObject> cardEntry : (Set<Map.Entry<String, JSONObject>>) cardsObject.entrySet()) {
                final String cardId = cardEntry.getKey();
                if (cardId.equals("40_3") || cardId.equals("40_17") || cardId.equals("40_19")) {
                    BufferedImage bufferedImage = new BufferedImage(jsonImageRecipe.getWidth(), jsonImageRecipe.getHeight(), BufferedImage.TYPE_INT_RGB);
                    jsonImageRecipe.renderImage(properties, cardEntry.getValue(), bufferedImage);

                    JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
                    jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    jpegParams.setCompressionQuality(1f);

                    final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
                    final File resultFile = new File(output, cardId + ".jpg");
                    try (FileImageOutputStream outputStream = new FileImageOutputStream(resultFile)) {
                        writer.setOutput(outputStream);
                        writer.write(null, new IIOImage(bufferedImage, null, null), jpegParams);
                    }
                }
            }
        } catch (IOException | ParseException e) {
            throw new ImageGenerationException("Unable to generate image", e);
        }
    }

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        try (Reader reader = new FileReader(args[0])) {
            properties.load(reader);
        }

        ImageGenerator generator = new ImageGenerator(properties, new File(properties.getProperty("recipe.file")));
        File folder = new File(args[1]);
        for (File file : folder.listFiles()) {
            generator.generateImagesFromCardsFile(file);
        }
    }
}
