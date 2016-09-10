package net.sadovnikov.marvinbot.plugins.xkcd_plugin;

import net.sadovnikov.marvinbot.core.annotations.RequiredRole;
import net.sadovnikov.marvinbot.core.db.DbException;
import net.sadovnikov.marvinbot.core.domain.Command;
import net.sadovnikov.marvinbot.core.domain.message.MessageToSend;
import net.sadovnikov.marvinbot.core.domain.user.UserRole;
import net.sadovnikov.marvinbot.core.events.event_types.MessageEvent;
import net.sadovnikov.marvinbot.core.plugin.Plugin;
import net.sadovnikov.marvinbot.core.plugin.PluginException;
import net.sadovnikov.marvinbot.core.schedule.Task;
import net.sadovnikov.marvinbot.core.service.CommandExecutor;
import net.sadovnikov.marvinbot.core.service.message.MessageSenderException;
import net.sadovnikov.marvinbot.plugins.xkcd_plugin.image.LastXkcdImage;
import net.sadovnikov.marvinbot.plugins.xkcd_plugin.image.RandomXkcdImage;
import net.sadovnikov.marvinbot.plugins.xkcd_plugin.image.XkcdImage;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.PluginWrapper;
import sun.plugin.dom.exception.PluginNotSupportedException;

import java.io.IOException;
import java.util.Date;
import java.util.Set;


public class XkcdPlugin extends Plugin {

    public XkcdPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    protected final long CHECK_INTERVAL_MILIS =  5 * 60 * 60 * 1000; // 5 hours

    public void start()
    {
        Set<String> chatsToCheck = marvin.pluginOptions().chat("").findChatswithOptionValues("checking_enabled", "on");
        for (String chatId : chatsToCheck) {
            addTaskForCheckingImages(chatId);
        }
    }

    protected void sendRandomImg(String chatId) throws IOException {
        XkcdImage randomImage = new RandomXkcdImage();
        sendImage(randomImage, chatId);
    }

    protected void sendLastImage(String chatId) throws IOException {
        XkcdImage lastImage = new LastXkcdImage();
        sendImage(lastImage, chatId);
    }

    protected void sendImage(XkcdImage image, String chatId) throws IOException {
        byte[] imageBytes = image.bytes();

        String imageDesc = image.description();
        MessageToSend msg = new MessageToSend(imageDesc, chatId);
        msg.addImage(imageBytes, image.name());

        try {
            marvin.message().send(msg);
        } catch (MessageSenderException e) {
            logger.catching(e);
        }
    }

    protected void addTaskForCheckingImages(String chatId) {
        Task checkTask = new CheckNewComicsTask(marvin, logger, chatId);
        marvin.tasksSchedule().addTask(checkTask, new Date(), CHECK_INTERVAL_MILIS);
    }

    @net.sadovnikov.marvinbot.core.annotations.Command("xkcd")
    @RequiredRole(UserRole.class)
    @Extension
    public class XkcdCommand extends CommandExecutor {

        @Override
        public void execute(Command cmd, MessageEvent ev) throws PluginException {

            String chatId = ev.getMessage().chatId();
            try {
                String[] args = cmd.getArgs();
                if (args.length == 0) {
                    sendRandomImg(chatId);
                } else if (args.length == 1 && args[0].equals("last")) {
                    sendLastImage(chatId);
                } else if (args.length == 1 && args[0].equals("on")) {
                    marvin.pluginOptions().chat(ev.getMessage().chatId()).set("checking_enabled", "on");
                    addTaskForCheckingImages(ev.getMessage().chatId());
                } else if (args.length == 1 && args[0].equals("off")) {
                    marvin.pluginOptions().chat(ev.getMessage().chatId()).set("checking_enabled", "off");
                }
            } catch (IOException | DbException e) {
                throw new PluginException(e);
            }

        }

        @Override
        public String getHelp() {
            return getLocaleBundle().getString("pluginHelp");
        }

        @Override
        public String getUsage() {
            return commandPrefix + "xkcd [on|off|last]";
        }
    }

}
