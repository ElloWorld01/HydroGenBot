package fr.hydrogen.bot;

import fr.hydrogen.bot.commands.*;
import fr.hydrogen.bot.database.DatabaseManager;
import fr.hydrogen.bot.other.EventsListener;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import javax.security.auth.login.LoginException;
import java.util.Scanner;

public class Bot implements Runnable {

    private static String databaseUrl = "jdbc:mysql://localhost:3306/hydrogenbot?serverTimezome=UTC";
    private static String username = "root";
    private static String password = "";

    private static String prefix = "h!";
    private static JDA jda;
    private final Scanner scanner = new Scanner(System.in);
    private DatabaseManager database = new DatabaseManager();
    private boolean running;

    public Bot() throws LoginException {

        String token = "ODI3NjI3OTM2NjU1MzQzNjc3.YGdylA.LQbl9DAXYBRMCOvis2ArLvPVP7w";

        jda = new JDABuilder(AccountType.BOT).setToken(token).build();
        /*----- Commands -----*/
        jda.addEventListener(new Info());
        jda.addEventListener(new Profil());
        jda.addEventListener(new Favorite());
        jda.addEventListener(new Help());
        jda.addEventListener(new Top());

        /*---- Other -----*/
        jda.addEventListener(new EventsListener());

        jda.getPresence().setActivity(Activity.playing("1 tr/min !"));

    }

    public static void main(String[] args) {
        try {
            Bot bot = new Bot();
            new Thread(bot, "bot").start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getPrefix() {
        return prefix;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static JDA getJda() {
        return jda;
    }

    public static String getDatabaseUrl() {
        return databaseUrl;
    }

    public DatabaseManager getDatabase() {
        return database;
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            if (scanner.hasNextLine()) {
                int stop = scanner.nextInt();
                if (stop == 0) {
                    scanner.close();
                    System.out.println("Arrêt du bot !");
                    jda.shutdown();
                    System.exit(0);
                }
            }
        }
    }

}