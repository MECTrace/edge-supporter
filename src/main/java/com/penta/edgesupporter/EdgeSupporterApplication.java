package com.penta.edgesupporter;

import com.penta.edgesupporter.configuration.FileUploadProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(
        {FileUploadProperties.class}
)
@SpringBootApplication
public class EdgeSupporterApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdgeSupporterApplication.class, args);
    }

}
