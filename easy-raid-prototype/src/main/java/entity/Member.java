package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    private String name;

    private String className;

    private String ilvl;

    private String difficulty;

    private List<String> day;

    private String knowledge;

    private List<String> hour;
}
