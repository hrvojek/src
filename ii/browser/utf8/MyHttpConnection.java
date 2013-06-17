package ii.browser.utf8;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.HttpConnection;

public class MyHttpConnection implements HttpConnection {
    private long streamLength = 8000; //I added this
    private DataInputStream dataInput;
    private InputStream in;
    private String encoding = "text/html";//providing space between "/" so that i can post this message.
    
    public MyHttpConnection (String htmlString) {
    	try {
    		in = new ByteArrayInputStream(htmlString.getBytes("UTF-8"));
    		dataInput = new DataInputStream(in);
    	}
    	catch (Exception e) {
    		System.out.println("HttpConnectionImpl : Exception : " + e);
    	}
    }
    
    public String getURL() {
    return "";
    }

	public String getProtocol() {
	    return "";
	}
	
	public String getHost() {
	    return "";
	}
	
	public String getFile() {
	    return "";
	}
	
	public String getRef() {
	    return "";
	}
	
	public String getQuery() {
	    return "";
	}
	
	public int getPort() {
	    return 0;
	}
	
	public String getRequestMethod() {
	    return "";
	}
	
	public void setRequestMethod(String s) throws IOException {
	
	}
	
	public String getRequestProperty(String s) {
	    return "";
	}
	
	public void setRequestProperty(String s, String s1) throws IOException {
	
	}
	
	public int getResponseCode() throws IOException {
	    return 200;
	}
	
	public String getResponseMessage() throws IOException {
	    return "";
	}
	
	public long getExpiration() throws IOException {
	    return 0;
	}
	
	public long getDate() throws IOException {
	    return 0;
	}
	
	public long getLastModified() throws IOException {
	    return 0;
	}
	
	public String getHeaderField(String s) throws IOException {
		String result;
		if(s.equalsIgnoreCase("content-type")) {
			result = getType();
        }
		else if(s.equalsIgnoreCase("content-length")) {
			result = Long.toString(getLength());
		}
		else if(s.equalsIgnoreCase("content-location")) {
			result = getURL();
		}
		else if(s.equalsIgnoreCase("accept-charset")) {
			result = "UTF-8";
		}
        else {
        	result = null;
        }
		
		return result;
	}
	
	public int getHeaderFieldInt(String s, int i) throws IOException {
	    return 0;
	}
	
	public long getHeaderFieldDate(String s, long l) throws IOException {
	    return 0;
	}
	
	public String getHeaderField(int i) throws IOException {
	    return "";
	}
	
	public String getHeaderFieldKey(int i) throws IOException {
	    return "";
	}
	
	public String getType() {
	    return "text/html";
	}
	
	public String getEncoding() {
	    return encoding;
	}
	
	public long getLength() {
	    return streamLength;
	}
	
	public InputStream openInputStream() throws IOException {
	    return in;
	}
	
	public DataInputStream openDataInputStream() throws IOException {
	    return dataInput;
	}
	
	public void close() throws IOException {
	
	}
	
	public OutputStream openOutputStream() throws IOException {
	    return new ByteArrayOutputStream();
	}
	
	public DataOutputStream openDataOutputStream() throws IOException {
	    return new DataOutputStream(new ByteArrayOutputStream());
	}
}
