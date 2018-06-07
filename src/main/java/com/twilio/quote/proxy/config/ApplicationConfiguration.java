package com.twilio.quote.proxy.config;

import com.twilio.quote.proxy.SpringConfigBasePackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = SpringConfigBasePackage.class)
public class ApplicationConfiguration {
}
