package com.stated.royally.config;

import kong.unirest.Unirest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * UniRest Config
 *
 * @author Nate Vardell
 * @since 7/1/2019
 */
@Log4j2
@Component
@Configuration
public class UniConfig {

    @Value("${official.resource.base}") String baseUrl;
    @Value("${clash.royal.dev.api.token}") String jwt;

    @PostConstruct
    public void init() {
        log.info("\n\nConfiguring Uni. JWT = " + jwt + "\n\n");

        Unirest.config()
                // Setup Metrics
                .instrumentWith(requestSummary -> {
                    long startNano = System.nanoTime();
                    return (responseSummary, exception) ->
                            log.info("path: {} status: {} time: {}",
                                    requestSummary.getRawPath(),
                                    responseSummary.getStatus(),
                                    System.nanoTime() - startNano);
                })
                // Set Custom Object Mapper
                .setObjectMapper(new CustomObjectMapper())
                // Set Default Values
                .setDefaultHeader("Authorization", "Bearer " + jwt)
                .defaultBaseUrl(baseUrl);
    }
}
