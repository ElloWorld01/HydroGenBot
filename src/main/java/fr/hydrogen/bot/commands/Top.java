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

public class Top extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" ");
        if (!event.getAuthor().isBot()) {
            if (message[0].equalsIgnoreCase(Bot.getPrefix() + "top")) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                EmbedBuilder embed = new EmbedBuilder()
                        .setColor(Color.RED)
                        .setFooter("Commande éxécutée le " + dateFormat.format(date), "https://cdn.discordapp.com/attachments/750755018138714132/752090964826521650/hydro-gen_whitebg.png");

                if (message.length > 3 || message.length < 2) {
                    event.getChannel().sendMessage("Erreur : h!top [fav/speed]").queue();
                    return;
                } else if (message[1].equalsIgnoreCase("fav") || message[1].equalsIgnoreCase("favorite")) {
                    embed.setTitle("Classement | Favori :star:");
                    try {
                        for (int i = 1; i <= DatabaseManager.getHydrogenIDList().size(); i++)
                            embed.addField("#" + i + " - Hydro-Gen n° " + DatabaseManager.getTopFavs(i), ">>> **" + DatabaseManager.getFavoriteCount(DatabaseManager.getTopFavs(i)) + "** :star:", false);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (message[1].equalsIgnoreCase("speed") || message[1].equalsIgnoreCase("bestspeed")) {
                    embed.setTitle("Classement | Vitesse :ocean:");
                    try {
                        for (int i = 1; i <= DatabaseManager.getHydrogenIDList().size(); i++)
                            embed.addField("#" + i + " - Hydro-Gen n° " + DatabaseManager.getTopSpeed(i), ">>> **" + DatabaseManager.getBestSpeed(DatabaseManager.getTopSpeed(i)) + "** tr/min :ocean:", false);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                event.getChannel().sendMessage(embed.build()).queue();
            }
        }
    }
}
