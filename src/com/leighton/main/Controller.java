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

    ArrayList<String> variables = new ArrayList<>();

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
    private boolean variable(String in)
    {
        boolean tmp = false;
        if(variables.size() > 0)
        {
            for(int i = 0; i < variables.size(); i++)
            {
                if(in.startsWith(variables.get(i)))
                {
                    tmp = true;
                }
            }
        }
        return tmp;
    }

    private String parseCode(ArrayList<String> code)
    {
        ArrayList<String> explanation = new ArrayList<>();
        boolean blockComment = false;
        for(int i = 0; i < code.size(); i++)
        {
            System.out.println(i); //debug
            code.set(i,code.get(i).trim());
            if(code.get(i).startsWith("*/"))
            {
                explanation.add(i,"Block comment end");
                blockComment = false;
            }
            else if(blockComment)
            {
                explanation.add(i,"comment content");
            }
            else if(code.get(i).startsWith("void"))
            {
                explanation.add(i,"Function: " + code.get(i).substring(code.get(i).indexOf(" "),code.get(i).indexOf("(")) + " with parameters: " + code.get(i).substring(code.get(i).indexOf("(")+1,code.get(i).indexOf(")")));
            }
            else if(code.get(i).startsWith("for"))
            {
                explanation.add(i,"For loop");
            }
            else if(code.get(i).startsWith("Serial.begin"))
            {
                explanation.add(i,"Serial port initialized at a baud rate of " + code.get(i).substring(code.get(i).indexOf("(")+1,code.get(i).indexOf(")")) + "bits/sec");
            }
            else if(code.get(i).startsWith("Serial.println"))
            {
                explanation.add(i,code.get(i).substring(code.get(i).indexOf("(")+1,code.get(i).indexOf(")")) + " Sent via Serial port with newline character appended");
            }
            else if(code.get(i).startsWith("Serial.print"))
            {
                explanation.add(i,code.get(i).substring(code.get(i).indexOf("(")+1,code.get(i).indexOf(")")) + " Sent via Serial port without newline character appended");
            }
            else if(code.get(i).startsWith("//"))
            {
                explanation.add(i,"Comment");
            }
            else if(code.get(i).startsWith("/*"))
            {
                explanation.add(i,"Block comment begin");
                blockComment = true;
            }
            else if(code.get(i).startsWith("const int"))
            {
                explanation.add(i,"Constant Integer variable: " + code.get(i).substring(code.get(i).indexOf(" ",code.get(i).indexOf(" ")+4),code.get(i).indexOf(" ",code.get(i).indexOf(" ")+5)) + " initialized");
                variables.add(code.get(i).substring(code.get(i).indexOf(" ",code.get(i).indexOf(" ")+4),code.get(i).indexOf(" ",code.get(i).indexOf(" ")+5)));
            }
            else if(code.get(i).startsWith("int"))
            {
                explanation.add(i,"Integer variable: " + code.get(i).substring(code.get(i).indexOf(" ")+1,code.get(i).indexOf(" ",code.get(i).indexOf(" ")+1)) + " initialized");
                variables.add(code.get(i).substring(code.get(i).indexOf(" ")+1,code.get(i).indexOf(" ",code.get(i).indexOf(" ")+1)));
            }
            else if(code.get(i).startsWith("const bool"))
            {
                explanation.add(i,"Constant Boolean variable: " + code.get(i).substring(code.get(i).indexOf(" ",code.get(i).indexOf(" ")+4),code.get(i).indexOf(" ",code.get(i).indexOf(" ")+5)) + " initialized");
                variables.add(code.get(i).substring(code.get(i).indexOf(" ",code.get(i).indexOf(" ")+4),code.get(i).indexOf(" ",code.get(i).indexOf(" ")+5)));
            }
            else if(code.get(i).startsWith("bool"))
            {
                explanation.add(i,"Boolean variable: " + code.get(i).substring(code.get(i).indexOf(" ")+1,code.get(i).indexOf(" ",code.get(i).indexOf(" ")+1)) + " initialized");
                variables.add(code.get(i).substring(code.get(i).indexOf(" ")+1,code.get(i).indexOf(" ",code.get(i).indexOf(" ")+1)));
            }
            else if(code.get(i).startsWith("const String"))
            {
                explanation.add(i,"Constant String variable: " + code.get(i).substring(code.get(i).indexOf(" ",code.get(i).indexOf(" ")+4),code.get(i).indexOf(" ",code.get(i).indexOf(" ")+5)) + " initialized");
                variables.add(code.get(i).substring(code.get(i).indexOf(" ",code.get(i).indexOf(" ")+4),code.get(i).indexOf(" ",code.get(i).indexOf(" ")+5)));
            }
            else if(code.get(i).startsWith("String"))
            {
                explanation.add(i,"Boolean variable: " + code.get(i).substring(code.get(i).indexOf(" ")+1,code.get(i).indexOf(" ",code.get(i).indexOf(" ")+1)) + " initialized");
                variables.add(code.get(i).substring(code.get(i).indexOf(" ")+1,code.get(i).indexOf(" ",code.get(i).indexOf(" ")+1)));
            }
            else if(variable(code.get(i)))
            {
                explanation.add(i,"Variable: " + code.get(i).substring(0, code.get(i).indexOf(" ")) + " updated");
            }
            else if(code.get(i).startsWith("pinMode"))
            {
                explanation.add(i,"Pin: " + code.get(i).substring(code.get(i).indexOf("(")+1,code.get(i).indexOf(",")) + " has been set to: " + code.get(i).substring(code.get(i).indexOf(",")+1,code.get(i).indexOf(")")));
            }
            else if(code.get(i).startsWith("delay"))
            {
                explanation.add(i, "Processor waits for: " + code.get(i).substring(code.get(i).indexOf("(")+1,code.get(i).indexOf(")")) + " milliseconds");
            }
            else if(code.get(i).startsWith("analogWrite") || code.get(i).startsWith("digitalWrite"))
            {
                explanation.add(i, "Pin: " + code.get(i).substring(code.get(i).indexOf("(")+1,code.get(i).indexOf(",")) + " set to: " + code.get(i).substring(code.get(i).indexOf(",")+1,code.get(i).indexOf(")")));
            }
            else if(code.get(i).startsWith("analogWrite"))
            {
                explanation.add(i, "Analog value read from pin: " + code.get(i).substring(code.get(i).indexOf("(")+1, code.get(i).indexOf(")")));
            }
            else if(code.get(i).startsWith("digitalWrite"))
            {
                explanation.add(i, "Digital value read from pin: " + code.get(i).substring(code.get(i).indexOf("(")+1, code.get(i).indexOf(")")));
            }
            else if(code.get(i).startsWith("{"))
            {
                explanation.add(i, "Something starts");
            }
            else if(code.get(i).startsWith("}"))
            {
                explanation.add(i, "Something ends");
            }
            else if(code.get(i).isEmpty())
            {
                explanation.add(i," ");
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
