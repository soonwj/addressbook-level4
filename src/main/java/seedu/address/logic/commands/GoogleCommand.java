package seedu.address.logic.commands;

import java.io.IOException;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

/**This class is the parent class for all commands calling Google's APIs
 * Created by Philemon1 on 21/10/2017.
 */
public abstract class GoogleCommand extends Oauth2Command {
    protected static final String CLIENT_ID =
            "591065149112-69ikmid17q2trahg28gip4o8srmo47pv.apps.googleusercontent.com";
    private static final String SERVICE_SOURCE = "GOOGLE";
    protected String accessScope;
    protected TokenResponse authToken;
    protected GoogleCredential credential;
    protected HttpTransport httpTransport;
    protected JacksonFactory jsonFactory;

    protected GoogleCommand(String googleCommandType, String inputAccessScope) throws IOException {
        super(SERVICE_SOURCE + "_" + googleCommandType);
        if (googleCommandType == null) {
            throw new IOException("Unknown Google Command type");
        }
        if (inputAccessScope == null) {
            throw new IOException("Unknown Google Command scope");
        }
        accessScope = inputAccessScope;
        httpTransport = new NetHttpTransport();
        jsonFactory = new JacksonFactory();
    }

    protected GoogleCommand() throws IOException {
        super();
    }

    /**Instantiates the GoogleCredentials for OAuth2 requests.
     * This is the final step in the OAuth2 protocol for Google APIs
     * @param authCode
     */
    protected void setupCredentials(String authCode) {
        authToken = new TokenResponse();
        authToken.setAccessToken(authCode);
        credential = new GoogleCredential().setFromTokenResponse(authToken);
    }
}
