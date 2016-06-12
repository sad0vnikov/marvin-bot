package net.sadovnikov.marbinbot.plugins.git_notifier_plugin;

import com.google.inject.Inject;
import com.sun.net.httpserver.*;
import net.sadovnikov.marbinbot.plugins.git_notifier_plugin.webhook_catchers.BitbucketWebhookCatcher;
import net.sadovnikov.marbinbot.plugins.git_notifier_plugin.webhook_catchers.Commit;
import net.sadovnikov.marbinbot.plugins.git_notifier_plugin.webhook_catchers.WebhookCatcher;
import net.sadovnikov.marvinbot.core.command.CommandExecutor;
import net.sadovnikov.marvinbot.core.command.annotations.Command;
import net.sadovnikov.marvinbot.core.events.event_types.MessageEvent;
import net.sadovnikov.marvinbot.core.message.SentMessage;
import net.sadovnikov.marvinbot.core.message_sender.MessageSender;
import net.sadovnikov.marvinbot.core.plugin.Plugin;
import net.sadovnikov.marvinbot.core.plugin.PluginException;
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

    MessageSender messageSender;

    @Inject
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public void start() {
        HttpListener listener = new HttpListener();
        listener.start();
    }

    protected class HttpListener extends Thread {

        public void run() {
            try {
                HttpServer server = HttpServer.create(new InetSocketAddress(DEFAULT_PORT), 0);
                server.createContext("/", new WebhookHandler());
                server.start();
            } catch (IOException e) {
                logger.catching(e);
            }
        }
    }


    protected class WebhookHandler implements HttpHandler
    {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));
                String requestBody = "";
                while (true) {
                    String line = in.readLine();
                    if (line == null) {
                        break;
                    }
                    requestBody += line;
                }
                logger.debug("incoming HTTP request (body: " + requestBody  + ")");
                JSONParser jsonParser = new JSONParser();
                JSONObject obj = (JSONObject) jsonParser.parse(requestBody);
                WebhookCatcher catcher = new BitbucketWebhookCatcher(obj);
                if (catcher.getIsPushNotification()) {

                    Map<String, String> pushInfo = catcher.getPushInfo();
                    String repName = pushInfo.get("repository");
                    logger.info("got bitbucket push notification for repository " + repName);

                    Commit[] commits = catcher.getPushedCommits();
                    String message = pushInfo.get("initiator") + " pushed ";
                    if (commits.length > 0) {
                        message += String.valueOf(commits.length) + " commits to " + pushInfo.get("repository") + ":\n";
                        for (int i = 0; i < commits.length; i++) {
                            message += commits[i].getHash() + " " + commits[i].getUser() + " | " + commits[i].getMessage() + "\n";
                        }

                        for (String chatId : findChatswithOptionValues("repositories_to_notify", repName)) {
                            SentMessage msgObj = new SentMessage();
                            msgObj.setText(message);
                            msgObj.setRecepientId(chatId);
                            messageSender.sendMessage(msgObj);
                        }

                    }
                }
                String responseBody = "<html><body>ok</body></html>";
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(httpExchange.getResponseBody()));
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, responseBody.length());
                out.write(responseBody);
                out.flush();
                httpExchange.close();

            } catch (Exception e) {
                logger.catching(e);
            }
        }

    }

    @Extension
    @Command("git-notifier")
    public class AddNotifierCommand extends CommandExecutor {


        public void execute(net.sadovnikov.marvinbot.core.command.Command command, MessageEvent ev) {
            String[] args = command.getArgs();
            if (args.length != 2 || (!args[0].equals("add") && !args[0].equals("remove"))) {
                messageSender.reply(ev.getMessage(), getUsage());
                return;
            }

            String chatId = ev.getMessage().getChatId();
            String addr = args[1];

            if (args[0].equals("add")) {

                if (addr.contains("bitbucket")) {
                    addRepository(chatId, addr);
                    messageSender.reply(ev.getMessage(), "Repository added");
                } else {
                    messageSender.reply(ev.getMessage(), "Only bitbucket repositories are supported now");
                }

            }

            if (args[0].equals("remove")) {
                removeRepository(chatId, addr);
                messageSender.reply(ev.getMessage(), "Repository removed");
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
                return new HashSet(getListChatOption(chatId, "repositories_to_notify"));

            } catch (PluginException e) {
                logger.catching(e);
                return new HashSet<String>();
            }
        }

        protected void saveChatRepsList(String chatId, Set<String> list) {
            try {
                setChatOption(chatId, "repositories_to_notify", new ArrayList<>(list));
            } catch (PluginException e) {
                logger.catching(e);
            }
        }

        public String getUsage() {
            return  "Usage: " + commandPrefix + "git-notifier add [repository_url]";
        }
    }
}
