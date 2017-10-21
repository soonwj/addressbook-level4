package seedu.address.ui;

import java.net.URL;
import java.util.Observable;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import seedu.address.MainApp;
import seedu.address.commons.auth.GoogleApiAuth;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.logic.GoogleApiAuthServiceCredentialsSetupCompleted;
import seedu.address.commons.events.logic.GoogleAuthRequestEvent;
import seedu.address.commons.events.logic.GoogleAuthSuccessEvent;
import seedu.address.commons.events.logic.GoogleAuthenticationSuccessEvent;
import seedu.address.commons.events.ui.FindLocationRequestEvent;
import seedu.address.commons.events.ui.Oauth2BrowserRequestEvent;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.logic.commands.Oauth2Command;
import seedu.address.model.person.ReadOnlyPerson;



/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    public static final String DEFAULT_PAGE = "default.html";
    public static final String GOOGLE_MAPS_URL = "https://www.google.com.sg/maps/place/";
    public static final String GOOGLE_SEARCH_URL_PREFIX = "https://www.google.com.sg/search?safe=off&q=";
    public static final String GOOGLE_SEARCH_URL_SUFFIX = "&cad=h";

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private WebView browser;

    private String currentUrl = "";
    private GoogleApiAuth authService;
    private ChangeListener currentUrlListener;

    public BrowserPanel() {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        loadDefaultPage();
        registerAsAnEventHandler(this);
    }


    private void loadPersonPage(ReadOnlyPerson person) {
        loadPage(GOOGLE_SEARCH_URL_PREFIX + person.getName().fullName.replaceAll(" ", "+")
                + GOOGLE_SEARCH_URL_SUFFIX);
    }

    /**
     * Loads Google Maps.
     */
    private void loadPersonLocation(ReadOnlyPerson person) {
        String add = person.getAddress().toString();
        String[] address = add.split("\\s");
        String location = "";
        for (String s: address) {
            location += s;
            location += "+";
        }
        loadPage(GOOGLE_MAPS_URL + location);
    }

    public void loadPage(String url) {
        Platform.runLater(() -> browser.getEngine().load(url));
    }

    /**
     * Loads a default HTML file with a background that matches the general theme.
     */
    private void loadDefaultPage() {
        URL defaultPage = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE);
        loadPage(defaultPage.toExternalForm());
    }

    /**
     * Frees resources allocated to the browser.
     */
    public void freeResources() {
        browser = null;
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPersonPage(event.getNewSelection().person);
    }

    @Subscribe
    private void handleOauth2BrowserRequestEvent(Oauth2BrowserRequestEvent event) {
        loadPage(event.getRequestUrl());
        resetUrlListener();
        currentUrlListener = new UrlListener(event.getCommandType());
        browser.getEngine().locationProperty().addListener(currentUrlListener);
    }

    /**
     * Resets any current listeners of the URL
     */
    private void resetUrlListener() {
        if (currentUrlListener != null) {
            browser.getEngine().locationProperty().removeListener(currentUrlListener);
            currentUrlListener = null;
        }
    }

    /**
     * Implements the functional interface ChangeListener. Lambda not used, due to the need to reset the url listener
     */
    private class UrlListener implements ChangeListener {
        private String commandType;
        UrlListener(String inputCommandType) {
            commandType = inputCommandType;
        }
        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            currentUrl = (String) newValue;
            if (authSuccessUrlDetected(currentUrl)) {
                EventsCenter.getInstance().post(new GoogleAuthenticationSuccessEvent(commandType,
                        currentUrl.split("=")[1].split("&")[0]));
            }
        }
    }

//
//    /**
//     * Event listener for Google Auth Requests Events
//     * @param event
//     */
//    @Subscribe
//    private void handleGoogleAuthRequestEvent(GoogleAuthRequestEvent event) {
//        authService = event.getAuthServiceRef();
//        loadPage(authService.getAuthContactWriteUrl());
//
//        /**Listener for URL : Code adapted from https://gist.github.com/tewarid/57031d4b2f0a27765fa82abd10c21351
//         * Fires a GoogleAuthSuccessEvent when a URL change to GoogleApiAuth.redirectUrl is detected
//         */
//        browser.getEngine().locationProperty().addListener(((observable, oldValue, newValue) -> {
//            currentUrl = (String) newValue;
//            if (authSuccessUrlDetected(currentUrl)) {
//                EventsCenter.getInstance().post(new GoogleAuthSuccessEvent());
//            }
//        }));
//    }
//
////    /**
////     * Event listener for Google Auth Success Events
////     * @param event
////     */
////    @Subscribe
////    private void handleGoogleAuthSucessEvent(GoogleAuthSuccessEvent event) {
////        String authCode = currentUrl.split("=")[1].split("&")[0];
////        if (authService.setupCredentials(authCode)) {
////            EventsCenter.getInstance().post(new GoogleApiAuthServiceCredentialsSetupCompleted());
////        }
////    }

    /**
     * Checks if Authentication is successful -> current domain reflects the set redirect endpoint
     * @param currentUrl
     * @return
     */
    private boolean authSuccessUrlDetected(String currentUrl) {
        System.out.println(currentUrl);
//        return currentUrl.substring(0, Oauth2Command.getRedirectUrl().length() - 1)
//                .equals(Oauth2Command.getRedirectUrl());
        return currentUrl.contains("access_token=");
    }

    /**
     * Event listener for Find Location Events
     * @param event
     */
    @Subscribe
    private void handleFindLocationRequestEvent(FindLocationRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event,
                "Getting location of " + event.targetPerson.getName().fullName));
        loadPersonLocation(event.targetPerson);
    }
}
