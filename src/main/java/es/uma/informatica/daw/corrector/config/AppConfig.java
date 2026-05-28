package es.uma.informatica.daw.corrector.config;

import java.net.http.HttpClient;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .requestFactory(crearInsecureRequest())
                .build();
    }

    private ClientHttpRequestFactory crearInsecureRequest() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpClient httpClient = HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .build();

            return new JdkClientHttpRequestFactory(httpClient);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new JdkClientHttpRequestFactory();
        }
    }
}
