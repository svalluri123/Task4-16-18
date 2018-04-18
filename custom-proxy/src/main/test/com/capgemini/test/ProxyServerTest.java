package com.capgemini.test;

import static org.junit.Assert.*;

import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.littleshoot.proxy.HttpFiltersSource;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.mockserver.integration.ClientAndServer;

import com.capgemini.filters.FilteredProxyServer;

public class ProxyServerTest {
	private Server webServer;
    private HttpProxyServer proxyServer;
    private int webServerPort;

    private ClientAndServer mockServer;
    private int mockServerPort;


	   @Before
	    public void setUp() throws Exception {
	        webServer = new Server(0);
	        webServer.start();
	        webServerPort = 8100;

	        mockServer = new ClientAndServer(0);
	        mockServerPort = mockServer.getPort();

	    }

	@After
	public void tearDown() throws Exception {
        try {
            if (webServer != null) {
                webServer.stop();
            }
        } finally {
            try {
                if (proxyServer != null) {
                    proxyServer.abort();
                }
            } finally {
                if (mockServer != null) {
                    mockServer.stop();
                }
            }
        }
    }

	@Test
	public void contentTypetest() {
		
	}

}
