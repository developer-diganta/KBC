package lifeLine;

import question.Question;

public interface LifeLine {
    <T> T useLifeLine(Question question);
}
