package com.MTAPizza.Sympoll.groupmanagementservice.config;

import com.MTAPizza.Sympoll.groupmanagementservice.client.MediaClient;
import com.MTAPizza.Sympoll.groupmanagementservice.client.PollClient;
import com.MTAPizza.Sympoll.groupmanagementservice.client.UserClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {
    @Value("${user.service.url}")
    private String userServiceUrl;

    @Value("${poll.service.url}")
    private String pollServiceUrl;

    @Value("${media.service.url}")
    private String mediaServiceUrl;

    @Bean
    public UserClient userClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(userServiceUrl)
                .build();

        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(UserClient.class);
    }

    @Bean
    public PollClient pollClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(pollServiceUrl)
                .build();

        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(PollClient.class);
    }

    @Bean
    public MediaClient mediaClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(mediaServiceUrl)
                .build();

        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(MediaClient.class);    }
}
