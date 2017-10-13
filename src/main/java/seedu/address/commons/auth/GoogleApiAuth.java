package seedu.address.commons.auth;

import java.util.Arrays;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

/**
 * License Crediting: Code adapted from : https://developers.google.com/people/v1/getting-started
 * Authentication Service: Created by Philemon1 on 11/10/2017.
 * This class is used to facilitate the OAuth2 process with Google's APIs
 * Each command instance seeking to perform OAuth2 with Google is expected to maintain its own instance of this class
 * Authentication is bypassed if login has been performed in the session before
 * Client ID and Secret used belong to the creator (Philemon) 's project CS2103T-addressbook
 * Client ID name: doc client
 * https://console.developers.google.com/apis/credentials?project=cs2103t-addressbook
 */
public class GoogleApiAuth {
    /**
     * Class Constants
     */
    public static final String REDIRECT_URL = "https://cs2103tdummyendpoint.herokuapp.com";
    private static String clientId = "591065149112-69ikmid17q2trahg28gip4o8srmo47pv.apps.googleusercontent.com";
    private static String clientSecret = "tXcIFXQ1OXEz9NTtMVC4KSc7";
    private static String contactWriteScope = "https://www.googleapis.com/auth/contacts";
    private static String contactReadOnlyScope = "https://www.googleapis.com/auth/contacts.readonly";

    /**
     * Class Attributes
     */
    private TokenResponse authToken;
    private GoogleCredential credential;
    private HttpTransport httpTransport;
    private JacksonFactory jsonFactory;

    /**
     * Default constructor
     */
    public GoogleApiAuth() {
        httpTransport = new NetHttpTransport();
        jsonFactory = new JacksonFactory();
    }

    public TokenResponse getAuthToken() {
        return authToken;
    }

    public GoogleCredential getCredential() {
        return credential;
    }

    /**
     * Returns a URL for the caller to set in webview, enabling end-user to authenticate/login.
     * Scope of permissions: Google Contacts Read/Write
     * @return a String URL for caller to redirect.
     */
    public String getAuthContactWriteUrl() {
        return new GoogleBrowserClientRequestUrl(clientId, REDIRECT_URL, Arrays.asList(contactWriteScope)).build();
    }

    /**
     * @return String:  redirect URL used upon successful authentication
     */
    public String getRedirectUrl() {
        return REDIRECT_URL;
    }

    /**
     * Fully Sets up the GoogleApiAuth instance when a valid authCode is input.
     * Once this method has executed successfully, this instance's credentials can be accessed internally or externally
     * for HTTP calls to the Google People API, using Google API client libraries.
     * @param authCode
     * @return boolean indicating if set-up is successful or not
     */
    public boolean setupCredentials(String authCode) {
        System.out.println(authCode);
        authToken = new TokenResponse();
        authToken.setAccessToken(authCode);
        credential = new GoogleCredential().setFromTokenResponse(authToken);
        return true;
    }

}
