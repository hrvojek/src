package ii.browser.utf8;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.component.ActiveRichTextField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.MainScreen;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class BrowserScreen extends MainScreen {
	private MainScreen mainScreen;

    public BrowserScreen() {

    	LabelField title = new LabelField("Hello World Demo" , LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH);
    	setTitle(title);
    	add(new RichTextField("Hello World!" ,Field.NON_FOCUSABLE));
    	add(new ActiveRichTextField("<p>abcd</p><p>Èistoæa je pola zdravlja!</p>"));
    	String test = "<p>kako, kako, kako sada? Èesto to èujemo od životinja</p>";
    
    	mainScreen = new MainScreen(); 
    	try {
    		new MBrowserFieldDemo(test, mainScreen);  //passing the html string and a Mainscreen
    	 
    	}catch (Exception e)
    	{
    		System.out.println("Exception rendering "+e.getMessage());
    	}   
    }
}
