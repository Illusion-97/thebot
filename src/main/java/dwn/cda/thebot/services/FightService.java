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

    private static Map<User,Integer> membersExp;
    public static void initExpTable(List<Member> members) {
        membersExp = members
                .stream()
                .collect(Collectors.toMap(Member::getUser, member -> 0));
    }

    public static User requestFight(@NonNull User asker,@NonNull User target) {
        return (random.nextBoolean() ? asker : target);
    }

    public static void gainExp(User user, int amount) {
        membersExp.computeIfPresent(user, (u, exp) -> exp + amount);
    }

    public static String getStat(@NonNull User asker) {
        return "You've gained %d exp points until now.".formatted(membersExp.getOrDefault(asker,-1));
    }
}
