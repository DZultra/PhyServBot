package net.dzultra;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.IntegrationType;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.*;
import java.util.EnumSet;

public class PhyServBotMain {
    public static Gson gson = new Gson();
    private static final InputStream stream;
    public static Reader reader;
    public static JsonObject json;

    static {
        InputStream inputStream = PhyServBotMain.class.getClassLoader().getResourceAsStream("bot_info.json");
        if (inputStream == null) {
            try {
                File jarDir = new File(PhyServBotMain.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
                File botFile = new File(jarDir, "bot_info.json");
                if (botFile.exists()) inputStream = new FileInputStream(botFile);
            } catch (Exception ignored) { }
            if (inputStream == null) {
                try {
                    File botFile = new File(System.getProperty("user.dir"), "bot_info.json");
                    if (botFile.exists()) inputStream = new FileInputStream(botFile);
                } catch (Exception ignored) { }
            }
        }
        stream = inputStream;
        if (stream == null) {
            throw new IllegalStateException("`bot_info.json` not found");
        }
        reader = new InputStreamReader(stream);
        json = gson.fromJson(reader, JsonObject.class);
    }

    public static void main(String[] args) {
        EnumSet<GatewayIntent> intents = EnumSet.noneOf(GatewayIntent.class);
        JDA bot = JDABuilder.createLight(json.get("token").getAsString(), intents)
                .addEventListeners(new SlashCommandListener())
                .addEventListeners(new ButtonInteractionListener())
                .build();

        bot.updateCommands().addCommands(
                Commands.slash("controls", "Get initial message")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                        .setContexts(InteractionContextType.ALL)
                        .setIntegrationTypes(IntegrationType.ALL),
                Commands.slash("status", "Get server status")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                        .setContexts(InteractionContextType.ALL)
                        .setIntegrationTypes(IntegrationType.ALL)
        ).queue();
    }
}
