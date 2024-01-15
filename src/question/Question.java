package question;

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
