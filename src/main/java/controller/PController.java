package controller;

import Neuro.*;
import interface_.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.parse.Parser;
import org.apache.commons.codec.binary.Base64;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.LinkedBlockingQueue;

@Controller
public class PController {
    static {
        Runtime.getRuntime().loadLibrary("TNParser");
    }
    byte[] sus;
    public LinkedBlockingQueue<File> images = new LinkedBlockingQueue<File>();

    public PController(String[] args){

        Runnable task1 = () -> {
            Parser parser = new Parser();
            Postironia post = new Postironia();
            post.oldmain(parser,this);
        };
        Thread thread1 = new Thread(task1);
        thread1.setDaemon(true);
        thread1.start();



    }



    @RequestMapping(value = "/g1", method = RequestMethod.GET)
    @ResponseBody
    public String gone() throws IOException, JSONException {
        File image = null;
        try {
            image = images.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BufferedImage bufferedImage = ImageIO.read(image);
        int h = bufferedImage.getHeight();
        int w = bufferedImage.getWidth();
        sus = Files.readAllBytes(image.toPath());
        String test = new String(Base64.encodeBase64(sus), "UTF-8");
        System.out.println("asddd31222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222dddddddd\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        JSONObject jsonComplex = new JSONObject();
        jsonComplex.put("height", h);
        jsonComplex.put("width", w);
        jsonComplex.put("image", test);
        return jsonComplex.toString();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String retpage(){
        return "index";
    }
}
