package controller;

import Neuro.*;
import interface_.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.parse.Parser;
@Controller
public class PController {
    public PController(String[] args){
        Parser parser = new Parser();
        Gui gui = new Gui();
        Postironia post = new Postironia();
        Runnable task1 = () -> post.oldmain(parser,gui);
        Thread thread1 = new Thread(task1);
        thread1.start();
    }

    @RequestMapping(value = "/g1", method = RequestMethod.GET)
    public String gone() {
        return "bruh";
    }

    @RequestMapping(value = "/g2", method = RequestMethod.GET)
    public String gtwo() {
        return "cringe";
    }
}
