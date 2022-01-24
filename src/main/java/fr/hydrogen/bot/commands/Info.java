package fr.hydrogen.bot.commands;

import fr.hydrogen.bot.Bot;
import fr.hydrogen.bot.database.DatabaseManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Info extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" ");
        if (!event.getAuthor().isBot()) {
            if (message[0].equalsIgnoreCase(Bot.getPrefix() + "info")) {
                if (message.length == 2) {
                    try {
                        int idListSize = DatabaseManager.getHydrogenIDList().size();
                        for (int i = 0; i < idListSize; i++) {
                            String hydrogenId = DatabaseManager.getHydrogenIDList().get(i);
                            if (message[1].equalsIgnoreCase(hydrogenId)) {
                                infoEmbed(message, event);
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    event.getChannel().sendMessage("> Erreur : h!info [numéro machine]").queue();
                }
            }
        }
    }

    public void infoEmbed(String[] message, MessageReceivedEvent event) throws SQLException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Infomations de l'Hydro-Gen [" + message[1] + "]")
                .setColor(new Color(87, 179, 255))
                .addField("Vitesse de rotation", DatabaseManager.getRotationSpeed(message[1]) + " tr/min", false)
                .addField("Nombre de favoris", DatabaseManager.getFavoriteCount(message[1]) + " ⭐", false)
                .setFooter("Commande éxécutée le " + dateFormat.format(date), "https://cdn.discordapp.com/attachments/750755018138714132/752090964826521650/hydro-gen_whitebg.png");

        event.getChannel().sendMessage(embed.build()).queue();
    }
}
