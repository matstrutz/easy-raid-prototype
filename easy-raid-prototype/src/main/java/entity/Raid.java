package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Raid {

    private String name;

    private List<Member> member = new ArrayList<>();

    private Integer squad;

    private String hour;

    private String day;

    private String difficulty;

    public Boolean hasMaxSupport(List<String> supports, Integer maxValue){
        Integer count = 0;
        for (Member mb: member) {
            if (count >= maxValue){
                break;
            }
            if(supports.contains(mb.getClassName())){
                count++;
            }
        }

        if(count < maxValue){
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    public Boolean maxMemberSize(Integer maxValue){
        return maxValue > member.size() ? Boolean.FALSE : Boolean.TRUE;
    }

    public Boolean validationIfMemberEnter(Member member){
        if(member.getHour().contains(hour) && member.getDay().contains(day) && member.getDifficulty().equals(difficulty)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
