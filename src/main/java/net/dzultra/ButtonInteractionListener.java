package net.dzultra;

import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ButtonInteractionListener extends ListenerAdapter {
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String componentId = event.getComponentId();

        // Validate button ID
        if (!componentId.contains(":")) {
            event.reply("Wrong Button-ID-Format.").setEphemeral(true).queue();
            return;
        }

        String[] parts = componentId.split(":", 2);
        if (parts.length < 2) {
            event.reply("Wrong Button-ID-Format.").setEphemeral(true).queue();
            return;
        }

        String authorId = parts[0];
        String action = parts[1];
        Channel executedChannel = event.getChannel();

        // Lookup server by channel ID
        Server server = ServerRegistry.getByChannelId(executedChannel.getId());
        if (server == null) {
            handleUnknownServer(event);
            return;
        }

        event.deferReply().queue();

        // Call the server-specific handler
        server.getHandler().handle(event, action, authorId);
    }

    private void handleUnknownServer(ButtonInteractionEvent event) {
        event.reply("Please use the controls in the correct channel.\nCurrent Channel ID does not match any in list.")
                .setEphemeral(true).queue();
    }

    public static void handleNeoForgeServer(ButtonInteractionEvent event, String action, String authorId) {
        boolean start = "start".equals(action);
        System.out.println(start ? "Starting the Neoforge Server..." : "Stopping the NeoForge Server...");

        APICaller.callServer(1, action);

        Server server = ServerRegistry.getByChannelId(event.getChannel().getId());
        if (server != null) server.setRunning(start);

        String newChannelName = start ? "N𝖾𝗈𝖥𝗈𝗋𝗀𝖾🟩" : "N𝖾𝗈𝖥𝗈𝗋𝗀𝖾🟥";
        event.getChannel().asTextChannel().getManager().setName(newChannelName).queue();

        event.getMessage().delete().queue();

        Button button = start
                ? Button.danger(authorId + ":stop", "Stop").asEnabled()
                : Button.success(authorId + ":start", "Start").asEnabled();

        event.getHook().sendMessage("").addComponents(ActionRow.of(button)).queue();
    }

    public interface ServerHandler {
        void handle(ButtonInteractionEvent event, String action, String authorId);
    }
}
