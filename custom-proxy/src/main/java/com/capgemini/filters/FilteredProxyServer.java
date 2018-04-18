package com.capgemini.filters;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;

import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSource;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/*
 * @Sirisha
 * Custom Proxy Implementation
 * Default Server Bootstrap 
 */
 
public class FilteredProxyServer {

	final int PORT;
	private HttpProxyServer server =null;
	final String SERVICE_HOST;
	final int SERVICE_HOST_PORT;
	public FilteredProxyServer() {
		
		Properties prop = new Properties();

		try {
		    prop.load(FilteredProxyServer.class.getResourceAsStream("/application.properties"));   

		} catch (IOException ex) {
		    ex.printStackTrace();
		}
		 String serverPort = prop.getProperty("server.port");
		 SERVICE_HOST= prop.getProperty("servicehost");
		 SERVICE_HOST_PORT=Integer.parseInt(prop.getProperty("servicehost.port"));
	
	   PORT = Integer.parseInt(serverPort);
	}

	public void startProxyServer() {
		HttpFiltersSource filtersSource = getFiltersSource();
		
		server=DefaultHttpProxyServer
				.bootstrap()
				.withPort(PORT)
				.withAllowRequestToOriginServer(true)
				.withFiltersSource(filtersSource)
				.withAllowLocalOnly(true)
				.withName("FilteringProxy")
				.start();
	}
	public void stopServer() {
		if(server!=null) {
			server.stop();
			server=null;
		}
	}
	
	public static void main(String args[]){
		FilteredProxyServer fps = new FilteredProxyServer();
		fps.startProxyServer();
	}
	
	/*
	 * HttpFilterscource invocation - HTTP Request Life cycle
	 */

	private HttpFiltersSource getFiltersSource() {
        return new HttpFiltersSourceAdapter(){

        	 @Override
             public int getMaximumRequestBufferSizeInBytes() {
                 return 0;//1024 * 1024;
             }
            public HttpFilters filterRequest(HttpRequest originalRequest) {
            	
                return new HttpFiltersAdapter(originalRequest){
					public void serverToProxyResponseTimedOut() {
						System.out.println("serverToProxyResponseTimedOut");
					}

					public void serverToProxyResponseReceiving() {
						System.out.println("serverToProxyResponseReceiving");
					}

					public void serverToProxyResponseReceived() {
						System.out.println("serverToProxyResponseReceived");
					}
					
					/*
					 * ServertoProxy response
					 */

					public HttpObject serverToProxyResponse(HttpObject httpObject) {
						System.out.println("serverToProxyResponse");
						return httpObject;
					}

					public void proxyToServerResolutionSucceeded(String serverHostAndPort,InetSocketAddress resolvedRemoteAddress) {
						System.out.println("proxyToServerResolutionSucceeded");
					}
                    /*
					 * DNS Resolution with Destination host and port
					 */
					public InetSocketAddress proxyToServerResolutionStarted(String resolvingServerHostAndPort) {
						System.out.println("proxyToServerResolutionStarted");
						return new InetSocketAddress(SERVICE_HOST, SERVICE_HOST_PORT);
						
					}

					public void proxyToServerResolutionFailed(String hostAndPort) {
						System.out.println("proxyToServerResolutionFailed");
					}

					public void proxyToServerRequestSent() {
						System.out.println("proxyToServerRequestSent");
					}

					public void proxyToServerRequestSending() {
						System.out.println("proxyToServerRequestSending");
					}

										
					public void proxyToServerConnectionSucceeded(ChannelHandlerContext serverCtx) {
						System.out.println("proxyToServerConnectionSucceeded");
					}

					public void proxyToServerConnectionStarted() {
						System.out.println("proxyToServerConnectionStarted");
					}

					public void proxyToServerConnectionSSLHandshakeStarted() {
						System.out.println("proxyToServerConnectionSSLHandshakeStarted");
					}

					public void proxyToServerConnectionQueued() {
						System.out.println("proxyToServerConnectionQueued");
					}

					public void proxyToServerConnectionFailed() {
						System.out.println("proxyToServerConnectionFailed");
					}

					/*
					 * ProxytoClientResponse
					 */	
					public HttpObject proxyToClientResponse(HttpObject httpObject) {
						System.out.println("proxyToClientResponse");
						return httpObject;
					}
					/*
					 * ProxyToServer Request
					 */	
					public HttpResponse proxyToServerRequest(HttpObject httpObject) {
						System.out.println("proxyToServerRequest");
						if(httpObject instanceof HttpRequest) {
                        	HttpHeaders headers=((HttpRequest) httpObject).headers();
                        	String contentType=headers.get("Content-Type");
							if(contentType==null || !contentType.contains("xml")){
								HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED, Unpooled.copiedBuffer(
										 "Invalid Content Type".toString(), CharsetUtil.UTF_8));
								response.headers().set("Connection", "close");
								return response;
							}else{
								return null;
							}
                        }
						return null;
					}
				};
			}
		};
	}
}