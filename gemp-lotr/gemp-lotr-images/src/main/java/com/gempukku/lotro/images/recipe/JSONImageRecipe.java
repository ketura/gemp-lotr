package com.gempukku.lotro.images.recipe;

import com.gempukku.lotro.images.ImageRecipe;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class JSONImageRecipe implements ImageRecipe {
    private int width;
    private int height;
    private List<LayerRecipe> layers = new LinkedList<>();
    private Map<Path, Image> imageCache = new HashMap<>();
    private Map<String, Function<RenderContext, ?>> functionMap;

    public JSONImageRecipe(File file) {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(file)) {
            final JSONObject recipe = (JSONObject) parser.parse(reader);
            width = ((Number) recipe.get("width")).intValue();
            height = ((Number) recipe.get("height")).intValue();

            functionMap = new HashMap<>();
            for (Map.Entry<String, JSONObject> function : (Set<Map.Entry<String, JSONObject>>) ((JSONObject) recipe.get("functions")).entrySet()) {
                final JSONObject functionObj = function.getValue();
                final String type = (String) functionObj.get("type");
                if (type.equalsIgnoreCase("boolean"))
                    functionMap.put(function.getKey(), createBooleanProvider(functionObj.get("function")));
            }

            JSONArray jsonLayers = (JSONArray) recipe.get("layers");
            for (JSONObject jsonLayer : (List<JSONObject>) jsonLayers) {
                layers.add(createLayerRecipe(jsonLayer));
            }
        } catch (IOException | ParseException e) {
            throw new RecipeGenerationException("Unable to generate recipe", e);
        }
    }

    private LayerRecipe createLayerRecipe(JSONObject jsonLayer) {
        String type = (String) jsonLayer.get("type");
        if (type.equalsIgnoreCase("image")) {
            Function<RenderContext, Path> pathProvider = createPathProvider(jsonLayer.get("path"));
            final Function<RenderContext, Integer> x = createIntProvider(jsonLayer.get("x"));
            final Function<RenderContext, Integer> y = createIntProvider(jsonLayer.get("y"));
            final Function<RenderContext, Integer> width = createIntProvider(jsonLayer.get("width"));
            final Function<RenderContext, Integer> height = createIntProvider(jsonLayer.get("height"));

            return new ImageLayerRecipe(
                    renderContext -> {
                        final Path imagePath = pathProvider.apply(renderContext);
                        return imageCache.computeIfAbsent(imagePath,
                                path -> {
                                    try {
                                        return ImageIO.read(path.toFile());
                                    } catch (IOException e) {
                                        throw new ImageGenerationException("Unable to load image: " + path, e);
                                    }
                                });
                    },
                    x, y, width, height);
        } else if (type.equalsIgnoreCase("textBox")) {
            final Function<RenderContext, String[]> text = createStringArrayProvider(jsonLayer.get("text"));
            JSONObject object = (JSONObject) jsonLayer.get("font");
            Map<String, Function<RenderContext, Font>> map = new HashMap<>();
            for (Map.Entry<String, Object> fontEntry : (Set<Map.Entry<String, Object>>) object.entrySet()) {
                map.put(fontEntry.getKey(), createFontProvider(fontEntry.getValue()));
            }
            Map<String, String> glyphMap = (JSONObject) jsonLayer.get("glyphs");
            Function<RenderContext, Paint> paint = createPaintProvider(jsonLayer.get("paint"), "black");

            Function<String, Function<RenderContext, Font>> fontStyleProvider = map::get;
            Function<RenderContext, TextBox> textBox = createTextBoxProvider(jsonLayer.get("box"));
            final Function<RenderContext, Float> minYStart = createFloatProvider(jsonLayer.get("minYStart"), 1f);
            Map<String, Number> yShiftsMap = (JSONObject) jsonLayer.get("yShifts");

            return new TextBoxLayerRecipe(
                    fontStyleProvider,
                    s -> glyphMap.get(s), paint,
                    s -> {
                        if (yShiftsMap == null)
                            return 0f;
                        final Number value = yShiftsMap.get(s);
                        if (value == null)
                            return 0f;
                        else
                            return value.floatValue();
                    },
                    text, textBox, minYStart);
        } else if (type.equalsIgnoreCase("rotate")) {
            final LayerRecipe layer = createLayerRecipe((JSONObject) jsonLayer.get("layer"));
            final Function<RenderContext, Integer> angle = createIntProvider(jsonLayer.get("angle"));
            final Function<RenderContext, Integer> x = createIntProvider(jsonLayer.get("x"));
            final Function<RenderContext, Integer> y = createIntProvider(jsonLayer.get("y"));
            return new RotateLayerRecipe(layer, angle, x, y);
        } else if (type.equalsIgnoreCase("conditional")) {
            Function<RenderContext, Boolean> condition = createBooleanProvider(jsonLayer.get("condition"));
            final List<LayerRecipe> values = getObjects(jsonLayer.get("values")).stream().map(value -> createLayerRecipe(value)).collect(toList());

            return (renderContext, graphics) -> {
                if (condition.apply(renderContext)) {
                    for (LayerRecipe value : values)
                        value.renderLayer(renderContext, graphics);
                }
            };
        }
        throw new RecipeGenerationException("Unable to find layer recipe of type: " + type);
    }

    private Function<RenderContext, String[]> createStringArrayProvider(Object value) {
        if (value instanceof JSONObject) {
            JSONObject valueObj = (JSONObject) value;
            final String type = (String) valueObj.get("type");
            if (type.equalsIgnoreCase("string")) {
                final Function<RenderContext, String> stringProvider = createStringProvider(valueObj.get("value"));

                return renderContext -> {
                    final String stringValue = stringProvider.apply(renderContext);
                    if (stringValue == null)
                        return new String[0];
                    return new String[]{stringValue};
                };
            } else if (type.equalsIgnoreCase("append")) {
                final JSONArray values = (JSONArray) valueObj.get("values");

                List<Function<RenderContext, String[]>> providers = (List<Function<RenderContext, String[]>>) values.stream().map(appendValue -> createStringArrayProvider(appendValue)).collect(Collectors.toList());

                return (renderContext -> {
                    List<String> result = new LinkedList<>();
                    for (Function<RenderContext, String[]> provider : providers) {
                        final String[] texts = provider.apply(renderContext);
                        if (texts != null)
                            result.addAll(Arrays.asList(texts));
                    }
                    return result.toArray(new String[0]);
                });
            } else if (type.equalsIgnoreCase("cardProperty")) {
                final Function<RenderContext, String> name = createStringProvider(valueObj.get("name"));
                final String prefix = (String) valueObj.get("prefix");
                final String postfix = (String) valueObj.get("postfix");
                return renderContext -> {
                    final String propertyName = name.apply(renderContext);

                    final String[] stringArray = getStringArray(renderContext.getCardInfo().get(propertyName));
                    for (int i = 0; i < stringArray.length; i++)
                        stringArray[i] = ((prefix != null) ? prefix : "") + stringArray[i] + ((postfix != null) ? postfix : "");
                    return stringArray;
                };
            }
        }
        throw new RecipeGenerationException("Unable to find string array recipe of type: " + value);
    }

    private List<JSONObject> getObjects(Object values) {
        if (values instanceof JSONObject)
            return Collections.singletonList((JSONObject) values);
        else if (values instanceof JSONArray)
            return (List<JSONObject>) values;
        throw new RecipeGenerationException("Unable to resolve objects: " + values);
    }

    private Function<RenderContext, Paint> createPaintProvider(Object paint, Object defaultValue) {
        Object paintVal = (paint != null) ? paint : defaultValue;

        if (paintVal instanceof String)
            return renderContext -> {
                try {
                    return (Color) Color.class.getDeclaredField(((String) paintVal).toUpperCase()).get(null);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    throw new ImageGenerationException("Unable to get color from: " + paintVal);
                }
            };

        throw new RecipeGenerationException("Unable to find paint recipe of type: " + paintVal);
    }

    private Function<RenderContext, TextBox> createTextBoxProvider(Object box) {
        if (box instanceof JSONObject) {
            JSONObject boxObj = (JSONObject) box;
            final String type = (String) boxObj.get("type");
            if (type.equalsIgnoreCase("box")) {
                final Function<RenderContext, Integer> x = createIntProvider(boxObj.get("x"));
                final Function<RenderContext, Integer> y = createIntProvider(boxObj.get("y"));
                final Function<RenderContext, Integer> width = createIntProvider(boxObj.get("width"));
                final Function<RenderContext, Integer> height = createIntProvider(boxObj.get("height"));
                final Function<RenderContext, String> horizontalAlignment = createStringProvider(boxObj.get("horAlign"));

                return renderContext -> new TextBox() {
                    @Override
                    public int getX() {
                        return x.apply(renderContext);
                    }

                    @Override
                    public int getY() {
                        return y.apply(renderContext);
                    }

                    @Override
                    public int getWidth() {
                        return width.apply(renderContext);
                    }

                    @Override
                    public int getHeight() {
                        return height.apply(renderContext);
                    }

                    @Override
                    public HorizontalAlignment getHorizontalAlignment() {
                        final String align = horizontalAlignment.apply(renderContext);
                        if (align == null || align.equalsIgnoreCase("left"))
                            return (availableWidth, usedWidth) -> 0;
                        else if (align.equalsIgnoreCase("center"))
                            return (availableWidth, usedWidth) -> (availableWidth - usedWidth) / 2;
                        else if (align.equalsIgnoreCase("right"))
                            return (availableWidth, usedWidth) -> (availableWidth - usedWidth);

                        throw new ImageGenerationException("Unable to recognize horizontal alignment: " + align);
                    }
                };
            }
        }
        throw new RecipeGenerationException("Unable to find text box recipe of type: " + box);
    }

    private Function<RenderContext, Font> createFontProvider(Object font) {
        if (font instanceof JSONObject) {
            JSONObject fontObj = (JSONObject) font;
            final String type = (String) fontObj.get("type");
            if (type.equalsIgnoreCase("ttf")) {
                Function<RenderContext, Path> pathProvider = createPathProvider(fontObj.get("path"));
                Function<RenderContext, Float> sizeProvider = createFloatProvider(fontObj.get("size"));
                Function<RenderContext, Integer> styleProvider = createIntProvider(fontObj.get("style"));

                return renderContext -> {
                    final Path path = pathProvider.apply(renderContext);
                    final float size = sizeProvider.apply(renderContext);
                    final int style = styleProvider.apply(renderContext);
                    try {
                        return Font.createFont(Font.TRUETYPE_FONT, path.toFile()).deriveFont(style, size);
                    } catch (FontFormatException | IOException e) {
                        throw new ImageGenerationException("Unable to get font", e);
                    }
                };
            }
        }
        throw new RecipeGenerationException("Unable to find font recipe: " + font);
    }

    private Function<RenderContext, Path> createPathProvider(Object path) {
        if (path instanceof String) {
            return renderContext -> Paths.get((String) path);
        } else if (path instanceof JSONObject) {
            JSONObject pathObj = (JSONObject) path;
            final String type = (String) pathObj.get("type");
            if (type.equalsIgnoreCase("property")) {
                Function<RenderContext, String> propertyName = createStringProvider(pathObj.get("name"));
                return renderContext ->
                        Paths.get(renderContext.getProperties().getProperty(
                                propertyName.apply(renderContext)));
            } else if (type.equalsIgnoreCase("resolve")) {
                final Function<RenderContext, Path> parent = createPathProvider(pathObj.get("parent"));
                final Function<RenderContext, String> child = createStringProvider(pathObj.get("child"));
                return renderContext -> {
                    final Path path1 = parent.apply(renderContext);
                    final String childStr = child.apply(renderContext);
                    return path1.resolve(childStr);
                };
            }
        }
        throw new RecipeGenerationException("Unable to recognize path type: " + path);
    }

    private Function<RenderContext, String> createStringProvider(Object string) {
        return createStringProvider(string, null);
    }

    private Function<RenderContext, String> createStringProvider(Object string, String defaultValue) {
        if (string == null)
            return renderContext -> defaultValue;
        if (string instanceof String) {
            return renderContext -> (String) string;
        } else if (string instanceof JSONObject) {
            JSONObject stringObj = (JSONObject) string;
            final String type = (String) stringObj.get("type");
            if (type.equalsIgnoreCase("property")) {
                Function<RenderContext, String> propertyName = createStringProvider(stringObj.get("name"));
                return renderContext ->
                        renderContext.getProperties().getProperty(
                                propertyName.apply(renderContext));
            } else if (type.equalsIgnoreCase("appendText")) {
                final JSONArray values = (JSONArray) stringObj.get("values");
                final List<Function<RenderContext, String>> list = (List<Function<RenderContext, String>>) values.stream().map(text -> createStringProvider(text)).collect(toList());

                return renderContext -> {
                    StringBuilder sb = new StringBuilder();
                    for (Function<RenderContext, String> valueProvider : list) {
                        final String textValue = valueProvider.apply(renderContext);
                        if (textValue != null)
                            sb.append(textValue);
                    }
                    return sb.toString();
                };
            } else if (type.equalsIgnoreCase("map")) {
                final Function<RenderContext, String> key = createStringProvider(stringObj.get("key"));
                Map<String, String> map = (Map<String, String>) stringObj.get("map");
                return renderContext -> {
                    final String mapKey = key.apply(renderContext);
                    final String value = map.get(mapKey);
                    if (value == null)
                        throw new ImageGenerationException("Missing value in map for key: " + mapKey);
                    return value;
                };
            } else if (type.equalsIgnoreCase("replace")) {
                final Function<RenderContext, String> source = createStringProvider(stringObj.get("source"));
                final Function<RenderContext, String> match = createStringProvider(stringObj.get("match"));
                final Function<RenderContext, String> with = createStringProvider(stringObj.get("with"));

                return renderContext -> source.apply(renderContext).replace(match.apply(renderContext), with.apply(renderContext));
            } else if (type.equalsIgnoreCase("capitalize")) {
                final Function<RenderContext, String> source = createStringProvider(stringObj.get("value"));

                return renderContext -> {
                    final String text = source.apply(renderContext);
                    final String[] textSplit = text.split(" ");
                    for (int i = 0; i < textSplit.length; i++) {
                        textSplit[i] = textSplit[i].substring(0, 1).toUpperCase() + textSplit[i].substring(1);
                    }
                    return String.join(" ", textSplit);
                };
            } else if (type.equalsIgnoreCase("cardProperty")) {
                final Function<RenderContext, String> name = createStringProvider(stringObj.get("name"));

                return renderContext -> {
                    final String propertyName = name.apply(renderContext);
                    final Object value = renderContext.getCardInfo().get(propertyName);
                    if (value == null)
                        return null;
                    else if (value instanceof String)
                        return (String) value;
                    else if (value instanceof Number)
                        return String.valueOf(((Number) value).intValue());
                    throw new ImageGenerationException("Unable to get card property: " + propertyName + ", unknown type: " + value);
                };
            } else if (type.equalsIgnoreCase("cardPropertyValueIn")) {
                final Function<RenderContext, String> name = createStringProvider(stringObj.get("name"));
                final JSONArray values = (JSONArray) stringObj.get("values");
                final List<Function<RenderContext, String>> list = (List<Function<RenderContext, String>>) values.stream().map(value -> createStringProvider(value)).collect(toList());

                return renderContext -> {
                    final String propertyName = name.apply(renderContext);
                    final String[] propertyValues = getStringArray(renderContext.getCardInfo().get(propertyName));
                    for (Function<RenderContext, String> renderContextStringFunction : list) {
                        final String acceptable = renderContextStringFunction.apply(renderContext);
                        for (String propertyValue : propertyValues) {
                            if (propertyValue.equalsIgnoreCase(acceptable))
                                return acceptable;
                        }
                    }
                    return null;
                };
            } else if (type.equalsIgnoreCase("append")) {
                final JSONArray values = (JSONArray) stringObj.get("values");
                final List<Function<RenderContext, String>> list = (List<Function<RenderContext, String>>) values.stream().map(value -> createStringProvider(value)).collect(toList());

                return renderContext -> {
                    StringBuilder sb = new StringBuilder();
                    for (Function<RenderContext, String> valueProvider : list) {
                        final String value = valueProvider.apply(renderContext);
                        if (value != null)
                            sb.append(value);
                    }
                    return sb.toString();
                };
            } else if (type.equalsIgnoreCase("optional")) {
                Function<RenderContext, Boolean> condition = createBooleanProvider(stringObj.get("condition"));
                final Function<RenderContext, String> value = createStringProvider(stringObj.get("value"));

                return renderContext -> {
                    if (condition.apply(renderContext))
                        return value.apply(renderContext);
                    return null;
                };
            }
        }
        throw new RecipeGenerationException("Unable to recognize String: " + string);
    }

    private Function<RenderContext, Boolean> createBooleanProvider(Object condition) {
        return createBooleanProvider(condition, null);
    }

    private Function<RenderContext, Boolean> createBooleanProvider(Object condition, Boolean defaultValue) {
        if (condition == null && defaultValue != null)
            return renderContext -> defaultValue;
        else if (condition instanceof Boolean)
            return renderContext -> (Boolean) condition;
        else if (condition instanceof JSONObject) {
            JSONObject conditionObj = (JSONObject) condition;
            final String type = (String) conditionObj.get("type");
            if (type.equalsIgnoreCase("cardHasProperty")) {
                final Function<RenderContext, String> name = createStringProvider(conditionObj.get("name"));

                return renderContext -> renderContext.getCardInfo().get(name.apply(renderContext)) != null;
            } else if (type.equalsIgnoreCase("cardHasPropertyValueIn")) {
                final Function<RenderContext, String> name = createStringProvider(conditionObj.get("name"));
                final JSONArray values = (JSONArray) conditionObj.get("values");
                final List<Function<RenderContext, String>> list = (List<Function<RenderContext, String>>) values.stream().map(value -> createStringProvider(value)).collect(toList());

                return renderContext -> {
                    String[] propertyValues = getStringArray(renderContext.getCardInfo().get(name.apply(renderContext)));
                    for (Function<RenderContext, String> renderContextStringFunction : list) {
                        final String acceptable = renderContextStringFunction.apply(renderContext);
                        for (String propertyValue : propertyValues) {
                            if (propertyValue.equalsIgnoreCase(acceptable))
                                return true;
                        }
                    }
                    return false;
                };
            } else if (type.equalsIgnoreCase("cardPropertyGreaterThanZero")) {
                final Function<RenderContext, String> name = createStringProvider(conditionObj.get("name"));

                return renderContext -> ((Number) renderContext.getCardInfo().get(name.apply(renderContext))).floatValue() > 0;
            } else if (type.equalsIgnoreCase("not")) {
                final Function<RenderContext, Boolean> opposite = createBooleanProvider(conditionObj.get("condition"));

                return renderContext -> !opposite.apply(renderContext);
            } else if (type.equalsIgnoreCase("or")) {
                final JSONArray values = (JSONArray) conditionObj.get("conditions");
                final List<Function<RenderContext, Boolean>> list = (List<Function<RenderContext, Boolean>>) values.stream().map(value -> createBooleanProvider(value)).collect(Collectors.toList());

                return renderContext -> {
                    for (Function<RenderContext, Boolean> oneCondition : list) {
                        if (oneCondition.apply(renderContext))
                            return true;
                    }
                    return false;
                };
            } else if (type.equalsIgnoreCase("function")) {
                final Function<RenderContext, String> name = createStringProvider(conditionObj.get("name"));
                return renderContext -> (Boolean) functionMap.get(name.apply(renderContext)).apply(renderContext);
            }
        }
        throw new RecipeGenerationException("Unable to recognize boolean: " + condition);
    }

    private String[] getStringArray(Object o) {
        if (o == null)
            return new String[0];
        else if (o instanceof String)
            return new String[]{(String) o};
        else if (o instanceof JSONArray)
            return ((List<String>) o).toArray(new String[0]);
        throw new RecipeGenerationException("Unable to recognize string array: " + o);
    }

    private Function<RenderContext, Float> createFloatProvider(Object value) {
        return createFloatProvider(value, null);
    }

    private Function<RenderContext, Float> createFloatProvider(Object value, Float defaultValue) {
        if (value == null && defaultValue != null)
            return renderContext -> defaultValue;
        if (value instanceof Number) {
            return renderContext -> ((Number) value).floatValue();
        } else if (value instanceof JSONObject) {
            final JSONObject valueObj = (JSONObject) value;
            final String type = (String) valueObj.get("type");
            if (type.equals("property")) {
                Function<RenderContext, String> propertyName = createStringProvider(valueObj.get("name"));
                return renderContext ->
                        Float.parseFloat(renderContext.getProperties().getProperty(
                                propertyName.apply(renderContext)));
            }
        }
        throw new RecipeGenerationException("Unable to recognize float: " + value);
    }

    private Function<RenderContext, Integer> createIntProvider(Object value) {
        if (value instanceof Number) {
            return renderContext -> ((Number) value).intValue();
        } else if (value instanceof JSONObject) {
            final JSONObject valueObj = (JSONObject) value;
            final String type = (String) valueObj.get("type");
            if (type.equalsIgnoreCase("conditional")) {
                final Function<RenderContext, Boolean> condition = createBooleanProvider(valueObj.get("condition"));
                final Function<RenderContext, Integer> aTrue = createIntProvider(valueObj.get("true"));
                final Function<RenderContext, Integer> aFalse = createIntProvider(valueObj.get("false"));

                return renderContext -> condition.apply(renderContext) ? aTrue.apply(renderContext) : aFalse.apply(renderContext);
            }
        }

        throw new RecipeGenerationException("Unable to recognize int: " + value);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void renderImage(Properties properties, JSONObject cardInfo, BufferedImage image) {
        RenderContext context = new RenderContext() {
            @Override
            public Properties getProperties() {
                return properties;
            }

            @Override
            public JSONObject getCardInfo() {
                return cardInfo;
            }
        };
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        try {
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            for (LayerRecipe layer : layers) {
                layer.renderLayer(context, graphics);
            }
        } finally {
            graphics.dispose();
        }
    }
}
