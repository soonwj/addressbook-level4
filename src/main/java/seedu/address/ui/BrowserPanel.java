package seedu.address.ui;

import java.net.URL;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.logic.GoogleApiAuthServiceCredentialsSetupCompleted;
import seedu.address.commons.events.logic.GoogleAuthRequestEvent;
import seedu.address.commons.events.logic.GoogleAuthSuccessEvent;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.commons.auth.GoogleApiAuth;
import seedu.address.commons.core.EventsCenter;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    public static final String DEFAULT_PAGE = "default.html";
    public static final String GOOGLE_SEARCH_URL_PREFIX = "https://www.google.com.sg/search?safe=off&q=";
    public static final String GOOGLE_SEARCH_URL_SUFFIX = "&cad=h";

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private WebView browser;

    private String currentUrl = "";
    private GoogleApiAuth authService;

    public BrowserPanel() {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        loadDefaultPage();
        registerAsAnEventHandler(this);

        /**Listener for URL : Code adapted from https://gist.github.com/tewarid/57031d4b2f0a27765fa82abd10c21351
         * Fires a GoogleAuthSuccessEvent when a URL change to GoogleApiAuth.redirectUrl is detected
         */
        browser.getEngine().locationProperty().addListener(((observable, oldValue, newValue) -> {
            currentUrl = (String) newValue;
            System.out.println("Browser Panel Redirected to: " + currentUrl);
            if (authSuccessUrlDetected(currentUrl)){
                EventsCenter.getInstance().post(new GoogleAuthSuccessEvent());
            }
        }));
    }


    private void loadPersonPage(ReadOnlyPerson person) {
        loadPage(GOOGLE_SEARCH_URL_PREFIX + person.getName().fullName.replaceAll(" ", "+")
                + GOOGLE_SEARCH_URL_SUFFIX);
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

    /**
     * Event listener for Google Auth Requests Events
     * @param event
     */
    @Subscribe
    private void handleGoogleAuthRequestEvent(GoogleAuthRequestEvent event) {
        authService = event.getAuthServiceRef();
        loadPage(authService.getAuthContactWriteUrl());
    }

    /**
     * Event listener for Google Auth Success Events
     * @param event
     */
    @Subscribe
    private void handleGoogleAuthSucessEvent(GoogleAuthSuccessEvent event) {
        String authCode = currentUrl.split("=")[1].split("&")[0];
        if (authService.setupCredentials(authCode)){
            EventsCenter.getInstance().post(new GoogleApiAuthServiceCredentialsSetupCompleted());
        }
    }

    private boolean authSuccessUrlDetected(String currentUrl) {
        return currentUrl.contains(GoogleApiAuth.redirectUrl);
    }
}
