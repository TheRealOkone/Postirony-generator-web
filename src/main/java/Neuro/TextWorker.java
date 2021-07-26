package Neuro;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.AbstractMap;
import java.util.Map;


public class TextWorker {
    private static final Integer minFontSize = 50;

    public void work(BufferedImage read, String text) {
        Graphics2D g = (Graphics2D) read.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        Integer fontSize = getFontSize("TimesRoman", text, read.getWidth());
        if (fontSize < minFontSize) {
            String[] elements = text.split(" ");
            StringBuilder firstElement = new StringBuilder();
            for (int i = 0; i < elements.length / 2; i++)
                firstElement.append(elements[i]).append(" ");
            StringBuilder secondElement = new StringBuilder();
            for (int i = elements.length / 2; i < elements.length; i++)
                secondElement.append(elements[i]).append(" ");
            int firstSize = getFontSize("TimesRoman", firstElement.toString(), read.getWidth());
            int secondSize = getFontSize("TimesRoman", secondElement.toString(), read.getWidth());
            Font realFont = firstSize <= secondSize ? new Font("TimesRoman", Font.PLAIN, firstSize) :
                    new Font("TimesRoman", Font.PLAIN, secondSize);
            int[] topXY = getXY(realFont, firstElement.toString(), read, Position.TOP);
            int[] bottomXY = getXY(realFont, secondElement.toString(), read, Position.BOTTOM);
            Map.Entry<Color, Integer> topColor = getColor(read, topXY[1]);
            Map.Entry<Color, Integer> bottomColor = getColor(read, bottomXY[1]);
            Color realColor = topColor.getValue() > bottomColor.getValue() ? topColor.getKey() : bottomColor.getKey();
            writeText(g, topXY[0], topXY[1], realColor, realFont, firstElement.toString());
            writeText(g, bottomXY[0], bottomXY[1], realColor, realFont, secondElement.toString());
        }
        else {
            Font font = new Font("TimesRoman", Font.PLAIN, fontSize);
            int[] XY = getXY(font, text, read, Position.BOTTOM);
            writeText(g, XY[0], XY[1], getColor(read, XY[1]).getKey(), font, text);
        }
        g.dispose();
    }

    private void writeText(Graphics2D g, int x, int y, Color color, Font font, String text) {
        g.setFont(font);
        g.setColor(color);
        g.drawString(text, x, y);
    }

    private int[] getXY(Font font, String text, BufferedImage read, Position position) {
        int[] result = new int[2];
        FontRenderContext frc = new FontRenderContext(null, true, true);
        Rectangle2D r2D = font.getStringBounds(text, frc);
        int rWidth = (int) Math.round(r2D.getWidth());
        result[0] = (read.getWidth() / 2) - (rWidth / 2);
        result[1] = read.getHeight();
        if (position == Position.TOP)
            result[1] = 40;
        if (position == Position.BOTTOM && read.getHeight() >= 10)
            result[1] = read.getHeight() - 10;
        return result;
    }

    private Map.Entry<Color, Integer> getColor(BufferedImage image, int y) {
        int black = 0;
        int white = 0;
        int clr;
        int red;
        int green;
        int blue;
        for (int newX = 1; newX < image.getWidth(); newX++) {
                for (int j = -10; j < 10; j++) {
                    int newY = y + j;
                    if (newY > 0 && newY < image.getHeight()) {
                        clr = image.getRGB(newX, newY);
                        red = (clr & 0x00ff0000) >> 16;
                        green = (clr & 0x0000ff00) >> 8;
                        blue = clr & 0x000000ff;
                        if (red >= 100 && green >= 100 && blue >= 100 && Math.abs(red - green) < 47 && Math.abs(red - blue) < 47 && Math.abs(blue - green) < 47)
                            black++;
                        else if (red >= 200 && green >= 200 && blue >= 200)
                            black++;
                        else if (red - blue > 100 && green - blue > 100)
                            black++;
                        else
                            white++;
                    }
                }
        }
        if (white >= black)
            return new AbstractMap.SimpleEntry<>(Color.WHITE, white - black);
        else
            return new AbstractMap.SimpleEntry<>(Color.black, black - white);
    }

    private Integer getFontSize(String fontS, String text, int widthImage) {
        FontRenderContext frc = new FontRenderContext(null, true, true);
        Font font;
        for (int size = 150; size > 0; size -= 5) {
            font = new Font(fontS, Font.PLAIN, size);
            if (widthImage - 15 > (int) Math.round(font.getStringBounds(text, frc).getWidth()))
                return size;
        }
        return 0;
    }

    private enum Position {
        BOTTOM,
        TOP
    }
}