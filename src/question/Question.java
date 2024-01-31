package question;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    public Question(String question, ArrayList<String> options, String answer, PreparedStatement preparedStatement) throws SQLException {
        this(question, options, answer);

        preparedStatement.setString(1,question);

        for (int i = 0; i < options.size(); i++) {
            preparedStatement.setString(i + 2, options.get(i));
        }
        preparedStatement.setString(6, answer);

        int result = preparedStatement.executeUpdate();
        if (result == 1) {
            System.out.println("1 question(s) added.");
        } else {
            System.out.println("There was an error adding the question.");
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
