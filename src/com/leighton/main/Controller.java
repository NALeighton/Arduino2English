package com.leighton.main;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Controller {

    @FXML
    private TextArea arduinoPane = new TextArea();

    @FXML
    private TextArea explanationPane = new TextArea();

    public void openFile() throws FileNotFoundException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arduino Code", "*.ino"));
        File file = chooser.showOpenDialog(new Stage());
        if(file !=  null)
        {
            Scanner input = new Scanner(file);
            String tmp = "";
            int i = 1;
            while(input.hasNext())
            {
                tmp += (i + ":\t" + input.nextLine() + "\n");
                i++;
            }
            arduinoPane.setText(tmp);
        }
    }
}
