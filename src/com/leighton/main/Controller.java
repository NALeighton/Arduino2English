package com.leighton.main;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
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

    public void saveFile()
    {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT", "*.txt"));
        File file = chooser.showSaveDialog(new Stage());
        if(file != null)
        {
            try {
                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(explanationPane.getText());
                bufferedWriter.close();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
