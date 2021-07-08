package bobs.PropInjector;

import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class PropInjectorImpl implements PropInjector {

    @PostConstruct
    void propInit() throws IOException {
        prop.addLast(new ResourcePropertySource("classpath:application.properties"));
    }

    @Override
    public String getBaseUrl(){
        return env.getProperty("baseUrl");
    }

    @Override
    public String get42Secret(){
        return env.getProperty("42OAuthSecret");
    }

    public String get42Uid(){
        return env.getProperty("42OAuthUid");
    }

    @Override
    public String getSlackToken(){
        return env.getProperty("slackToken");
    }

    @Override
    public String getSlackUrl(){
        return env.getProperty("slackUrl");
    }
}
