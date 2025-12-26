package net.dzultra;

import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();

        if (event.getName().equals("controls")) {
            if(ServerRegistry.isServerRunning(event.getChannel().getId())) {
                event.reply("").addComponents(ActionRow.of(
                        Button.danger(userId + ":stop", "Stop").asEnabled()
                )).queue();
            } else {
                event.reply("").addComponents(ActionRow.of(
                        Button.success(userId + ":start", "Start").asEnabled()
                )).queue();
            }
        }
    }
}