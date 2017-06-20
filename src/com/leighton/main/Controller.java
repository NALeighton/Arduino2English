package com.leighton.main;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
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
            ArrayList<String> code = new ArrayList<>();
            while(input.hasNext())
            {
                String in = input.nextLine();
                tmp += (i + ":\t" + in + "\n");
                code.add(in);
                i++;
            }
            arduinoPane.setText(tmp);
            input.close();
            explanationPane.setText(parseCode(code));
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

    private String parseCode(ArrayList<String> code)
    {
        ArrayList<String> explanation = new ArrayList<>();
        for(int i = 0; i < code.size(); i++)
        {
            if(code.get(i).contains("void"))
            {
                explanation.add(i,"Function: " + code.get(i).substring(code.get(i).indexOf(" "),code.get(i).indexOf("(")));
            }
            else if(code.get(i).contains("Serial.begin"))
            {
                explanation.add(i,"Serial port initialized at a baud rate of " + code.get(i).substring(code.get(i).indexOf("(")+1,code.get(i).indexOf(")")) + "bits/sec");
            }
            else if(code.get(i).contains("Serial.println"))
            {
                explanation.add(i,code.get(i).substring(code.get(i).indexOf("(")+1,code.get(i).indexOf(")")) + " Sent via Serial port with newline character appended");
            }
            else if(code.get(i).contains("//"))
            {
                explanation.add(i,"Comment");
            }
            else
            {
                explanation.add(i,"Things go in and things come back out... in other words, magic occurs!");
            }
        }
        String tmp = "";
        for(int i = 0; i < explanation.size(); i++)
        {
            tmp += i+1 + ": " + explanation.get(i) + "\n";
        }
        return tmp;
    }
}
