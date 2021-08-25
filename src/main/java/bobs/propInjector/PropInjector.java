package bobs.propInjector;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;

@Component
public interface PropInjector {
    GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
    ConfigurableEnvironment env = ctx.getEnvironment();
    MutablePropertySources prop = env.getPropertySources();

    String getBaseUrl();
    String get42Secret();
    String get42Uid();
    String getSlackToken();
    String getSlackUrl();
}
