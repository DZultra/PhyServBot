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
        switch (event.getName()) {
            case "controls" -> controlsCommand(event);
            case "status"   -> statusCommand(event);
            case "server"   -> serverCommand(event);
            default         -> unknownCommand(event);
        }
    }

    private void unknownCommand(SlashCommandInteractionEvent event) {
        event.reply("Unknown command! Something clearly went wrong").setEphemeral(true).queue();
    }

    private void controlsCommand(SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();
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
    }

    private void statusCommand(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();
        boolean status = APICaller.callServerStatus(Duration.of(2, ChronoUnit.SECONDS));
        if (status) {
            event.getHook().sendMessage("The server is currently **ONLINE** :green_circle:").setEphemeral(true).queue();
        } else {
            event.getHook().sendMessage("The server is currently **OFFLINE** :red_circle:").setEphemeral(true).queue();
        }
    }

    private void serverCommand(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();
        boolean status = APICaller.callServerStatus(Duration.of(2, ChronoUnit.SECONDS));
        if (status) {
            try {
                APICaller.stopPhysicalServer();
                event.getHook().sendMessage("Server is online. Stop command sent.").queue();
            } catch (Exception e) {
                event.getHook().sendMessage("Error stopping server: " + e.getMessage()).queue();
            }
        } else {
            try {
                String mac = PhyServBotMain.json.get("mac").getAsString();
                ProcessBuilder pb = new ProcessBuilder("wakeonlan", "-i", "192.168.178.255", mac);
                pb.redirectErrorStream(true);
                Process p = pb.start();
                int exit = p.waitFor();
                if (exit == 0) {
                    event.getHook().sendMessage("WOL command sent to Physical Server").queue();
                } else {
                    event.getHook().sendMessage("WOL command failed (Exit: " + exit + ")").queue();
                }
            } catch (Exception e) {
                event.getHook().sendMessage("Error sending WOL command: " + e.getMessage()).queue();
            }
        }
    }
}