package net.sadovnikov.marvinbot.plugins.xkcd_plugin;

import net.sadovnikov.marvinbot.core.annotations.RequiredRole;
import net.sadovnikov.marvinbot.core.domain.Command;
import net.sadovnikov.marvinbot.core.domain.message.MessageToSend;
import net.sadovnikov.marvinbot.core.domain.user.UserRole;
import net.sadovnikov.marvinbot.core.events.event_types.MessageEvent;
import net.sadovnikov.marvinbot.core.plugin.Plugin;
import net.sadovnikov.marvinbot.core.plugin.PluginException;
import net.sadovnikov.marvinbot.core.service.CommandExecutor;
import net.sadovnikov.marvinbot.core.service.message.MessageSenderException;
import net.sadovnikov.marvinbot.plugins.xkcd_plugin.image.LastXkcdImage;
import net.sadovnikov.marvinbot.plugins.xkcd_plugin.image.RandomXkcdImage;
import net.sadovnikov.marvinbot.plugins.xkcd_plugin.image.XkcdImage;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.PluginWrapper;

import java.io.IOException;


public class XkcdPlugin extends Plugin {

    public XkcdPlugin(PluginWrapper wrapper) {
        super(wrapper);
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
                    //@todo send new comics each time it is published
                } else if (args.length == 1 && args[0].equals("off")) {

                }
            } catch (IOException e) {
                throw new PluginException(e);
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
    }

}
