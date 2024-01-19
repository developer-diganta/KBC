package question;

import java.util.ArrayList;

public class QuestionList {
    private ArrayList<Question> questionList;

    public QuestionList(){
        questionList=new ArrayList<>();
    }
    public void addQuestionToQuestionList(Question question) {
        questionList.add(question);
    }

    public ArrayList<Question> getQuestionList() {
        return questionList;
    }


}
