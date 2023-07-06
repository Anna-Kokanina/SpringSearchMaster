package searchengine.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

// The @Getter and @Setter annotations from Lombok library generate
// the standard getter and setter methods for the fields in the class.
// This helps to reduce boilerplate code.

@Getter
@Setter

// The @Component annotation tells Spring that this class is a bean and should be
// automatically detected by Spring's component scanning.
@Component

// The @ConfigurationProperties annotation tells Spring to map the properties
// in the application.properties file that start with "indexing-settings" to this bean.
@ConfigurationProperties(prefix = "indexing-settings")
public class SitesList {

    // This list will contain Site objects, each representing a site to be indexed.
    // The sites are loaded from the application.properties file.
    private List<Site> sites;
}
