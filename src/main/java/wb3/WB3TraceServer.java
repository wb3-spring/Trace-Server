package wb3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import zipkin.server.EnableZipkinServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;

@SpringBootApplication
@EnableZipkinServer
@EnableDiscoveryClient
public class WB3TraceServer {

    public static void main(String[] args) {
        SpringApplication.run(WB3TraceServer.class, args);
    }

    @Value("${server.port}")
    String port;

    @Bean
    /**
     * Dynamically assign the hosts IP address to this instances of the Eureka Client
     *
     * @param  inetUtils  Java Networks Utilities
     * @return	An instance of a EurekaInstanceConfigBean with dynamically configured IP address
     */
    public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils){
        EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils);

        String ip = null;
        String urlPattern = "http://{0}:{1}{2}";

        try {
            // Get the IP address of the current host using Java Network Utils
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        config.setIpAddress(ip);
        config.setPreferIpAddress(true);

        // Build the Status Page URL specific to this instances IP Address
        String statusPageUrl = MessageFormat.format(urlPattern,
                config.getIpAddress(),
                port,
                config.getStatusPageUrlPath());
        config.setStatusPageUrl(statusPageUrl);

        // Build the Health Page URL specific to this instances IP Address
        String healthPageUrl = MessageFormat.format(urlPattern,
                config.getIpAddress(),
                port,
                config.getHealthCheckUrlPath());
        config.setHealthCheckUrl(healthPageUrl);

        return config;
    }
}
