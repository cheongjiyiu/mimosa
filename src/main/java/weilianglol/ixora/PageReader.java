package weilianglol.ixora;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import lombok.Getter;

public class PageReader {

	private @Getter Document document;

	public PageReader(String url) throws IOException {
		Resource html = new ClassPathResource(url);
		this.document = Jsoup.parse(html.getInputStream(), null, "");
	}
	
	public boolean hasElement(String attr, String val) {
		return document.getElementsByAttributeValue(attr, val).size() > 0;
	}
	
	public Element getElement(String attr, String val) {
		return document.getElementsByAttributeValue(attr, val).get(0);
	}
	
	public boolean hasFragment(String val) {
		return hasElement("th:fragment", val);
	}
	
	public Element getFragment(String val) {
		return getElement("th:fragment", val);
	}

}
