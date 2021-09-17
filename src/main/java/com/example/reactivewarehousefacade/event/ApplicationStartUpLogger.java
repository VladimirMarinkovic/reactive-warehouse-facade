package com.example.reactivewarehousefacade.event;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationStartUpLogger {

    private final Environment env;
    private final WebEndpointProperties webEndpointProperties;

    public static final String SPRING_PROFILE_SWAGGER = "swagger";

    @EventListener(ApplicationStartedEvent.class)
    public void logApplicationStartUp(final ApplicationStartedEvent event) {
        final Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());

        final String protocol = env.getProperty("server.ssl.key-store") != null ? "https" : "http";
        final String serverPort = env.getProperty("server.port");
        final String contextPath = Optional.ofNullable(env.getProperty("server.servlet.context-path")).filter(StringUtils::isBlank).orElse("/");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                 "Application '{}' is running! Access URLs:\n\t" +
                 "Local: \t\t{}://localhost:{}{}\n\t" +
                 "External: \t{}://{}:{}{}\n\t" +
                 "Swagger: \t{}\n\t" +
                 "Health:  \t{}://{}:{}{}/health\n\t" +
                 "Profile(s): \t{}\n----------------------------------------------------------",
                 env.getProperty("spring.application.name"),
                 protocol,
                 serverPort,
                 contextPath,
                 protocol,
                 hostAddress,
                 serverPort,
                 contextPath,
                 activeProfiles.contains(SPRING_PROFILE_SWAGGER) ? protocol + "://" + hostAddress + ":" + serverPort + contextPath + "swagger-ui/index.html" : "No Swagger Deployed",
                 protocol,
                 hostAddress,
                 serverPort,
                 webEndpointProperties.getBasePath(),
                 env.getActiveProfiles());
    }

}
