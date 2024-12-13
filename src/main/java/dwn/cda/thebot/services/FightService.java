package dwn.cda.thebot.services;

import lombok.NonNull;
import net.dv8tion.jda.api.entities.User;

public class FightService {
    public static String requestFight(@NonNull User asker,@NonNull User target) {
        return "%s challenges %s in an epic fight !%nWho'll come out with glory ?"
                .formatted(asker.getEffectiveName(),target.getEffectiveName());
    }
}
