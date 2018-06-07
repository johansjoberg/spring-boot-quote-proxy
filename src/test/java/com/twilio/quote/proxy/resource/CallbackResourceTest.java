package com.twilio.quote.proxy.resource;

import com.twilio.quote.proxy.client.GuidResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.twilio.quote.proxy.exception.ResourceExceptionHandler.INTERNAL_SERVER_ERROR_MESSAGE;
import static com.twilio.quote.proxy.resource.CallbackResource.GUID_MISSING_ERROR_MESSAGE;
import static com.twilio.quote.proxy.resource.CallbackResource.QUOTE_MISSING_ERROR_MESSAGE;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class CallbackResourceTest extends AbstractResourceTest {
	private static final String RESOURCE_URL = "/callback/v1/quoteResult/";

	@Autowired
	private ApplicationResource tested;

	@Test
	public void shouldReturnAcceptedWhenAllQuoteElementsIsProvided() throws Exception {
		final String guid = "1234";
		final String keyword = "abcd";
		final Quote quote = new Quote(guid, keyword, "Best quote ever.");

		when(quoteService.provideQuote(quote)).thenReturn(new GuidResponse(guid));

		mockMvc.perform(
			post(RESOURCE_URL)
				.contentType(CONTENT_TYPE)
				.content(asJson(quote)))
			.andExpect(status().isAccepted())
			.andExpect(content().contentType(CONTENT_TYPE))
			.andExpect(jsonPath("$.guid", is(guid)));
	}

	@Test
	public void shouldReturnBadRequestdWhenMandatoryElementGuidIsMissing() throws Exception {
		final Quote quote = new Quote();
		quote.setKeyword("abcd");
		quote.setQuote("efgh");

		mockMvc.perform(
			post(RESOURCE_URL)
				.contentType(CONTENT_TYPE)
				.content(asJson(quote)))
			.andExpect(status().isBadRequest())
			.andExpect(content().contentType(CONTENT_TYPE))
			.andExpect(jsonPath("$.message", is(GUID_MISSING_ERROR_MESSAGE)));
	}

	@Test
	public void shouldReturnBadRequestdWhenMandatoryElementQuoteIsMissing() throws Exception {
		final Quote quote = new Quote();
		quote.setKeyword("abcd");
		quote.setGuid("1234");

		mockMvc.perform(
			post(RESOURCE_URL)
				.contentType(CONTENT_TYPE)
				.content(asJson(quote)))
			.andExpect(status().isBadRequest())
			.andExpect(content().contentType(CONTENT_TYPE))
			.andExpect(jsonPath("$.message", is(QUOTE_MISSING_ERROR_MESSAGE)));
	}

	@Test
	public void shouldReturnServerErrorWhenUnexpectedExceptionOccurs() throws Exception {
		final Quote quote = new Quote("dsaf", "sdaf", "sgdfsa");
		when(quoteService.provideQuote(quote)).thenThrow(new RuntimeException("Something unexpected happened."));

		mockMvc.perform(
			post(RESOURCE_URL)
				.contentType(CONTENT_TYPE)
				.content(asJson(quote)))
			.andExpect(status().isInternalServerError())
			.andExpect(content().contentType(CONTENT_TYPE))
			.andExpect(jsonPath("$.message", is(INTERNAL_SERVER_ERROR_MESSAGE)));
	}

}
