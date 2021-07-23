package controller;

import Neuro.*;
import interface_.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.parse.Parser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
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
    public byte[] gone() throws IOException {
        File image = null;
        try {
            image = images.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BufferedImage bufferedImage = ImageIO.read(image);

        WritableRaster raster = bufferedImage .getRaster();
        DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();
        sus = data.getData();

        System.out.println("asddd31222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222dddddddd\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        return sus;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String retpage(){
        return "index";
    }
}
