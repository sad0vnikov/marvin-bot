package net.sadovnikov.marvinbot.plugins.git_notifier_plugin;

import net.sadovnikov.marvinbot.core.service.chat.AbstractChat;
import net.sadovnikov.marvinbot.core.service.chat.Chat;
import net.sadovnikov.marvinbot.plugins.http_server.HttpEndpoint;
import net.sadovnikov.marvinbot.plugins.http_server.HttpHandler;
import net.sadovnikov.marvinbot.plugins.git_notifier_plugin.webhook_catchers.BitbucketWebhookCatcher;
import net.sadovnikov.marvinbot.plugins.git_notifier_plugin.webhook_catchers.Commit;
import net.sadovnikov.marvinbot.plugins.git_notifier_plugin.webhook_catchers.WebhookCatcher;
import net.sadovnikov.marvinbot.core.db.DbException;
import net.sadovnikov.marvinbot.core.domain.message.MessageToSend;
import net.sadovnikov.marvinbot.core.service.CommandExecutor;
import net.sadovnikov.marvinbot.core.annotations.Command;
import net.sadovnikov.marvinbot.core.events.event_types.MessageEvent;
import net.sadovnikov.marvinbot.core.domain.user.ChatModeratorRole;
import net.sadovnikov.marvinbot.core.plugin.Plugin;
import net.sadovnikov.marvinbot.core.service.message.MessageSenderException;
import net.sadovnikov.marvinbot.plugins.http_server.HttpRequest;
import net.sadovnikov.marvinbot.plugins.http_server.HttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.PluginWrapper;


import java.util.*;


public class GitNotifierPlugin extends Plugin {

    public GitNotifierPlugin(PluginWrapper pluginWrapper) {
        super(pluginWrapper);
    }



    @HttpEndpoint("/git-webhooks")
    @Extension
    public class WebhookHandler extends HttpHandler {

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

                        for (AbstractChat chat : marvin.pluginOptions().chat(null).findChatswithOptionValues("repositories_to_notify", repName)) {
                            MessageToSend msgObj = new MessageToSend(message, chat);
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
    public class AddNotifierCommand extends CommandExecutor {


        public void execute(net.sadovnikov.marvinbot.core.domain.Command command, MessageEvent ev) {
            String[] args = command.getArgs();

            try {

                Chat chat = ev.getMessage().chat();
                Optional<String> action = Optional.empty();
                if (args.length > 0) {
                    action = Optional.of(args[0]);
                }
                Optional<String> addr = Optional.empty();
                if (args.length > 1) {
                    addr = Optional.of(args[1]);
                }

                if (action.isPresent() && action.get().equals("add") && addr.isPresent()) {

                    if (addr.get().contains("bitbucket")) {
                        addRepository(chat, addr.get());
                        marvin.message().reply(ev.getMessage(), getLocaleBundle().getString("repositoryAdded"));
                    } else {
                        marvin.message().reply(ev.getMessage(), getLocaleBundle().getString("repositoryTypeRestrictions"));
                    }

                } else if (action.isPresent() && action.get().equals("remove") && addr.isPresent()) {
                    removeRepository(chat, addr.get());
                    marvin.message().reply(ev.getMessage(), getLocaleBundle().getString("repositoryRemoved"));

                } else if (action.isPresent() && action.get().equals("list")) {
                    Set<String> repositories = getChatRepsList(chat);
                    String msgText;
                    if (repositories.size() == 0) {
                        msgText = getLocaleBundle().getString("repositoriesListIsEmpty");
                    } else {
                        String repsList = StringUtils.join(repositories.iterator(), "\n");
                        msgText = getLocaleBundle().getString("repositoriesList") + "\n" + repsList;
                    }

                    marvin.message().reply(ev.getMessage(), msgText);
                } else {
                    marvin.message().reply(ev.getMessage(), getUsage());
                }

            } catch (MessageSenderException e) {
                logger.catching(e);
            }

        }

        protected void addRepository(Chat chat, String addr) {
            Set<String> repositories = getChatRepsList(chat);
            repositories.add(addr);
            saveChatRepsList(chat, repositories);
        }

        protected void removeRepository(Chat chat, String addr) {
            Set<String> repositories = getChatRepsList(chat);
            repositories.remove(addr);
            saveChatRepsList(chat, repositories);
        }

        protected Set<String> getChatRepsList(Chat chat) {
            try {

                List<String> valuesList = marvin.pluginOptions()
                        .chat(chat)
                        .getValuesList("repositories_to_notify");

                return new HashSet<>(valuesList);

            } catch (DbException e) {
                logger.catching(e);
                return new HashSet<>();
            }
        }

        protected void saveChatRepsList(Chat chat, Set<String> list) {
            try {
                marvin.pluginOptions().chat(chat).set("repositories_to_notify", new ArrayList<>(list));
            } catch (DbException e) {
                logger.catching(e);
            }
        }

        @Override
        public String getHelp() {
            return getLocaleBundle().getString("pluginHelp");
        }

        public String getUsage() {
            return  "Usage: " + commandPrefix + "git-notifier add | remove | list";
        }
    }
}
