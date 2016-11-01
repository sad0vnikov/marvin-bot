package net.sadovnikov.marvinbot.plugins.custom_notification_webhooks;

import net.sadovnikov.marvinbot.core.annotations.Command;
import net.sadovnikov.marvinbot.core.annotations.RequiredRole;
import net.sadovnikov.marvinbot.core.domain.message.MessageToSend;
import net.sadovnikov.marvinbot.core.domain.user.ChatModeratorRole;
import net.sadovnikov.marvinbot.core.events.event_types.MessageEvent;
import net.sadovnikov.marvinbot.core.plugin.Plugin;
import net.sadovnikov.marvinbot.core.plugin.PluginException;
import net.sadovnikov.marvinbot.core.service.CommandExecutor;
import net.sadovnikov.marvinbot.core.service.message.MessageSenderException;
import net.sadovnikov.marvinbot.plugins.http_server.HttpEndpoint;
import net.sadovnikov.marvinbot.plugins.http_server.HttpHandler;
import net.sadovnikov.marvinbot.plugins.http_server.HttpRequest;
import net.sadovnikov.marvinbot.plugins.http_server.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.PluginWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class CustomNotificationPlugin extends Plugin {

    public static final String NOTIFICATION_TYPES_OPTION = "custom_webhooks_notifications_types";
    public static final String ALL_NOTIFICATIONS_TYPES_CMD_ARGUMENT = "all";
    public static final String NONE_NOTIFICATIONS_TYPES_CMD_ARGUMENT = "none";

    protected Logger logger = LogManager.getLogger(getClass());

    public CustomNotificationPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }


    @Extension
    @HttpEndpoint("/notify")
    public class WebhookHandler extends HttpHandler {

        @Override
        public HttpResponse handle(HttpRequest request) {
            String requestBody = request.getBody();
            JSONParser jsonParser = new JSONParser();
            try {
                JSONObject jsonData = (JSONObject) jsonParser.parse(requestBody);
                String message = (String) jsonData.get("message");

                JSONObject resp = new JSONObject();

                if (message == null) {
                    resp.put("status", "error");
                    resp.put("message", "'message' parameter is required");
                    return new HttpResponse(resp.toJSONString(), HttpResponse.STATUS_BAD_REQUEST);
                }

                String type = (String) jsonData.get("type");
                Set<String> chatsIds = marvin.pluginOptions().chat("")
                        .findChatsOptionValuesIn(NOTIFICATION_TYPES_OPTION, new String[]{ type, ALL_NOTIFICATIONS_TYPES_CMD_ARGUMENT });

                for (String chatId : chatsIds) {
                    MessageToSend messageToSend = new MessageToSend(message, chatId);
                    marvin.message().send(messageToSend);
                }

                resp.put("status", "ok");
                resp.put("message", "sent message to " + chatsIds.size() + (chatsIds.size() == 1 ? " chat" : " chats"));
                return new HttpResponse(resp.toJSONString());

            } catch (ParseException e) {
                logger.catching(e);
                JSONObject resp = new JSONObject();
                resp.put("status", "error");
                resp.put("error", "got invalid JSON");
                return new HttpResponse(resp.toJSONString(), HttpResponse.STATUS_BAD_REQUEST);
            } catch (MessageSenderException e) {
                logger.catching(e);
                JSONObject resp = new JSONObject();
                resp.put("status", "error");
                resp.put("error", "internal error");
                return new HttpResponse(resp.toJSONString(), HttpResponse.STATUS_INTERNAL_ERROR);
            }

        }
    }

    @Extension
    @Command("notification-types")
    @RequiredRole(ChatModeratorRole.class)
    public class SetChatNotificationsTypes extends CommandExecutor {

        @Override
        public void execute(net.sadovnikov.marvinbot.core.domain.Command cmd, MessageEvent ev) throws PluginException {

            String[] args = cmd.getArgs();
            String chatId = ev.getMessage().chatId();
            String commandResponseText;
            List<String> enabledNotificationsTypes = new ArrayList<>();

            try {

                if (args.length == 0) {
                    enabledNotificationsTypes = marvin.pluginOptions().chat(chatId).getValuesList(NOTIFICATION_TYPES_OPTION);

                } else {

                    for (String arg : args) {
                        enabledNotificationsTypes.add(arg);
                    }

                    marvin.pluginOptions().chat(chatId).set(NOTIFICATION_TYPES_OPTION, enabledNotificationsTypes);
                }

                if ((enabledNotificationsTypes.size() == 1 && enabledNotificationsTypes.get(0).equals(NONE_NOTIFICATIONS_TYPES_CMD_ARGUMENT))
                        || enabledNotificationsTypes.size() == 0) {

                    commandResponseText = getLocaleBundle().getString("custom_notifications_disabled");

                } else if (enabledNotificationsTypes.size() == 1 && enabledNotificationsTypes.get(0).equals(ALL_NOTIFICATIONS_TYPES_CMD_ARGUMENT)) {
                    commandResponseText = getLocaleBundle().getString("all_custom_notifications_enabled");
                } else {
                    commandResponseText = String.format(getLocaleBundle().getString("following_custom_notifications_enabled"), String.join(",", enabledNotificationsTypes));
                }

                marvin.message().reply(ev.getMessage(), commandResponseText);

            } catch (Exception e) {
                logger.catching(e);
            }


        }

        @Override
        public String getHelp() {
            return getLocaleBundle().getString("help");
        }

        @Override
        public String getUsage() {
            return commandPrefix + " type1 type2";
        }
    }

}
