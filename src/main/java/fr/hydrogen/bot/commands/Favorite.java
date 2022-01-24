package fr.hydrogen.bot.commands;

import fr.hydrogen.bot.Bot;
import fr.hydrogen.bot.database.DatabaseManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;

public class Favorite extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" ");
        if (!event.getAuthor().isBot()) {
            if (message[0].equalsIgnoreCase(Bot.getPrefix() + "favorite") || message[0].equalsIgnoreCase(Bot.getPrefix() + "fav")) {
                try {
                    DatabaseManager.setUserID(event.getAuthor());

                    if (message[1].equalsIgnoreCase("clear")) {
                        DatabaseManager.clearUserFavorite(event.getAuthor(), event.getChannel());
                    } else {
                        DatabaseManager.setUserFavorite(event.getAuthor(), message[1], event.getChannel());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
