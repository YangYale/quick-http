package com.ipet.http.executor;

import com.ipet.http.enums.BodyType;
import com.ipet.http.exception.RequestException;
import com.ipet.http.response.ResponseParserFactory;
import com.ipet.http.utils.HttpUri;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultBHttpClientConnectionFactory;
import org.apache.http.impl.nio.DefaultHttpClientIODispatch;
import org.apache.http.impl.nio.pool.BasicNIOConnFactory;
import org.apache.http.impl.nio.pool.BasicNIOConnPool;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.nio.protocol.BasicAsyncRequestProducer;
import org.apache.http.nio.protocol.BasicAsyncResponseConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestExecutor;
import org.apache.http.nio.protocol.HttpAsyncRequester;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.nio.reactor.ssl.SSLSetupHandler;
import org.apache.http.protocol.*;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.File;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/6 16:58
 */
public class HttpRequestsExecutor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static HttpRequestsExecutor httpRequestsExecutor = new HttpRequestsExecutor();

    private static final int DEFAULT_MAX_PER_ROUTE = 1;
    private static final int DEFAULT_MAX_TOTAL = 1;

    private HttpRequestsExecutor() {
    }

    private Socket createSocket(HttpUri httpUri,String certification) throws Exception{
        if(HttpUri.SCHEMA_HTTPS.equalsIgnoreCase(httpUri.getSchema())){
            //是ssl请求
            SSLContext sslContext = StringUtils.isNotBlank(certification) ? SSLContexts.custom().loadTrustMaterial(new File(certification)).build() :
                    SSLContexts.createSystemDefault();
            SSLSocket sslSocket = (SSLSocket) sslContext.getSocketFactory().createSocket(httpUri.getHost(),httpUri.getPort());
            sslSocket.setEnabledProtocols(new String[]{
                    "TLSv1","TLSv1.1","TLSv1.2"
            });
            /*sslSocket.setEnabledCipherSuites(new String[]{
                    "TLS_DHE_RSA_WITH_AES_256_CBC_SHA",
                    "TLS_DHE_DSS_WITH_AES_256_CBC_SHA"
            });*/
            return sslSocket;
        }else{
            return new Socket(httpUri.getHost(),httpUri.getPort());
        }
    }

    private BasicNIOConnPool createConnPool(HttpUri httpUri,String certification,ConnectionConfig connectionConfig,
                                            final ConnectingIOReactor connectingIOReactor) throws Exception{
        final HttpAsyncRequestExecutor httpAsyncRequestExecutor = new HttpAsyncRequestExecutor();
        final IOEventDispatch eventDispatch = new DefaultHttpClientIODispatch(httpAsyncRequestExecutor,connectionConfig);
        Executors.newSingleThreadExecutor().submit(()->{
            try{
                connectingIOReactor.execute(eventDispatch);
            }catch (Exception e){
                logger.error("asyncDoRequest IOReactor Error.",e);
            }
        });

        if(HttpUri.SCHEMA_HTTPS.equalsIgnoreCase(httpUri.getSchema())){
            SSLContext sslContext = StringUtils.isNotBlank(certification) ? SSLContexts.custom().loadTrustMaterial(new File(certification)).build() :
                    SSLContexts.createSystemDefault();
            BasicNIOConnFactory basicNIOConnFactory = new BasicNIOConnFactory(sslContext, new SSLSetupHandler() {
                @Override
                public void initalize(SSLEngine sslengine) throws SSLException {
                    sslengine.setEnabledProtocols(new String[]{
                            "TLSv1","TLSv1.1","TLSv1.2"
                    });
                    /*sslengine.setEnabledCipherSuites(new String[]{
                            "TLS_DHE_RSA_WITH_AES_256_CBC_SHA",
                            "TLS_DHE_DSS_WITH_AES_256_CBC_SHA"
                    });*/
                }

                @Override
                public void verify(IOSession iosession, SSLSession sslsession) throws SSLException {
                    //X509Certificate[] certificates = sslsession.getPeerCertificateChain();
                    //logger.info("Certificates : {}",Arrays.toString(certificates));
                }
            },connectionConfig);
            final BasicNIOConnPool basicNIOConnPool = new BasicNIOConnPool(connectingIOReactor,basicNIOConnFactory,0);
            basicNIOConnPool.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
            basicNIOConnPool.setMaxTotal(DEFAULT_MAX_TOTAL);
            return basicNIOConnPool;
        }else{
            final BasicNIOConnPool basicNIOConnPool = new BasicNIOConnPool(connectingIOReactor,connectionConfig);
            basicNIOConnPool.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
            basicNIOConnPool.setMaxTotal(DEFAULT_MAX_TOTAL);
            return basicNIOConnPool;
        }
    }

    /**
     * 同步http请求方式（暂时先基于BI/O）
     * @param request 请求对象，请提前封装好请求体
     * @param httpUri 请求地址目标
     * @param certification 证书（ssl请求时传递有效）
     * @param connectionConfig 请求技术性配置（缓存区大小，字符集等）
     * @param resultType 请求返回对象类型（反序列化使用）
     * @param <R> 请求返回对象泛型类型
     * @param <E> 请求体对象泛型类型
     * @return R
     * @throws Exception
     */
    public <R, E extends BasicHttpRequest> R doRequest(E request,
                                                        HttpUri httpUri,
                                                        String certification,
                                                        ConnectionConfig connectionConfig,
                                                        Class<?> resultType) throws Exception {
        final HttpProcessor httpProcessor = HttpProcessorBuilder.create().add(new RequestContent())
                .add(new RequestTargetHost())
                .add(new RequestConnControl())
                .add(new RequestExpectContinue(true)).build();
        final HttpRequestExecutor httpRequestExecutor = new HttpRequestExecutor();
        HttpCoreContext httpCoreContext = HttpCoreContext.create();
        httpCoreContext.setTargetHost(new HttpHost(httpUri.getHost(),httpUri.getPort(),httpUri.getSchema()));
        try(DefaultBHttpClientConnection defaultBHttpClientConnection = new DefaultBHttpClientConnectionFactory(connectionConfig).
                createConnection(createSocket(httpUri,certification))){
            logger.info(" >> Request Uri: {}",httpUri);
            httpRequestExecutor.preProcess(request,httpProcessor,httpCoreContext);
            HttpResponse httpResponse = httpRequestExecutor.execute(request,defaultBHttpClientConnection,httpCoreContext);
            httpRequestExecutor.postProcess(httpResponse,httpProcessor,httpCoreContext);
            logger.info(" << Response: {}",httpResponse.getStatusLine());
            if(httpResponse.getStatusLine().getStatusCode() >= 200 && httpResponse.getStatusLine().getStatusCode() < 300){
                return (R) ResponseParserFactory.Builder.INSTANCE.build().
                        create(BodyType.toBodyType(httpResponse.getEntity().getContentType().getValue())).
                        parse(httpResponse.getEntity(),connectionConfig.getCharset(),resultType);
            }else{
                throw new RequestException(httpResponse.getStatusLine().getStatusCode());
            }
        }
    }

    /**
     * 异步http请求方式（基于NI/O）
     * @param request 请求对象，请提前封装好请求体
     * @param httpUri 请求地址目标
     * @param certification 证书（ssl请求时传递有效）
     * @param connectionConfig 请求技术性配置（缓存区大小，字符集等）
     * @param resultType 请求返回对象类型（反序列化使用）
     * @param <R> 请求返回对象泛型类型
     * @param <E> 请求体对象泛型类型
     * @return Future<R>
     *  @see Future
     * @throws Exception
     */
    public <R, E extends BasicHttpRequest> Future<R> asyncDoRequest(E request,
                                                                     HttpUri httpUri,
                                                                     String certification,
                                                                     ConnectionConfig connectionConfig,
                                                                     Class<?> resultType) throws Exception{
        final HttpProcessor httpProcessor = HttpProcessorBuilder.create().add(new RequestContent())
                .add(new RequestTargetHost())
                .add(new RequestConnControl())
                .add(new RequestExpectContinue(true)).build();

        final ConnectingIOReactor connectingIOReactor = new DefaultConnectingIOReactor();
        final BasicNIOConnPool basicNIOConnPool = createConnPool(httpUri,certification,connectionConfig,connectingIOReactor);

        final HttpAsyncRequester requester = new HttpAsyncRequester(httpProcessor);
        HttpCoreContext httpCoreContext = HttpCoreContext.create();

        return Executors.newSingleThreadExecutor().submit(() -> {
            Future<HttpResponse> httpResponseFuture = requester.execute(new BasicAsyncRequestProducer(new HttpHost(httpUri.getHost(), httpUri.getPort(), httpUri.getSchema()), request),
                    new BasicAsyncResponseConsumer(),
                    basicNIOConnPool,
                    httpCoreContext,
                    new FutureCallback<HttpResponse>() {
                        @Override
                        public void completed(HttpResponse result) {
                            logger.info("Target [{}] -> {}",httpUri,result.getStatusLine());
                        }

                        @Override
                        public void failed(Exception ex) {
                            logger.error("Target [" + httpUri + "] -> " + ex);
                        }

                        @Override
                        public void cancelled() {
                            logger.warn("Target [" + httpUri + "] -> cancelled");
                        }
                    }
            );
            HttpResponse httpResponse = httpResponseFuture.get();
            logger.info("Shutting down I/O reactor.");
            connectingIOReactor.shutdown();
            if(httpResponse.getStatusLine().getStatusCode() >= 200 && httpResponse.getStatusLine().getStatusCode() < 300){
                return (R) ResponseParserFactory.Builder.INSTANCE.build().
                        create(BodyType.toBodyType(httpResponse.getEntity().getContentType().getValue())).
                        parse(httpResponse.getEntity(),connectionConfig.getCharset(),resultType);
            }else{
                throw new RequestException(httpResponse.getStatusLine().getStatusCode());
            }
        });
    }

    public enum ExcetorBuilder {
        INSTANCE;
        public HttpRequestsExecutor create() {
            return httpRequestsExecutor;
        }
    }

}
