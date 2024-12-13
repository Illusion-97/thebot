package dwn.cda.thebot.bot;

import dwn.cda.thebot.services.FightService;
import net.dv8tion.jda.api.entities.Guild;
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
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        guild = event.getGuild();
        FightService.initExpTable(guild.getMembers());
        guild.updateCommands().addCommands(
                Commands.slash("iwantproblemsalways", "check your own fame."),
                Commands.slash("idontwantpeace", "Provoking someone in an epic duel.")
                        .addOption(OptionType.USER,"target","Your mighty opponent.", true)
        ).queue();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "iwantproblemsalways":
                event.reply("In Progress").queue();
                break;
            case "idontwantpeace":
                event.reply(FightService.requestFight(event.getUser(), Objects.requireNonNull(event.getOption("target")).getAsUser())).queue();
                break;
            default:
                event.reply("I'm a teapot").setEphemeral(true).queue();
        }
    }
}
