package ii.browser.utf8;

import java.io.IOException;

import javax.microedition.io.HttpConnection;

import net.rim.device.api.browser.field.BrowserContent;
import net.rim.device.api.browser.field.Event;
import net.rim.device.api.browser.field.RedirectEvent;
import net.rim.device.api.browser.field.RenderingApplication;
import net.rim.device.api.browser.field.RenderingException;
import net.rim.device.api.browser.field.RenderingOptions;
import net.rim.device.api.browser.field.RenderingSession;
import net.rim.device.api.browser.field.RequestedResource;
import net.rim.device.api.io.http.HttpHeaders;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.component.Status;
import net.rim.device.api.ui.container.MainScreen;

public class MBrowserFieldDemo implements RenderingApplication {
    private String data;
    private static final String REFERER = "referer";
    private RenderingSession renderingSession;
    private HttpConnection currentConnection;
    private Field displayableField;
    private MainScreen mainScreen;

    public MBrowserFieldDemo(String data, MainScreen mainScreen) {
        this.data = data;
        this.mainScreen = mainScreen;
      
        renderingSession = RenderingSession.getNewInstance();
        renderingSession.getRenderingOptions().setProperty(RenderingOptions.CORE_OPTIONS_GUID, RenderingOptions.JAVASCRIPT_ENABLED, true);
        ResourceFetchThread thread = new ResourceFetchThread(data, null, this);
        thread.start();
    }

    public void processConnection(HttpConnection connection, Event e) {
        // cancel previous request
        if (currentConnection != null) {
            try {
                currentConnection.close();
            } catch (IOException e1) {
            }
        }

        currentConnection = connection;
        BrowserContent browserContent = null;

        try {
            browserContent = renderingSession.getBrowserContent(connection, this, e);

            if (browserContent != null) {
                Field field = browserContent.getDisplayableContent();
                if (field != null) {
                    synchronized (Application.getEventLock()) {
                        this.mainScreen.add(field);
                    }
                }
                browserContent.finishLoading();
                System.out.println("Done loading>>>>>");
            }

        } catch (RenderingException re) {
            System.out.println("RenderingException : " + re);
        } catch (Exception ex) {
            System.out.println("Exception : " + ex);
            ex.printStackTrace();
        }
    }

    public Object eventOccurred(Event event) {
        int eventId = event.getUID();

        switch (eventId) {

            case Event.EVENT_URL_REQUESTED: {
                ResourceFetchThread thread = new ResourceFetchThread(data, null, this);
                thread.start();
                break;
            }
            case Event.EVENT_BROWSER_CONTENT_CHANGED: {
                break;
            }
            case Event.EVENT_REDIRECT: {

                RedirectEvent e = (RedirectEvent) event;
                String referrer = e.getSourceURL();

                switch (e.getType()) {

                    case RedirectEvent.TYPE_SINGLE_FRAME_REDIRECT:
                        // show redirect message
                        Application.getApplication().invokeAndWait(new Runnable() {
                            public void run() {
                                Status.show("You are being redirected to a different page...");
                            }
                        });

                        break;
                }

                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setProperty(REFERER, referrer);
                ResourceFetchThread thread = new ResourceFetchThread(this.data, event, this);
                thread.start();
                break;
            }
            case Event.EVENT_CLOSE:				//no close the app support
            case Event.EVENT_SET_HEADER:        // no cache support
            case Event.EVENT_SET_HTTP_COOKIE:   // no cookie support
            case Event.EVENT_HISTORY:           // no history support
            case Event.EVENT_EXECUTING_SCRIPT:  // no progress bar is supported
            case Event.EVENT_FULL_WINDOW:       // no full window support
            case Event.EVENT_STOP:              // no stop loading support
            default:
        }
        return null;
    }

    public int getAvailableHeight(BrowserContent browserField) {
        // field has full screen
        return Display.getHeight();
    }

    public int getAvailableWidth(BrowserContent browserField) {
        // field has full screen
        return Display.getWidth();
    }

    public int getHistoryPosition(BrowserContent browserField) {
        // no history support
        return 0;
    }

    public String getHTTPCookie(String url) {
        // no cookie support
        return null;
    }

    public HttpConnection getResource(RequestedResource resource, BrowserContent referrer) {

        if (resource == null) {
            return null;
        }

        // check if this is cache-only request
        if (resource.isCacheOnly()) {
            // no cache support
            return null;
        }

        String url = resource.getUrl();

        if (url == null) {
            return null;
        }

        // if referrer is null we must return the connection
        if (referrer == null) {
            HttpConnection connection = new MyHttpConnection(url);
            return connection;

        }

        return null;
    }


    public void invokeRunnable(Runnable runnable) {
        (new Thread(runnable)).run();
    }

    public Field getDisplayableField() {
        if (displayableField == null)
            displayableField = new NullField();
        return displayableField;
    }
}

class ResourceFetchThread extends Thread {

    private MBrowserFieldDemo application;
    private Event event;
    private String data;

    ResourceFetchThread(String data, Event event, MBrowserFieldDemo application) {
        this.data = data;
        this.application = application;
        this.event = event;
    }

    public void run() {
        HttpConnection connection = new MyHttpConnection(this.data);
        this.application.processConnection(connection, this.event);
    }
}
