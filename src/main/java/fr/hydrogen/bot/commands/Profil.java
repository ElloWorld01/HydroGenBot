package fr.hydrogen.bot.commands;

import fr.hydrogen.bot.Bot;
import fr.hydrogen.bot.database.DatabaseManager;
import fr.hydrogen.bot.other.IsRightType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Profil extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" ");
        User user = event.getAuthor();

        if (!event.getAuthor().isBot()) {
            if ((message[0].equalsIgnoreCase(Bot.getPrefix() + "profil")) || ((message[0].equalsIgnoreCase(Bot.getPrefix() + "profile")))) {
                if (message.length == 1) {
                    try {
                        infoEmbed(user, event);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (IsRightType.isInt(message[1])) {
                        long userID = Long.parseLong(message[1]);
                        try {
                            user = event.getGuild().getMemberById(userID).getUser();
                            DatabaseManager.setUserID(user);
                            infoEmbed(user, event);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            String userName = message[1];
                            user = event.getGuild().getMembersByName(userName, true).get(0).getUser();
                            DatabaseManager.setUserID(user);
                            infoEmbed(user, event);
                        } catch (Exception e) {
                            event.getChannel().sendMessage("> Nous n'avons pas trouvé cet utilisateur.").queue();
                        }
                    }
                }
            }
        }
    }

    public void infoEmbed(User user, MessageReceivedEvent event) throws SQLException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String userFavorite = DatabaseManager.getUserFavorite(user);
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Infomations de " + user.getName())
                .setColor(new Color(168, 109, 231))
                .addField("Hydro-Gen favori", ""+userFavorite, true)
                .setThumbnail(user.getAvatarUrl())
                .setFooter("Commande éxécutée le " + dateFormat.format(date), "https://cdn.discordapp.com/attachments/750755018138714132/752090964826521650/hydro-gen_whitebg.png");

        event.getChannel().sendMessage(embed.build()).queue();
    }
}
