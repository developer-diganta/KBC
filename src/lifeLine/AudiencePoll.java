package lifeLine;

import question.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AudiencePoll implements LifeLine{
    private HashMap<String,Integer> audiencePoll;

    @Override
    public HashMap<String,Integer> useLifeLine(Question question) {
        String answer = question.getAnswer();
        var options = question.getOptions();
        audiencePoll = new HashMap<>();
        for(int i=0;i<options.size();i++){
            if(Objects.equals(options.get(i), answer)){
                audiencePoll.put(answer,85);
            }else{
                audiencePoll.put(options.get(i),5);
            }
        }
        return audiencePoll;
    }
}
