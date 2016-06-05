package net.sadovnikov.marbinbot.plugins.git_notifier_plugin.webhook_catchers;

import com.mongodb.util.JSON;
import org.apache.logging.log4j.LogManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

public class BitbucketWebhookCatcher extends WebhookCatcher {

    public BitbucketWebhookCatcher(JSONObject request) {
        super(request);
    }

    @Override
    public boolean getIsPushNotification() {
        return request.get("push") != null;
    }

    @Override
    public Map<String,String> getPushInfo() {
        Map<String,String> pushInfo = new HashMap<>();


        try {
            JSONObject userInfo = (JSONObject) request.get("actor");
            pushInfo.put("initiator", (String) userInfo.get("display_name"));

            JSONObject repositoryInfo = (JSONObject) request.get("repository");
            pushInfo.put("repository", "http://bitbucket.org/" + repositoryInfo.get("full_name").toString());

        } catch (Exception e) {
            LogManager.getLogger("core-logger").catching(e);
            return pushInfo;
        }


        return pushInfo;
    }

    @Override
    public Commit[] getPushedCommits() {

        List<Commit> commits = new ArrayList<>();

        JSONObject push = (JSONObject) request.get("push");
        JSONArray changes = (JSONArray) push.get("changes");
        Iterator<JSONObject> changesIterator = changes.iterator();
        while (changesIterator.hasNext()) {
            JSONObject ch = changesIterator.next();

            JSONObject newChanges = (JSONObject) ch.get("new");
            if (newChanges == null) {
                continue;
            }

            JSONArray commitsJsonArray = (JSONArray) ch.get("commits");
            Iterator<JSONObject> commitsIterator = commitsJsonArray.iterator();


            while (commitsIterator.hasNext()) {
                JSONObject commitJson = commitsIterator.next();
                String commitHash = commitJson.get("hash").toString();
                String commitAuthor = commitJson.get("author").toString();
                String commitMessage = commitJson.get("message").toString();

                Commit commit = new Commit(commitHash, commitAuthor, commitMessage);
                commits.add(commit);
            }

        }

        return commits.toArray(new Commit[commits.size()]);

    }


}
