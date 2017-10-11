package seedu.address.commons.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.util.Arrays;

/**
 * License Crediting: Code adapted from : https://developers.google.com/people/v1/getting-started
 * Created by Philemon1 on 11/10/2017.
 * This class is used to facilitate the OAuth2 process with Google's APIs
 * Client ID and Secret used belong to the creator (Philemon) 's project CS2103T-addressbook
 * Client ID name: doc client
 * https://console.developers.google.com/apis/credentials?project=cs2103t-addressbook
 */
public class GoogleApiAuth {
    /**
     * Class Constants
     */
    private static String clientId = "591065149112-69ikmid17q2trahg28gip4o8srmo47pv.apps.googleusercontent.com";
    private static String clientSecret = "tXcIFXQ1OXEz9NTtMVC4KSc7";
    private static String contactWriteScope = "https://www.googleapis.com/auth/contacts";
    private static String contactReadOnlyScope = "https://www.googleapis.com/auth/contacts.readonly";
    private static String redirectUrl = "https://cs2103tdummyendpoint.herokuapp.com";

    /**
     * Class Attributes
     */
    private GoogleTokenResponse authToken;
    private GoogleCredential credential;
    private HttpTransport httpTransport;
    private JacksonFactory jsonFactory;

    /**
     * Default constructor
     */
    public GoogleApiAuth(){
        httpTransport = new NetHttpTransport();
        jsonFactory = new JacksonFactory();
    }

    public GoogleTokenResponse getAuthToken() {
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
        return new GoogleBrowserClientRequestUrl(clientId, redirectUrl, Arrays.asList(contactWriteScope)).build();
    }

    /**
     * Fully Sets up the GoogleApiAuth instance when a valid authCode is input.
     * Once this method has executed successfully, this instance's credentials can be accessed internally or externally
     * for HTTP calls to the Google People API, using Google API client libraries.
     * @param authCode
     * @return boolean indicating if set-up is successful or not
     */
    public boolean setupCredentials(String authCode) {
        try {
            setAuthToken(authCode);
        } catch(IOException e) {
            return false;
        }
        // Input parameter is valid, attempt to generate credentials now
        credential = new GoogleCredential.Builder()
                    .setTransport(httpTransport)
                    .setJsonFactory(jsonFactory)
                    .setClientSecrets(clientId, clientSecret)
                    .build()
                    .setFromTokenResponse(authToken);
        return true;
    }

    /**
     * Helper method called by setupCredentials(), to obtain authToken from user input authCode
     * @param authCode
     * @throws IOException
     */
    private void setAuthToken(String authCode) throws IOException {
        if(authCode == null) {
            throw new IOException("authCode cannnot be null");
        }
        authToken =  new GoogleAuthorizationCodeTokenRequest(
                httpTransport, jsonFactory, clientId, clientSecret, authCode, redirectUrl)
                .execute();
    }



}
