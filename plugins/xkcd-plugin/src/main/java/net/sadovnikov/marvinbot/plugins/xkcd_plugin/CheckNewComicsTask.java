package net.sadovnikov.marvinbot.plugins.xkcd_plugin;

import net.sadovnikov.marvinbot.api.BotPluginApi;
import net.sadovnikov.marvinbot.core.domain.message.MessageToSend;
import net.sadovnikov.marvinbot.core.schedule.Task;
import net.sadovnikov.marvinbot.plugins.xkcd_plugin.image.LastXkcdImage;
import net.sadovnikov.marvinbot.plugins.xkcd_plugin.image.XkcdImage;
import org.apache.logging.log4j.Logger;


public class CheckNewComicsTask extends Task {

    BotPluginApi marvin;
    Logger logger;
    String chatId;

    public CheckNewComicsTask(BotPluginApi marvin, Logger logger, String chatId) {
        this.marvin = marvin;
        this.logger = logger;
        this.chatId = chatId;
    }

    @Override
    public void run() {
        try {
            XkcdImage lastImage = new LastXkcdImage();
            int imageNum = lastImage.num();
            String lastShownImageNum = marvin.pluginOptions().chat(chatId).get("last_shown_comics_num");
            if (lastShownImageNum == null || imageNum > Integer.parseInt(lastShownImageNum)) {
                MessageToSend msg = new MessageToSend(lastImage.description(), chatId);
                msg.addImage(lastImage.bytes(), lastImage.name());

                marvin.message().send(msg);
            }

            marvin.pluginOptions().chat(chatId).set("last_shown_comics_num", String.valueOf(imageNum));

        } catch (Throwable e) {
            logger.catching(e);
        }

    }
}
