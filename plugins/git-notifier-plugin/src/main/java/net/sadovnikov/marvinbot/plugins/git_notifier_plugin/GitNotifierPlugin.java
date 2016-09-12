package net.sadovnikov.marvinbot.plugins.git_notifier_plugin;

import com.sun.net.httpserver.HttpExchange;
import net.sadovnikov.marvinbot.plugins.http_server.HttpEndpoint;
import net.sadovnikov.marvinbot.plugins.http_server.HttpHandler;
import net.sadovnikov.marvinbot.plugins.git_notifier_plugin.webhook_catchers.BitbucketWebhookCatcher;
import net.sadovnikov.marvinbot.plugins.git_notifier_plugin.webhook_catchers.Commit;
import net.sadovnikov.marvinbot.plugins.git_notifier_plugin.webhook_catchers.WebhookCatcher;
import net.sadovnikov.marvinbot.core.db.DbException;
import net.sadovnikov.marvinbot.core.domain.message.MessageToSend;
import net.sadovnikov.marvinbot.core.service.CommandExecutor;
import net.sadovnikov.marvinbot.core.annotations.Command;
import net.sadovnikov.marvinbot.core.annotations.RequiredRole;
import net.sadovnikov.marvinbot.core.events.event_types.MessageEvent;
import net.sadovnikov.marvinbot.core.domain.user.ChatModeratorRole;
import net.sadovnikov.marvinbot.core.plugin.Plugin;
import net.sadovnikov.marvinbot.core.service.message.MessageSenderException;
import net.sadovnikov.marvinbot.plugins.http_server.HttpRequest;
import net.sadovnikov.marvinbot.plugins.http_server.HttpResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.PluginWrapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.*;


public class GitNotifierPlugin extends Plugin {

    private final int DEFAULT_PORT = 8080;

    public GitNotifierPlugin(PluginWrapper pluginWrapper) {
        super(pluginWrapper);
    }



    @HttpEndpoint("/git-webhooks")
    @Extension
    public class WebhookHandler extends HttpHandler
    {

        @Override
        public HttpResponse handle(HttpRequest request) {
            try {
                String requestBody = request.getBody();
                logger.debug("incoming HTTP request (body: " + requestBody  + ")");
                JSONParser jsonParser = new JSONParser();
                JSONObject obj = (JSONObject) jsonParser.parse(requestBody);
                WebhookCatcher catcher = new BitbucketWebhookCatcher(obj);
                if (catcher.getIsPushNotification()) {

                    Map<String, String> pushInfo = catcher.getPushInfo();
                    String repName = pushInfo.get("repository");
                    logger.info("got bitbucket push notification for repository " + repName);

                    Commit[] commits = catcher.getPushedCommits();
                    String message = " === " + pushInfo.get("initiator") + " pushed ";

                    Set<String> affectedBranches = catcher.getAffectedBranches();

                    if (commits.length > 0 && affectedBranches.contains("master")) {
                        message += String.valueOf(commits.length) + " commits to " + pushInfo.get("repository") + ": ===\n";
                        for (int i = 0; i < commits.length; i++) {
                            message += commits[i].getHash() + " " + commits[i].getUser() + "\n    " + commits[i].getMessage() + "\n";
                        }

                        for (String chatId : marvin.pluginOptions().chat(null).findChatswithOptionValues("repositories_to_notify", repName)) {
                            MessageToSend msgObj = new MessageToSend(message, chatId);
                            marvin.message().send(msgObj);
                        }

                    }
                }
                String responseBody = "<html><body>ok</body></html>";
                return new HttpResponse(responseBody);

            } catch (Exception e) {
                logger.catching(e);
            }

            return new HttpResponse("<html><body>an internal error occured</body></html>", HttpResponse.STATUS_INTERNAL_ERROR);
        }

    }

    @Extension
    @Command("git-notifier")
    @RequiredRole(ChatModeratorRole.class)
    public class AddNotifierCommand extends CommandExecutor {


        public void execute(net.sadovnikov.marvinbot.core.domain.Command command, MessageEvent ev) {
            String[] args = command.getArgs();

            try {
                if (args.length != 2 || (!args[0].equals("add") && !args[0].equals("remove"))) {
                    marvin.message().reply(ev.getMessage(), getUsage());
                    return;
                }

                String chatId = ev.getMessage().chatId();
                String addr = args[1];

                if (args[0].equals("add")) {

                    if (addr.contains("bitbucket")) {
                        addRepository(chatId, addr);
                        marvin.message().reply(ev.getMessage(), getLocaleBundle().getString("repositoryAdded"));
                    } else {
                        marvin.message().reply(ev.getMessage(), getLocaleBundle().getString("repositoryTypeRestrictions"));
                    }

                }

                if (args[0].equals("remove")) {
                    removeRepository(chatId, addr);
                    marvin.message().reply(ev.getMessage(), getLocaleBundle().getString("repositoryRemoved"));
                }

            } catch (MessageSenderException e) {
                logger.catching(e);
            }

        }

        protected void addRepository(String chatId, String addr) {
            Set<String> repositories = getChatRepsList(chatId);
            repositories.add(addr);
            saveChatRepsList(chatId, repositories);
        }

        protected void removeRepository(String chatId, String addr) {
            Set<String> repositories = getChatRepsList(chatId);
            repositories.remove(addr);
            saveChatRepsList(chatId, repositories);
        }

        protected Set<String> getChatRepsList(String chatId) {
            try {

                List<String> valuesList = marvin.pluginOptions()
                        .chat(chatId)
                        .getValuesList("repositories_to_notify");

                return new HashSet<>(valuesList);

            } catch (DbException e) {
                logger.catching(e);
                return new HashSet<>();
            }
        }

        protected void saveChatRepsList(String chatId, Set<String> list) {
            try {
                marvin.pluginOptions().chat(chatId).set("repositories_to_notify", new ArrayList<>(list));
            } catch (DbException e) {
                logger.catching(e);
            }
        }

        @Override
        public String getHelp() {
            return getLocaleBundle().getString("pluginHelp");
        }

        public String getUsage() {
            return  "Usage: " + commandPrefix + "git-notifier add [repository_url]";
        }
    }
}
