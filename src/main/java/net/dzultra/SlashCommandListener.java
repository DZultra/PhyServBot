package net.dzultra;

import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class SlashCommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();

        if (event.getName().equals("controls")) {
            if (event.getUser().getId().equals(PhyServBotMain.json.get("admin_id").getAsString())) {
                event.reply("You are no authorized to use this command! Please contact DZultra").setEphemeral(true).queue();
            }
            if(ServerRegistry.isServerRunning(event.getChannel().getId())) {
                event.reply("").addComponents(ActionRow.of(
                        Button.danger(userId + ":stop", "Stop").asEnabled()
                )).queue();
            } else {
                event.reply("").addComponents(ActionRow.of(
                        Button.success(userId + ":start", "Start").asEnabled()
                )).queue();
            }
        } else if (event.getName().equals("status")) {
            event.deferReply().setEphemeral(true).queue();
            boolean status = APICaller.callServerStatus(Duration.of(2, ChronoUnit.SECONDS));
            if (status) {
                event.getHook().sendMessage("The server is currently **ONLINE** :green_circle:").setEphemeral(true).queue();
            } else {
                event.getHook().sendMessage("The server is currently **OFFLINE** :red_circle:").setEphemeral(true).queue();
            }
        }
    }
}