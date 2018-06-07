package com.twilio.quote.proxy.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.twilio.quote.proxy.service.QuoteService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static wiremock.com.google.common.base.Throwables.propagate;

@RunWith(SpringRunner.class)
@WebMvcTest
public abstract class AbstractResourceTest {
	@Autowired
	protected MockMvc mockMvc;

	@MockBean
	protected QuoteService quoteService;

	static final MediaType CONTENT_TYPE = new MediaType(APPLICATION_JSON.getType(), APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	static String asJson(Object object) {
		try {
			return jsonObjectMapper()
				.writer()
				.withDefaultPrettyPrinter()
				.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw propagate(e);
		}
	}

	private static ObjectMapper jsonObjectMapper() {
		return new ObjectMapper()
			.configure(SerializationFeature.WRAP_ROOT_VALUE, false)
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}
}
