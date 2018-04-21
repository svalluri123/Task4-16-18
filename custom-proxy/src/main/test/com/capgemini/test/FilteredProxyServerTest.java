package com.capgemini.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.Rule;
import org.junit.Test;

import com.capgemini.filters.FilteredProxyServer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
public class FilteredProxyServerTest {
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(9081);
	
	
	@Test
	public void instantiateFilteredProxyServer(){
		FilteredProxyServer server = new FilteredProxyServer();
		assertEquals(8100,server.PORT);
		assertEquals("localhost", server.SERVICE_HOST);
		assertEquals(9081, server.SERVICE_HOST_PORT);
	}
	@Test
	public void instantiateStartProxyServer(){
		FilteredProxyServer server = new FilteredProxyServer();
		server.startProxyServer();
		String strUrl = "http://localhost:8100/testapp";	
		HttpURLConnection urlConn = null;
		 try {
		        URL url = new URL(strUrl);
		        urlConn = (HttpURLConnection) url.openConnection();
		        urlConn.connect();
		        assertEquals(HttpURLConnection.HTTP_UNAUTHORIZED, urlConn.getResponseCode());
		        assertEquals("Unauthorized", urlConn.getResponseMessage());
		    } catch (IOException e) {
		    	
		       fail();
		    }finally {
		    	server.stopServer();
		    }
	  }
	
	@Test
	public void instantiateStartProxyXMLTestServer(){
		FilteredProxyServer server = new FilteredProxyServer();
		server.startProxyServer();
		String strUrl = "http://localhost:8100/testapp";	
		stubFor(get(urlPathEqualTo("/testapp"))
				  .willReturn(aResponse()
				  .withStatus(200)
				  .withHeader("Content-Type", "application/xml")
				  .withBody("<hello/>")));

		HttpURLConnection urlConn = null;
		 try {
		        URL url = new URL(strUrl);
		        urlConn = (HttpURLConnection) url.openConnection();
		        urlConn.setRequestProperty("Content-Type", "application/xml");
		        urlConn.connect();
		        System.out.println(urlConn.getResponseCode());
		        System.out.println(urlConn.getResponseMessage());
		        String response =readFullyAsString(urlConn.getInputStream(),"UTF-8");
		        System.out.println(response);
		        assertEquals(HttpURLConnection.HTTP_OK, urlConn.getResponseCode());
		        assertEquals("<hello/>", response);
		        
		    } catch (IOException e) {
		    	
		       fail();
		    }finally {
		    	server.stopServer();
		    }
	  }
	
	public String readFullyAsString(InputStream inputStream, String encoding) throws IOException {
        return readFully(inputStream).toString(encoding);
    }

    private ByteArrayOutputStream readFully(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos;
    }
}
