package com.capgemini.filters;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSource;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

/*
 * Comments
 */
public class FilteredProxyServer {


	public static void main(String[] args) {
		
		Properties prop = new Properties();

		try {
		    prop.load(FilteredProxyServer.class.getResourceAsStream("/application.properties"));   

		} catch (IOException ex) {
		    ex.printStackTrace();
		}
		 String serverPort = prop.getProperty("server.port");
	   
	
	   final int PORT = Integer.parseInt(serverPort);
	   
		HttpFiltersSource filtersSource = getFiltersSource();
		
		DefaultHttpProxyServer
				.bootstrap()
				.withPort(PORT)
				.withAllowRequestToOriginServer(true)
				.withFiltersSource(filtersSource)
				.withAllowLocalOnly(true)
				.withName("FilteringProxy")
				.start();
	}

	/*
	 * Comments
	 */

	private static HttpFiltersSource getFiltersSource() {
        return new HttpFiltersSourceAdapter(){

        	 @Override
             public int getMaximumRequestBufferSizeInBytes() {
                 return 1024 * 1024;
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

					public HttpObject serverToProxyResponse(HttpObject httpObject) {
						System.out.println("serverToProxyResponse");
						return httpObject;
					}

					public void proxyToServerResolutionSucceeded(String serverHostAndPort,InetSocketAddress resolvedRemoteAddress) {
						System.out.println("proxyToServerResolutionSucceeded");
					}

					public InetSocketAddress proxyToServerResolutionStarted(String resolvingServerHostAndPort) {
						System.out.println("proxyToServerResolutionStarted");
						return new InetSocketAddress("localhost", 9081);
						
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

					
					public HttpObject proxyToClientResponse(HttpObject httpObject) {
						System.out.println("proxyToClientResponse");
						return httpObject;
					}
					public HttpResponse proxyToServerRequest(HttpObject httpObject) {
						System.out.println("proxyToServerRequest");
//						 if (httpObject instanceof FullHttpRequest) {
//                            try {
//
//                            	FullHttpRequest res=(FullHttpRequest) httpObject;
//    							ByteBuf content=res.content();
//    							byte[] bytes = new byte[content.readableBytes()];
//    							content.readBytes(bytes);
//    							HttpHeaders headers=res.headers();
//    							System.out.println("Request Data: \n"+new String(bytes));
//    							
//    							String contentType=headers.get("Content-Type");
//    							if(contentType==null || !contentType.contains("xml")){
//    								HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED);
//    								return response;
//    							}else{
//    								return null;
//    							}
//							} 
//                            catch (Exception e) {
//								e.printStackTrace();
//							}
//                        }
						return null;
					}


				};
			}
		};
	}
}
