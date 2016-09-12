package net.sadovnikov.marvinbot.plugins.git_notifier_plugin.webhook_catchers;

import org.apache.logging.log4j.LogManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


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
            pushInfo.put("repository", "https://bitbucket.org/" + repositoryInfo.get("full_name").toString());

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

            JSONArray commitsJsonArray = (JSONArray) ch.get("commits");

            if (commitsJsonArray == null) {
                continue;
            }

            Iterator<JSONObject> commitsIterator = commitsJsonArray.iterator();


            while (commitsIterator.hasNext()) {
                JSONObject commitJson = commitsIterator.next();
                String commitHash = commitJson.get("hash").toString();

                JSONObject authorInfo = (JSONObject) commitJson.get("author");

                String commitAuthor   = authorInfo.get("raw").toString();
                String commitMessage = commitJson.get("message").toString();



                Commit commit = new Commit(commitHash, commitAuthor, commitMessage);
                commits.add(commit);
            }

        }

        return commits.toArray(new Commit[commits.size()]);

    }


    public Set<String> getAffectedBranches() {

        JSONObject push = (JSONObject) request.get("push");
        JSONArray changes = (JSONArray) push.get("changes");
        Iterator<JSONObject> changesIterator = changes.iterator();

        Set<String> affectedBranches = new HashSet<>();
        while (changesIterator.hasNext()) {
            JSONObject ch = changesIterator.next();
            JSONObject n = (JSONObject) ch.get("new");

            String branch = null;
            if (n != null) {
                branch = (String) n.get("name");
            }

            if (branch != null) {
                affectedBranches.add(branch);
            }
        }

        return affectedBranches;
    }
}
