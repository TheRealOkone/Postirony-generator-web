package ru.parse;
import org.testng.annotations.Test;

import java.io.IOException;

public class BalabobaParserTest {
    @Test
    public void balabobaTest() throws IOException {

        for(int i=0; i<10;i++){
            System.out.println(BalabobaParser.getText("Цифра" + i));
        }
    }


}