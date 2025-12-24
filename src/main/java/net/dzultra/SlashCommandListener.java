package net.dzultra;

import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();

        if (event.getName().equals("controls")) {
            event.reply("Control the Server").addComponents(ActionRow.of(
                            Button.success(userId + ":start", "Start").asEnabled(),
                            Button.danger(userId + ":stop", "Stop").asEnabled()
            )).queue();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String[] id = event.getComponentId().split(":");
        String authorId = id[0];
        String type = id[1];

        if (!authorId.equals(event.getUser().getId())) {
            event.reply("You are not authorized to use these controls.").setEphemeral(true).queue();
            return;
        }

        // Acknowledge with an ephemeral reply
        event.deferReply(true).queue();

        switch (type) {
            case "start" -> {
                System.out.println("Starting the server...");
                event.getMessage().delete().queue();
                event.getHook().sendMessage("Server is starting...").queue();
            }
            case "stop" -> {
                System.out.println("Stopping the server...");
                event.getMessage().delete().queue();
                event.getHook().sendMessage("Server is stopping...").queue();
            }
        }
    }

    private void sendMessage(MessageChannel channel, String content, ActionRow actionRow) {
        channel.sendMessage(content)
                .addComponents(actionRow)
                .queue();
    }
}
