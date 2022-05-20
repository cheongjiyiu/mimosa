package weilianglol.ixora;

import java.io.IOException;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SilentJavaScriptErrorListener;

import lombok.Getter;
import lombok.Setter;

public class PageParser {
	
	private @Getter @Setter String url;
	private @Getter @Setter HtmlPage page;
	private @Getter @Setter boolean exceptions;
	
	private CollectingAlertHandler alertHandler;
	
	public PageParser(String html) {
		this("file://", html, false);
	}
	
	public PageParser(String url, String html, boolean exceptions) {
		this.alertHandler = new CollectingAlertHandler();
		this.url = url;
		this.exceptions = exceptions;
		
		parse(html);
	}
	
	private void parse(String html) {
		try (WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
			webClient.setAlertHandler(alertHandler);
			webClient.getOptions().setDownloadImages(true);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(exceptions);
			webClient.getOptions().setThrowExceptionOnScriptError(exceptions);
			
			if (!exceptions)
				webClient.setJavaScriptErrorListener(new SilentJavaScriptErrorListener());
			
			page = webClient.loadHtmlCodeIntoCurrentWindow(html);
			page.initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ScriptResult run(String javascript) {
		return page.executeJavaScript(javascript);
	}
	
	public void update() {
		parse(page.asXml());
	}
	
	public List<String> getAlerts() {
		return alertHandler.getCollectedAlerts();
	}

}
