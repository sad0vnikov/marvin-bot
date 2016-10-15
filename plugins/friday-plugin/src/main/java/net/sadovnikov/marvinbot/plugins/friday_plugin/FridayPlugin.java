package net.sadovnikov.marvinbot.plugins.friday_plugin;


import net.sadovnikov.marvinbot.core.domain.message.MessageToSend;
import net.sadovnikov.marvinbot.core.plugin.Plugin;
import net.sadovnikov.marvinbot.core.schedule.Task;
import net.sadovnikov.marvinbot.core.service.chat.AbstractChat;
import ro.fortsoft.pf4j.PluginException;
import ro.fortsoft.pf4j.PluginWrapper;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Random;

public class FridayPlugin extends Plugin {


    public FridayPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    private final int RUN_HOUR = 12;
    private final int RUN_MINUTES = 0;
    private static final DayOfWeek FRIDAY = DayOfWeek.FRIDAY;


    @Override
    public void start() throws PluginException {
        super.start();
        addScheduledTask();
    }

    public void addScheduledTask() {
        LocalDateTime currentTime = LocalDateTime.now();
        Task task = new FridayGreetingTask();

        LocalDateTime friday;

        if (currentTime.getDayOfWeek().equals(FRIDAY) && currentTime.getHour() <= RUN_HOUR && currentTime.getMinute() < RUN_MINUTES) {
            friday = currentTime;
        // if it's already friday and the time the bot should have told about that have already passed:
        } else if (currentTime.getDayOfWeek().equals(FRIDAY) && currentTime.getHour() >= RUN_HOUR && currentTime.getMinute() >= RUN_MINUTES) {
            task.run();
            friday = currentTime.plusWeeks(1);
        } else {
            friday = currentTime.with(TemporalAdjusters.next(FRIDAY));
        }

        friday = friday.withHour(RUN_HOUR).withMinute(RUN_MINUTES).withSecond(0);

        int weekInMillis = 7 * 24 * 60 * 60 * 1000;
        marvin.tasksSchedule().addTask(task, friday, weekInMillis);
    }


    public class FridayGreetingTask extends Task {

        protected final String LAST_GREET_KEY = "last_greet_date";

        @Override
        public void run() {

            LocalDate today = LocalDate.now();

            try {
                for (AbstractChat chat : marvin.contact().getGroupChats()) {

                    String lastGreetDateValue = marvin.pluginOptions().chat(chat.chatId()).get(LAST_GREET_KEY);

                    if (lastGreetDateValue != null) {
                        LocalDate lastGreetDate = LocalDate.parse(lastGreetDateValue);
                        if (lastGreetDate.isEqual(today)) {
                            continue;
                        }
                    }


                    MessageToSend message = new MessageToSend(getGreetingText(), chat.chatId());
                    marvin.message().send(message);

                    marvin.pluginOptions().chat(chat.chatId()).set(LAST_GREET_KEY, today.toString());
                }
            } catch (Exception e) {
                logger.catching(e);
            }

        }

        protected String getGreetingText() {

            String[] emojiis = new String[]{"(celebrate)", "(beer)", "(monkey)", "(holidayspirit)", "(party)"};
            int randomIndex = new Random().nextInt(emojiis.length);

            String text = getLocaleBundle().getString("friday_greeting");
            text += " " + emojiis[randomIndex];

            return text;
        }

    }


}
