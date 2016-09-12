package net.sadovnikov.marvinbot.plugins.git_notifier_plugin.webhook_catchers;

import org.json.simple.JSONObject;

import java.util.Map;
import java.util.Set;

public abstract class WebhookCatcher {

    JSONObject request;

    public WebhookCatcher(JSONObject request) {
        this.request = request;
    }

    public abstract boolean getIsPushNotification();

    public abstract Map<String,String> getPushInfo();

    public abstract Set<String> getAffectedBranches();

    public abstract Commit[] getPushedCommits();

}
