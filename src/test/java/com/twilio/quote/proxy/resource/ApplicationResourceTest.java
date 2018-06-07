package com.twilio.quote.proxy.resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.request.async.DeferredResult;

import static com.twilio.quote.proxy.exception.ResourceExceptionHandler.INTERNAL_SERVER_ERROR_MESSAGE;
import static com.twilio.quote.proxy.resource.ApplicationResource.KEYWORD_MANDATORY_PARAM_ERROR_MESSAGE;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class ApplicationResourceTest extends AbstractResourceTest {
	private static final String RESOURCE_URL = "/application/v1/quote/";

	@Autowired
	private ApplicationResource tested;


	@Test
	public void shouldReturnQuoteWhenValidKeywordIsProvided() throws Exception {
		final String guid = "1234";
		final String keyword = "abcd";
		final String quote = "Best quote ever.";

		final DeferredResult<ResponseEntity<Quote>> mockedResult = new DeferredResult<>();
		mockedResult.setResult(new ResponseEntity<>(new Quote(guid, keyword, quote), HttpStatus.OK));
		when(quoteService.getQuote(keyword)).thenReturn(mockedResult);

		final MvcResult mvcResult = mockMvc.perform((get(RESOURCE_URL).param("keyword", keyword)))
			.andExpect(request().asyncStarted())
			.andReturn();

		mockMvc.perform(asyncDispatch(mvcResult))
			.andExpect(status().isOk())
			.andExpect(content().contentType(CONTENT_TYPE))
			.andExpect(jsonPath("$.keyword", is(keyword)))
			.andExpect(jsonPath("$.quote", is(quote)));
	}

	@Test
	public void shouldReturnErrorMessageWhenKeywordIsNotProvided() throws Exception {
		mockMvc.perform(get(RESOURCE_URL))
			.andExpect(status().isBadRequest())
			.andExpect(content().contentType(CONTENT_TYPE))
			.andExpect(jsonPath("$.message", is(KEYWORD_MANDATORY_PARAM_ERROR_MESSAGE)));
	}

	@Test
	public void shouldReturnServerErrorWhenUnexpectedExceptionOccurs() throws Exception {
		final String keyword = "abcd";

		when(quoteService.getQuote(keyword)).thenThrow(new RuntimeException("Something unexpected happened."));

		mockMvc.perform(get(RESOURCE_URL).param("keyword", keyword))
			.andExpect(status().isInternalServerError())
			.andExpect(content().contentType(CONTENT_TYPE))
			.andExpect(jsonPath("$.message", is(INTERNAL_SERVER_ERROR_MESSAGE)));

	}

}
