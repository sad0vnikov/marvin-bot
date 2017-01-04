package net.sadovnikov.marvinbot.plugins.xkcd_plugin;

import net.sadovnikov.marvinbot.core.db.DbException;
import net.sadovnikov.marvinbot.core.domain.Command;
import net.sadovnikov.marvinbot.core.domain.message.MessageToSend;
import net.sadovnikov.marvinbot.core.domain.user.UserRole;
import net.sadovnikov.marvinbot.core.events.event_types.MessageEvent;
import net.sadovnikov.marvinbot.core.plugin.Plugin;
import net.sadovnikov.marvinbot.core.plugin.PluginException;
import net.sadovnikov.marvinbot.core.schedule.Task;
import net.sadovnikov.marvinbot.core.service.CommandExecutor;
import net.sadovnikov.marvinbot.core.service.chat.AbstractChat;
import net.sadovnikov.marvinbot.core.service.chat.Chat;
import net.sadovnikov.marvinbot.core.service.message.MessageSenderException;
import net.sadovnikov.marvinbot.plugins.xkcd_plugin.image.LastXkcdImage;
import net.sadovnikov.marvinbot.plugins.xkcd_plugin.image.RandomXkcdImage;
import net.sadovnikov.marvinbot.plugins.xkcd_plugin.image.XkcdImage;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.PluginWrapper;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.Set;


public class XkcdPlugin extends Plugin {

    public XkcdPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    protected final long CHECK_INTERVAL_MILIS =  5 * 60 * 60 * 1000; // 5 hours

    public void start()
    {
        Set<AbstractChat> chatsToCheck = marvin.pluginOptions().chat().findChatswithOptionValues("checking_enabled", "on");
        for (AbstractChat chat : chatsToCheck) {
            addTaskForCheckingImages(chat);
        }
    }

    protected void sendRandomImg(AbstractChat chat) throws IOException {
        XkcdImage randomImage = new RandomXkcdImage();
        sendImage(randomImage, chat);
    }

    protected void sendLastImage(AbstractChat chat) throws IOException {
        XkcdImage lastImage = new LastXkcdImage();
        sendImage(lastImage, chat);
    }

    protected void sendImage(XkcdImage image, AbstractChat chat) throws IOException {
        byte[] imageBytes = image.bytes();

        String imageDesc = image.description();
        MessageToSend msg = new MessageToSend(imageDesc, chat);
        msg.addImage(imageBytes, image.mimeType(), image.name());

        try {
            marvin.message().send(msg);
        } catch (MessageSenderException e) {
            logger.catching(e);
        }
    }

    protected void addTaskForCheckingImages(AbstractChat chat) {
        Task checkTask = new CheckNewComicsTask(marvin, logger, chat);
        marvin.tasksSchedule().addTask(checkTask, new Date(), CHECK_INTERVAL_MILIS);
    }

    @net.sadovnikov.marvinbot.core.annotations.Command("xkcd")
    @Extension
    public class XkcdCommand extends CommandExecutor {

        @Override
        public void execute(Command cmd, MessageEvent ev) throws PluginException {

            Chat chat = ev.getMessage().chat();
            try {
                Optional<String> action = cmd.action();
                if (!action.isPresent()) {
                    sendRandomImg(chat);
                } else if (action.get().equals("last")) {
                    sendLastImage(chat);
                } else if (action.get().equals("on")) {
                    marvin.pluginOptions().chat(ev.getMessage().chat()).set("checking_enabled", "on");
                    String messageText = getLocaleBundle().getString("sending_new_comics_enabled");
                    marvin.message().reply(ev.getMessage(), messageText);
                    addTaskForCheckingImages(chat);
                } else if (action.get().equals("off")) {
                    marvin.pluginOptions().chat(ev.getMessage().chat()).set("checking_enabled", "off");
                    String messageText = getLocaleBundle().getString("sending_new_comics_disabled");
                    marvin.message().reply(ev.getMessage(), messageText);
                }
            } catch (IOException | DbException | MessageSenderException e) {
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
