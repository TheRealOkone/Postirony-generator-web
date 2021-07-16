package controller;

import Neuro.Postironia;
import interface_.Gui;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import ru.parse.Parser;
@Component
public class PController {
    public PController(String[] args){
        Parser parser = new Parser();
        Gui gui = new Gui();
        Postironia post = new Postironia();
        Runnable task1 = () -> post.oldmain(parser,gui);
        Thread thread1 = new Thread(task1);
        thread1.start();
    }
}
