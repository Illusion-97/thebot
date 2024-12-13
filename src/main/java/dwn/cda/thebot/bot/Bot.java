package dwn.cda.thebot.bot;

import dwn.cda.thebot.services.FightService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class Bot extends ListenerAdapter {
    private Guild guild;
    private TextChannel textChannel;
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        guild = event.getGuild();
        FightService.initExpTable(guild.getMembers());
        textChannel = guild.getTextChannelsByName("hors-sujet", true).stream().findFirst().orElseThrow();
        guild.updateCommands().addCommands(
                Commands.slash("iwantproblemsalways", "check your own fame."),
                Commands.slash("idontwantpeace", "Provoking someone in an epic duel.")
                        .addOption(OptionType.USER,"target","Your mighty opponent.", true)
        ).queue();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        User asker = event.getUser();
        switch (event.getName()) {
            case "iwantproblemsalways":
                event.reply(FightService.getStat(asker)).queue();
                break;
            case "idontwantpeace":
                User target = Objects.requireNonNull(event.getOption("target")).getAsUser();
                event.reply("You've successfully provoked " + target.getEffectiveName()).setEphemeral(true)
                        .queue(v -> textChannel.sendMessage(
                                "%s challenges %s in an epic fight !%nWho'll come out with glory ?"
                                        .formatted(asker.getEffectiveName(), target.getName())
                        ).queue(vv -> {
                            User winner = FightService.requestFight(asker, target);
                            textChannel.sendMessage(
                                    "After some marvelous strikes %s dominate the opponent and increase their fame !"
                                            .formatted(winner)
                            ).queue(vvv -> {
                                int amount = 500;
                                winner.openPrivateChannel().queue(privateChannel ->
                                        privateChannel.sendMessage(
                                                "You've gained %d exp points !".formatted(amount)
                                        ).queue());
                                FightService.gainExp(winner, amount);
                            });
                        }));
                break;
            default:
                event.reply("I'm a teapot").setEphemeral(true).queue();
        }
    }
}
