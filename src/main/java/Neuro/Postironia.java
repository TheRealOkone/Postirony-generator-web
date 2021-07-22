package Neuro;

import interface_.Gui;
import ru.parse.BalabobaParser;
import ru.parse.Parser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

public class Postironia {
    public void oldmain(Parser parser) {
        File ZN = new File("ZN");
        deleteDirectory(ZN);
        ZN.mkdir();
        int requestIndex = 0;
        try {
            while (true) {
                File fRequest = parser.qtxt.take();
                File filePicture = parser.qjpg.take();
                String request;
                try (BufferedReader reader = new BufferedReader(new FileReader(fRequest))) {
                    request = reader.readLine();
                }
                fRequest.delete();
                String text = crop(BalabobaParser.getText(request));
                System.out.println(text);
                BufferedImage read;
                try {
                    read = ImageIO.read(filePicture);
                    Graphics2D g = (Graphics2D) read.getGraphics();
                    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
                    Font font = getFont("TimesRoman", text, read.getWidth());
                    g.setFont(font);
                    FontRenderContext frc = new FontRenderContext(null, true, true);
                    Rectangle2D r2D = font.getStringBounds(text, frc);
                    int rWidth = (int) Math.round(r2D.getWidth());
                    int x = (read.getWidth() / 2) - (rWidth / 2);
                    int y = read.getHeight();
                    if (read.getHeight() > 10)
                        y = read.getHeight() - 10;
                    g.setColor(getColor(read, x, y));
                    g.drawString(text, x, y);
                    g.dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                    filePicture.delete();
                    continue;
                }
                String imageName = "ZN\\image" + requestIndex +".jpg";
                requestIndex++;
                if (requestIndex == 500)
                    requestIndex = 0;
                File resultImage = new File(imageName);
                ImageIO.write(read, "jpg", resultImage);
                filePicture.delete();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Color getColor(BufferedImage image, int x, int y) {
        int black = 0;
        int white = 0;
        int clr;
        int red;
        int green;
        int blue;
        for (int i = -10; i < 10; i++) {
            int newX = x + i;
            if (newX > 0 && newX < image.getWidth())
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
            return Color.WHITE;
        else
            return Color.BLACK;
    }

    private static void deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                File f = new File(dir, aChildren);
                deleteDirectory(f);
            }
            dir.delete();
        } else dir.delete();
    }

    private static String crop(String text) {
        String[] strs = text.split("-");
        String description;
        if (strs.length > 1)
            description = strs[1];
        else
            description = strs[0];
        String[] firstDescriptions = description.split("\\.")[0].split(" ");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 5 && i < firstDescriptions.length; i++)
            result.append(firstDescriptions[i]).append(" ");
        return result.toString();
    }

    private static Font getFont(String fontS, String text, int widthImage) {
        FontRenderContext frc = new FontRenderContext(null, true, true);
        Font font = null;
        for (int size = 200; size > 0; size -= 15) {
            font = new Font(fontS, Font.PLAIN, size);
            if (widthImage - 15 > (int) Math.round(font.getStringBounds(text, frc).getWidth()))
                return font;
        }
        return font;
    }
}

