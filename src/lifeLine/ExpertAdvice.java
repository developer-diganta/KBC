package lifeLine;

import question.Question;

public class ExpertAdvice implements LifeLine{

    @Override
    public String useLifeLine(Question question) {
        return question.getAnswer();
    }
}
