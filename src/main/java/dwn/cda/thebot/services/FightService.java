package dwn.cda.thebot.services;

import lombok.NonNull;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FightService {

    private static SecureRandom random = new SecureRandom();

    private static Map<Member,Integer> membersExp;
    public static void initExpTable(List<Member> members) {
        membersExp = members
                .stream()
                .collect(Collectors.toMap(member -> member, member -> 0));
    }

    public static String requestFight(@NonNull User asker,@NonNull User target) {
        return "%s challenges %s in an epic fight !%nWho'll come out with glory ?%nAfter some marvelous strikes %s dominate the opponent and increase their fame ! HURRAY !"
                .formatted(
                        asker.getEffectiveName(),
                        target.getEffectiveName(),
                        getWinner(asker, target).getEffectiveName()
                );
    }

    private static User getWinner(@NotNull User asker, @NotNull User target) {
        return random.nextBoolean() ? asker : target;
    }
}
