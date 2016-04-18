package it.hash.osgi.content.rest;

import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import it.hash.osgi.content.service.Content;
import it.hash.osgi.content.service.ContentServiceImpl;

import static it.hash.osgi.utils.StringUtils.*;

@Path("/contents")
public class ContentResources {
	private volatile ContentServiceImpl _contentService;

	@GET
	@Path("{content:.*}")
	@Produces(MediaType.WILDCARD)
	public Response getContent(@PathParam("content") String content) throws Exception {
		Content cnt = (Content)_contentService.getContent(content, "IT").get("content");
		
		return Response.ok(new ByteArrayInputStream(Base64.decodeBase64(cnt.getContent()))).type(cnt.getType()).build();
	}
	
	@GET
	@Path("{content:.*}")
	@Produces(MediaType.WILDCARD)
	public Response getContent(@PathParam("content") String content, @QueryParam("lang") String lang) throws Exception {
		Content cnt = (Content)_contentService.getContent(content, lang).get("content");
		
		return Response.ok(new ByteArrayInputStream(Base64.decodeBase64(cnt.getContent()))).type(cnt.getType()).build();
	}
	
	@GET
	@Path("{content:.*}")
	@Produces(MediaType.WILDCARD)
	public Response getContent(@PathParam("content") String content, @QueryParam("lang") String lang, @QueryParam("type") String type) throws Exception {
		Content cnt = (Content)_contentService.getContent(content, lang, type).get("content");
		
		return Response.ok(new ByteArrayInputStream(Base64.decodeBase64(cnt.getContent()))).type(cnt.getType()).build();
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> setContent(
			@QueryParam("name") String name,
			@QueryParam("lang") String lang,
			@QueryParam("type") String type,
			@QueryParam("content") String content) throws Exception {
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
			}
			else
				result.put("returnCode", "000");
		}
		else
			result = _contentService.setContent(name, lang, type, Base64.encodeBase64(content.getBytes(Charset.forName("UTF-8"))));

		return result;
	}
}