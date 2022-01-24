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

public class Help extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" ");
        if (!event.getAuthor().isBot()) {
            if (message[0].equalsIgnoreCase(Bot.getPrefix() + "help") && message.length == 1) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                try {
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("Commande help")
                            .setColor(new Color(168, 109, 231))
                            .addField(":scroll: `h!help`\n",
                                    "Affiche les commandes et leurs utilisations.\n\n", false)
                            .addField(":information_source: `h!info [numéro machine]`\n",
                                    "Affiche les informations de la machine choisie.\n\n", false)
                            .addField(":bar_chart: `h!profil [Nom/Id/Mention d'utilisateur]`\n",
                                    "Affiche les informations de la personne choisie.\n\n", false)
                            .addField(":heart: `h!fav [numéro machine]`\n",
                                    "Met en favori la machine choisie, lui donne 1 point dans le classement et affiche sur votre profil qu'elle est votre préférée.\n\n", false)
                            .addField(":broken_heart: `h![favorite/fav] clear`\n",
                                    "Retire votre machine préférée.\n\n", false)
                            .addField(":satellite: Les machines actuellement présentes", DatabaseManager.getHydrogenIDList().toString(), false)
                            .setFooter("Commande éxécutée le " + dateFormat.format(date), "https://cdn.discordapp.com/attachments/750755018138714132/752090964826521650/hydro-gen_whitebg.png");

                    event.getChannel().sendMessage(embed.build()).queue();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
