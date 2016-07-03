package net.sadovnikov.marvinbot.core.service.client;

import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.SkypeBuilder;
import com.samczsun.skype4j.Visibility;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.samczsun.skype4j.exceptions.InvalidCredentialsException;
import com.samczsun.skype4j.exceptions.NotParticipatingException;

/**
 * This is a skype net.sadovnikov.marvinbot.core.service.client implementation whilch uses Skype4j lib
 */
public class Skype4jClient extends SkypeClient {

    private Skype skype;

    public Skype4jClient(String login, String password) {
        this.skype = new SkypeBuilder(login, password).withAllResources().build();

    }

    @Override
    public void connect() {
        try {
            skype.login();
            skype.subscribe();
        } catch (InvalidCredentialsException | NotParticipatingException | ConnectionException e) {
            throw new ClientException(e);
        }
    }

    @Override
    public void setVisible() {
        try {
            skype.setVisibility(Visibility.ONLINE);
        } catch (ConnectionException e) {
            throw new ClientException(e);
        }
    }

    @Override
    public void disconnect() throws ClientException {
        try {
            skype.logout();
        } catch (ConnectionException e) {
            throw new ClientException(e);
        }
    }

    @Override
    public void setStatusText() throws ClientException {
        throw new ClientException("now implemented yet");
    }

    public Skype getSkype4jInstance() {
        return skype;
    }
}
