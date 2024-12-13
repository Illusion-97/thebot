package dwn.cda.thebot.services;

import net.dv8tion.jda.api.entities.Member;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpService {
    private static Map<Member,Integer> membersExp;
    public static void initExpTable(List<Member> members) {
        membersExp = members
                .stream()
                .collect(Collectors.toMap(member -> member, member -> 0));
    }
}
