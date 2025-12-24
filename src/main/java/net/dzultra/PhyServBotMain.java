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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.EnumSet;
import java.util.Objects;

public class PhyServBotMain {
    public static Gson gson = new Gson();
    private static final InputStream stream = PhyServBotMain.class.getClassLoader().getResourceAsStream("bot_info.json");
    public static Reader reader = new InputStreamReader(Objects.requireNonNull(stream));
    public static JsonObject json = gson.fromJson(reader, JsonObject.class);

    public static void main(String[] args) {
        EnumSet<GatewayIntent> intents = EnumSet.noneOf(GatewayIntent.class);
        JDA api = JDABuilder.createLight(json.get("token").getAsString(), intents)
                .addEventListeners(new SlashCommandListener())
                .build();

        api.updateCommands().addCommands(
                Commands.slash("controls", "Get control message")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                        .setContexts(InteractionContextType.ALL)
                        .setIntegrationTypes(IntegrationType.ALL)
        ).queue();
    }
}
