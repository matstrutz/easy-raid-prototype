import entity.Member;
import entity.Raid;
import org.apache.commons.lang3.StringUtils;
import service.Read;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class main {

    //TODO SEPARAR MEMBROS POR NIVEL DE CONHECIMENTO
    //TODO LIMITAR QUANTIDADE MINIMA DE SUPPORT POR PARTY
    //TODO ADICIONAR COLUNA VAZIA QUANDO NÃO HOUVER MEMBRO
    private static final String FILEPATH = "src/main/resources/Planilha_sem_titulo.xlsx";

    private static final String RAID_NAME = "VYKAS";
    private static final Integer RAID_SYZE = 8;
    private static final Integer PARTY_SIZE = 4;
    private static final Integer SUPPORT_PER_PARTY = 1;
    private static final Integer MAX_SAME_CLASS_PER_PARTY = 2;
    private static final List<String> SUPPORT_CLASS = Arrays.asList("Bard","Paladin");

    public static void main(String[] args){
        List<Raid> raids = new ArrayList<>();
        List<Member> members = Read.mapFile(FILEPATH);
        removeNonParticipants(members);
        Collections.shuffle(members);

        Raid firstRaid = new Raid();

        firstRaid.setName(RAID_NAME);
        firstRaid.setSquad(1);
        firstRaid.setDay(members.get(0).getDay().get(0));
        firstRaid.setHour(members.get(0).getHour().get(0));
        firstRaid.getMember().add(members.get(0));
        firstRaid.setDifficulty(members.get(0).getDifficulty());
        members.remove(0);

        raids.add(firstRaid);
        for (Member member : members) {
            AtomicReference<Boolean> needsNewRaid = new AtomicReference<>(Boolean.FALSE);
            for (int i = 0; i < raids.size(); i++) {
                Boolean isSup = SUPPORT_CLASS.contains(member.getClassName());
                if ((raids.get(i).validationIfMemberEnter(member)) &&
                        !(raids.get(i).hasExactSupportNumber(SUPPORT_CLASS, calculateQtdSupsPerRaid()) || raids.get(i).maxMemberSize(RAID_SYZE))) {
                    raids.get(i).getMember().add(member);
                    needsNewRaid.set(Boolean.FALSE);
                    break;
                } else {
                    needsNewRaid.set(Boolean.TRUE);
                }
            }

            if (needsNewRaid.get()) {
                raids.add(createNewRaid(member, raids.size() + 1));
            }
        }

        printRaids(raids);
    }

    private static Integer calculateQtdSupsPerRaid(){
        return RAID_SYZE / PARTY_SIZE;
    }

    private static Raid createNewRaid(Member member, Integer squadCount){
        Raid raid = new Raid();

        raid.setDifficulty(member.getDifficulty());
        raid.setDay(member.getDay().get(0));
        raid.setHour(member.getHour().get(0));
        raid.setSquad(squadCount);
        raid.getMember().add(member);
        raid.setName(RAID_NAME);

        return raid;
    }

    private static void removeNonParticipants(List<Member> members){
        AtomicReference<Member> removeMember = new AtomicReference<>(new Member());
        members.forEach(item -> {
            if(Objects.isNull(item.getDifficulty())
                    || Objects.isNull(item.getClassName())
                    || Objects.isNull(item.getIlvl())
                    || Objects.isNull(item.getKnowledge())){
                removeMember.set(item);
            }
        });

        members.remove(removeMember.get());
    }

    private static void printRaids(List<Raid> raids){
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n");

        raids.forEach(raid -> {
            sb.append(raid.getName()).append(" ")
                    .append(raid.getDifficulty()).append(" ")
                    .append(raid.getHour()).append(" ")
                    .append(raid.getDay()).append(" ")
                    .append("ESQUADRÃO").append(" ").append(raid.getSquad());
            sb.append("\n\n");
            sb.append(basicTitlePad("Nome"))
                    .append(basicTitlePad("Classe")).append(basicTitlePad("Raid Leader"));
            sb.append("\n\n");
            raid.getMember().forEach(member -> {
                sb.append(basicPad(member.getName())).append(" | ").append(basicPad(member.getClassName())).append(" | ").append(" ");
                sb.append("\n");
            });

            sb.append("\n\n");
        });

        System.out.println(sb);
    }

    private static String basicPad(String value){
        value = StringUtils.rightPad(value, 20, " ");
        value = StringUtils.leftPad(value, 20, " ");
        return value;
    }

    private static String basicTitlePad(String value){
        value = StringUtils.rightPad(value, 20, " ");
        value = StringUtils.leftPad(value, 25, " ");
        return value;
    }
}
