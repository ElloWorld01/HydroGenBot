package fr.hydrogen.bot.database;

import fr.hydrogen.bot.Bot;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    /*---------- Getters ----------*/

    public static List<String> getUserIDList() throws SQLException {

        Connection connection = DriverManager.getConnection(Bot.getDatabaseUrl(), Bot.getUsername(), Bot.getPassword());
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM hydrogenbot.users;");

        List<String> userIdList = new ArrayList<>();

        while (resultSet.next()) {
            userIdList.add(resultSet.getString("userId"));
        }
        return userIdList;
    }

    public static List<String> getHydrogenIDList() throws SQLException {

        Connection connection = DriverManager.getConnection(Bot.getDatabaseUrl(), Bot.getUsername(), Bot.getPassword());
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM hydrogenbot.hydrogenstats;");

        List<String> userIdList = new ArrayList<>();

        while (resultSet.next()) {
            userIdList.add(resultSet.getString("id"));
        }
        return userIdList;
    }
    /*----- Pour 1 seul Hydro-Gen -----*/
    public static int getFavoriteCount(String hydrogenId) throws SQLException {
        Connection connection = DriverManager.getConnection(Bot.getDatabaseUrl(), Bot.getUsername(), Bot.getPassword());
        Statement statement = connection.createStatement();

        ResultSet userFavoriteSearch;
        int favoriteCount = 0;
        for (int i = 0; i < DatabaseManager.getHydrogenIDList().size(); i++) {
            userFavoriteSearch = statement.executeQuery("SELECT favoriteCount FROM hydrogenbot.hydrogenstats WHERE (`id` = '" + hydrogenId + "');");
            while (userFavoriteSearch.next()) {
                favoriteCount = userFavoriteSearch.getInt("favoriteCount");
            }
        }
        return favoriteCount;
    }

    public static int getRotationSpeed(String hydrogenId) throws SQLException {
        Connection connection = DriverManager.getConnection(Bot.getDatabaseUrl(), Bot.getUsername(), Bot.getPassword());
        Statement statement = connection.createStatement();

        ResultSet rotationSpeedSearch;
        int rotationSpeed = 0;
        for (int i = 0; i < DatabaseManager.getHydrogenIDList().size(); i++) {
            rotationSpeedSearch = statement.executeQuery("SELECT rotationSpeed FROM hydrogenbot.hydrogenstats WHERE (`id` = '" + hydrogenId + "');");
            while (rotationSpeedSearch.next()) {
                rotationSpeed = rotationSpeedSearch.getInt("rotationSpeed");
            }
        }
        return rotationSpeed;
    }
    public static int getBestSpeed(String hydrogenId) throws SQLException {
        Connection connection = DriverManager.getConnection(Bot.getDatabaseUrl(), Bot.getUsername(), Bot.getPassword());
        Statement statement = connection.createStatement();

        ResultSet rotationSpeedSearch;
        int rotationSpeed = 0;
        for (int i = 0; i < DatabaseManager.getHydrogenIDList().size(); i++) {
            rotationSpeedSearch = statement.executeQuery("SELECT bestSpeed FROM hydrogenbot.hydrogenstats WHERE (`id` = '" + hydrogenId + "');");
            while (rotationSpeedSearch.next()) {
                rotationSpeed = rotationSpeedSearch.getInt("bestSpeed");
            }
        }
        return rotationSpeed;
    }
    /*----- Pour 1 seul utilisateur -----*/

    public static String getUserFavorite(User user) throws SQLException {

        Connection connection = DriverManager.getConnection(Bot.getDatabaseUrl(), Bot.getUsername(), Bot.getPassword());
        Statement statement = connection.createStatement();

        ResultSet userFavoriteSearch;
        String favorite = null;
        for (int i = 0; i < DatabaseManager.getUserIDList().size(); i++) {
            if (DatabaseManager.getUserIDList().get(i).equalsIgnoreCase(user.getId())) {
                userFavoriteSearch = statement.executeQuery("SELECT favorite FROM hydrogenbot.users WHERE (`userId` = '" + user.getId() + "');");
                while (userFavoriteSearch.next()) {
                    favorite = userFavoriteSearch.getString("favorite");
                }
            }
        }
        return favorite;
    }
    /*----- Les meilleures valeurs pour chaque chose -----*/
    public static String getTopFavs(int baseId) throws SQLException {
        Connection connection = DriverManager.getConnection(Bot.getDatabaseUrl(), Bot.getUsername(), Bot.getPassword());
        Statement statement = connection.createStatement();

        String bestHydrogen = null;

        ResultSet bestHydrogenSearch = statement.executeQuery(
                "SELECT id FROM hydrogenstats WHERE favoriteCount = (SELECT DISTINCT(favoriteCount) FROM hydrogenstats ORDER BY favoriteCount DESC LIMIT " + (baseId - 1) + ",1)");
        while (bestHydrogenSearch.next()) {
            bestHydrogen = bestHydrogenSearch.getString("id");
        }
        return bestHydrogen;
    }

    public static String getTopSpeed(int baseId) throws SQLException {
        Connection connection = DriverManager.getConnection(Bot.getDatabaseUrl(), Bot.getUsername(), Bot.getPassword());
        Statement statement = connection.createStatement();

        String bestHydrogen = null;

        ResultSet bestHydrogenSearch = statement.executeQuery(
                "SELECT id FROM hydrogenstats WHERE bestSpeed = (SELECT DISTINCT(bestSpeed) FROM hydrogenstats ORDER BY bestSpeed DESC LIMIT " + (baseId - 1) + ",1)");
        while (bestHydrogenSearch.next()) {
            bestHydrogen = bestHydrogenSearch.getString("id");
        }
        return bestHydrogen;
    }

    /*---------- Setters ----------*/

    public static void setUserFavorite(User user, String hydrogenId, MessageChannel channel) throws SQLException {
        Connection connection = DriverManager.getConnection(Bot.getDatabaseUrl(), Bot.getUsername(), Bot.getPassword());
        Statement statement = connection.createStatement();


        /* FAVORITECOUNT DU NOUVEAU FAV */
        int favoriteCount = 0;

        ResultSet favoriteCountResult = statement.executeQuery("SELECT favoriteCount FROM hydrogenbot.hydrogenstats WHERE (`id` = " + hydrogenId + ");");
        while (favoriteCountResult.next()) { favoriteCount = favoriteCountResult.getInt("favoriteCount"); }

        /*----- ANCIEN Favorite du User -----*/
        String favoriteHydrogen = null;

        ResultSet favoriteResult = statement.executeQuery("SELECT favorite FROM hydrogenbot.users WHERE (`userId` = '" + user.getId() + "');");
        while (favoriteResult.next()) { favoriteHydrogen = favoriteResult.getString("favorite"); }

        /* FAVORITECOUNT DE LANCIEN FAV */
        int favoriteBefore = 0;

        ResultSet userFavoriteResult = statement.executeQuery("SELECT favoriteCount FROM hydrogenbot.hydrogenstats WHERE (`id` = '" + favoriteHydrogen + "');");
        while (userFavoriteResult.next()) { favoriteBefore = userFavoriteResult.getInt("favoriteCount"); }

        if (favoriteHydrogen != null) {
            for (int i = 0; i < DatabaseManager.getHydrogenIDList().size(); i++) {
                if (DatabaseManager.getHydrogenIDList().get(i).equalsIgnoreCase(hydrogenId)) {
                    if (!favoriteHydrogen.equalsIgnoreCase(hydrogenId)) {

                        if (favoriteBefore > 0) {
                            statement.execute("UPDATE `hydrogenbot`.`hydrogenstats` SET `favoriteCount` = '" + (favoriteBefore - 1) + "' WHERE (`id` = '" + favoriteHydrogen + "');");
                        } else {
                            statement.execute("UPDATE `hydrogenbot`.`hydrogenstats` SET `favoriteCount` = '" + 0 + "' WHERE (`id` = '" + favoriteHydrogen + "');");
                        }
                        statement.execute("UPDATE `hydrogenbot`.`users` SET `favorite` = '" + hydrogenId + "' WHERE (`userId` = '" + user.getId() + "');");
                        statement.execute("UPDATE `hydrogenbot`.`hydrogenstats` SET `favoriteCount` = '" + (favoriteCount + 1) + "' WHERE (`id` = '" + hydrogenId + "');");
                        channel.sendMessage("> Hydro-Gen favori modifié avec succès (" + hydrogenId + ") !").queue();
                    } else {
                        channel.sendMessage("> Vous avez déjà ce bot en favori !").queue();
                    }
                    return;
                }
            }
        } else {
            channel.sendMessage("> Erreur : h!favorite [numéro machine]").queue();
        }
    }

    public static void clearUserFavorite(User user, MessageChannel channel) throws SQLException {

        Connection connection = DriverManager.getConnection(Bot.getDatabaseUrl(), Bot.getUsername(), Bot.getPassword());
        Statement statement = connection.createStatement();

        /* FAVORITECOUNT DE LANCIEN FAV */
        int favoriteBefore = 0;
        String favoriteHydrogen = null;

        ResultSet favoriteResult = statement.executeQuery("SELECT favorite FROM hydrogenbot.users WHERE (`userId` = '" + user.getId() + "');");
        while (favoriteResult.next()) { favoriteHydrogen = favoriteResult.getString("favorite"); }

        ResultSet userFavoriteResult = statement.executeQuery("SELECT favoriteCount FROM hydrogenbot.hydrogenstats WHERE (`id` = '" + favoriteHydrogen + "');");
        while (userFavoriteResult.next()) { favoriteBefore = userFavoriteResult.getInt("favoriteCount"); }

            statement.execute("UPDATE `hydrogenbot`.`users` SET `favorite` = '" + "Aucun" + "' WHERE (`userId` = '" + user.getId() + "');");
            statement.execute("UPDATE `hydrogenbot`.`hydrogenstats` SET `favoriteCount` = '" + (favoriteBefore - 1) + "' WHERE (`id` = '" + favoriteHydrogen + "');");

            channel.sendMessage("> Hydro-Gen favori effacé avec succès !").queue();
    }

    public static void setUserID(User user) throws SQLException {
        List<String> userID = new ArrayList<>();

        boolean ifHaveID = false;

        Connection connection = DriverManager.getConnection(Bot.getDatabaseUrl(), Bot.getUsername(), Bot.getPassword());
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM hydrogenbot.users;");

        while (resultSet.next()) {
            userID.add(resultSet.getString("userId"));
        }
        for (String s : userID) {
            if (s.equals(user.getId())) {
                ifHaveID = true;
            }
        }
        if (!ifHaveID) {
            statement.execute("insert into users(userId) values ('" + user.getId() + "')");
        }
    }
}
