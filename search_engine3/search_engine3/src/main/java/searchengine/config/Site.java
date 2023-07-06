package searchengine.config;

import lombok.Getter;
import lombok.Setter;

// The @Setter and @Getter annotations from Lombok library generate
// the standard setter and getter methods for the fields in the class.
// This helps to reduce boilerplate code.

@Setter
@Getter
public class Site {
    // URL of the site. This is where the site can be accessed on the internet.
    private String url;

    // Name of the site. This is a human-friendly name that can be used to identify the site.
    private String name;

    // Constructor method for Site class. This method is used to create a new instance of Site.
    public Site(String url, String name) {
        this.url = url;
        this.name = name;
    }

    // Default constructor for Site class. This is required for frameworks that use reflection,
    // such as Spring. Without a default constructor, these frameworks would not be able to instantiate the class.
    public Site() {
    }
}
