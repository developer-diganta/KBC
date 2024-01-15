package lifeLine;

import question.Question;

import java.util.ArrayList;
import java.util.Objects;

//FiftyFifty lifeline Class
public class FiftyFiftyLifeLine implements LifeLine{
    private static final String name="50:50";
    private ArrayList<String> remainingOptions;

    public FiftyFiftyLifeLine() {

    }

    @Override
    public ArrayList<String> useLifeLine(Question question) {
        String correctAnswer = question.getAnswer();
        double randomValue = Math.random() * 3;
        int randomOption = (int) randomValue;
        ArrayList<String> options = new ArrayList<>();
        options.add(correctAnswer);
        if(Objects.equals(correctAnswer, question.getOptions().get(randomOption))){
            if(randomOption==0) randomOption++;
            else if(randomOption==2) randomOption--;
        }
        options.add(question.getOptions().get(randomOption));
        return options;
    }
}
