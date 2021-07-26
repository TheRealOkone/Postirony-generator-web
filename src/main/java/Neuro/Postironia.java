package Neuro;

import controller.PController;
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
    public void oldmain(Parser parser, PController controller) {
        TextWorker worker = new TextWorker();
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
                String text = crop(BalabobaParser.getText(request));
                System.out.println(text);
                BufferedImage read;
                try {
                    read = ImageIO.read(filePicture);
                    worker.work(read, text);
                } catch (Exception e) {
                    e.printStackTrace();
                    fRequest.delete();
                    filePicture.delete();
                    continue;
                }
                String imageName = "ZN\\image" + requestIndex +".jpg";
                requestIndex++;
                if (requestIndex == 500)
                    requestIndex = 0;
                File resultImage = new File(imageName);
                ImageIO.write(read, "jpg", resultImage);
                controller.images.put(resultImage);
                fRequest.delete();
                filePicture.delete();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
        String shortText = text.split("\\.")[0].split(" - ")[0].split(",")[0];
        String[] questionSplit = shortText.split("\\?");
        if (questionSplit.length > 1)
            shortText = questionSplit[0] + "?";
        else shortText = questionSplit[0];
        String[] exclamationSplit = shortText.split("\\?");
        if (exclamationSplit.length > 1)
            shortText = exclamationSplit[0] + "!";
        else shortText = exclamationSplit[0];
//        String[] cropText = shortText.split(" ");
//        StringBuilder result = new StringBuilder();
//        for (int i = 0; i < 7 && i < cropText.length; i++)
//            result.append(cropText[i]).append(" ");
        return shortText;
    }
}

