package it.hash.osgi.content.shell;

import static it.hash.osgi.utils.StringUtils.isNotEON;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import it.hash.osgi.content.service.ContentService;

public class Commands {
	private volatile ContentService _contentService;

	public void create(String name, String lang, String type, String content) throws Exception {
		Map<String, Object> result = new TreeMap<String, Object>();
		
		// GET content from HTTP connection
		if(content.startsWith("http")) {
	        URL url = new URL(content);
	        
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
	
				// Replace name
				if(name.endsWith("${content.filename}"))
					if(isNotEON(url.getPath())) {
						String[] splitted = url.getPath().split("/");
						name = name.replace("${content.filename}",splitted[splitted.length-1]);
					}
					else
						name = name.replace("${content.filename}","");

				// Get MIME-TYPE
				Map<String, List<String>> map = httpURLConnection.getHeaderFields();
				List<String> content_type = map.get("Content-Type");
		        byte[] bytes = IOUtils.toByteArray(url.openConnection().getInputStream());

		        // PUT Base64 encoded content
		        result = _contentService.setContent(name, lang, content_type.get(0), Base64.encodeBase64(bytes));
				int status = (Integer) result.get("returnCode");
				System.out.println("called shell command 'createContent': "+content+" - return-code: "+status);
			}
			else
				result.put("returnCode", "KO");
		}
		else
			result = _contentService.setContent(name, lang, type, Base64.encodeBase64(content.getBytes(Charset.forName("UTF-8"))));

		
		int status = (Integer) result.get("returnCode");
		System.out.println("called shell command 'createContent': "+content+" - return-code: "+status);
	}

}
