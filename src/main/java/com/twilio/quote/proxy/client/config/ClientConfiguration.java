package com.twilio.quote.proxy.client.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.twilio.quote.proxy.client.QuoteBackendClient;
import feign.Feign;
import feign.Logger;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper()
			.registerModule(new JavaTimeModule())
			.configure(SerializationFeature.WRAP_ROOT_VALUE, false)
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	@Bean
	public QuoteBackendClient quoteBackendClient(@Value("${quote.backend.url}") String host, ObjectMapper objectMapper) {
		return Feign.builder()
			.encoder(new FormEncoder())
			.decoder(new JacksonDecoder(objectMapper))
			.logger(new Slf4jLogger(QuoteBackendClient.class))
			.logLevel(Logger.Level.FULL)
			.target(QuoteBackendClient.class, host);
	}
}