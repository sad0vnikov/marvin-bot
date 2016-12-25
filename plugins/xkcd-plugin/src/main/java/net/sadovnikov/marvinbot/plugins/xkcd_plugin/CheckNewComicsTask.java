package net.sadovnikov.marvinbot.plugins.xkcd_plugin;

import net.sadovnikov.marvinbot.api.BotPluginApi;
import net.sadovnikov.marvinbot.core.domain.message.MessageToSend;
import net.sadovnikov.marvinbot.core.schedule.Task;
import net.sadovnikov.marvinbot.core.service.chat.AbstractChat;
import net.sadovnikov.marvinbot.core.service.chat.Chat;
import net.sadovnikov.marvinbot.plugins.xkcd_plugin.image.LastXkcdImage;
import net.sadovnikov.marvinbot.plugins.xkcd_plugin.image.XkcdImage;
import org.apache.logging.log4j.Logger;


public class CheckNewComicsTask extends Task {

    BotPluginApi marvin;
    Logger logger;
    AbstractChat chat;

    public CheckNewComicsTask(BotPluginApi marvin, Logger logger, AbstractChat chat) {
        this.marvin = marvin;
        this.logger = logger;
        this.chat = chat;
    }

    @Override
    public void run() {
        try {
            XkcdImage lastImage = new LastXkcdImage();
            int imageNum = lastImage.num();
            String lastShownImageNum = marvin.pluginOptions().chat(chat).get("last_shown_comics_num");
            if (lastShownImageNum == null || imageNum > Integer.parseInt(lastShownImageNum)) {
                MessageToSend msg = new MessageToSend(lastImage.description(), chat);
                msg.addImage(lastImage.bytes(), lastImage.mimeType(), lastImage.name());

                marvin.message().send(msg);
            }

            marvin.pluginOptions().chat(chat).set("last_shown_comics_num", String.valueOf(imageNum));

        } catch (Throwable e) {
            logger.catching(e);
        }

    }
}
