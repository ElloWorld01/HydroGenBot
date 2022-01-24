package fr.hydrogen.bot.other;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class EventsListener implements EventListener {
    public void onEvent(@NotNull GenericEvent event) {
        System.out.println(event.getClass().getSimpleName());
    }
}
