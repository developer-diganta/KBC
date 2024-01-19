package question;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

//question class with its appropriate fields and methods
public final class Question {
    private final String question;
    private final ArrayList<String> options;
    private final String answer;

    public Question(String question, ArrayList<String> options, String answer) {
        this.question = question;
        this.options = options;
        this.answer = answer;
    }

    public Question(String question, ArrayList<String> options, String answer, Path path){
        //constructor chaining implemented to initialise the required fields
        this(question, options, answer);
        //writes question to file
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile(),true))){
            writer.append("Q:").append(this.question).append("\n");
            writer.append("Option 1:").append(this.options.get(0)).append("\n");
            writer.append("Option 2:").append(this.options.get(1)).append("\n");
            writer.append("Option 3:").append(this.options.get(2)).append("\n");
            writer.append("Option 4:").append(this.options.get(3)).append("\n");
            writer.append("CA: ").append(this.answer).append("\n");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public String getAnswer() {
        return answer;
    }
}
