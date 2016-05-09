package net.sadovnikov.marvinbot.core.db.repository;

/**
 * A plugin option for concrete chat
 */
public class PluginChatOption extends PluginOption {

    String chatId;

    public PluginChatOption(String chatId, String pluginName) {
        super(pluginName);
        this.chatId = chatId;
    }

    protected String getHashName() {
        return super.getHashName().concat(":").concat(chatId);
    }

}
