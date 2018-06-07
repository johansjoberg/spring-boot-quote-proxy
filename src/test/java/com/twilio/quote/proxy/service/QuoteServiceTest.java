package com.twilio.quote.proxy.service;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.twilio.quote.proxy.client.GuidResponse;
import com.twilio.quote.proxy.exception.BadRequestException;
import com.twilio.quote.proxy.resource.Quote;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.fail;
import static org.springframework.http.HttpStatus.ACCEPTED;


public class QuoteServiceTest extends AbstractServiceTest {

	@Autowired
	protected QuoteService quoteService;

	@Test
	public void getQuoteShouldReturnQuoteWhenValidKeywordIsProvided() {
		final String guid = UUID.randomUUID().toString();
		final String myKeyword = "myKeyword";
		final String quote = "My best quote.";

		stubAcceptedBackendResponse(guid);

		// call quote service to get a deferred result
		final DeferredResult<ResponseEntity<Quote>> quoteResult = quoteService.getQuote(myKeyword);

		// simulate callback
		final GuidResponse guidResponse = quoteService.provideQuote(new Quote(guid, myKeyword, quote));

		// verify that correct guid is retured
		Assert.assertEquals(guid, guidResponse.getGuid());

		// verify result is completed and that it has the correct content
		Assert.assertTrue(quoteResult.isSetOrExpired());
		final ResponseEntity<Quote> result = (ResponseEntity<Quote>) quoteResult.getResult();
		Assert.assertEquals(myKeyword, result.getBody().getKeyword());
		Assert.assertEquals(guid, result.getBody().getGuid()); // will fail because we have not connected
		Assert.assertEquals(quote, result.getBody().getQuote());
	}

	@Test
	public void provideQuoteShouldThrowBadRequestExceptionWhenUnknownGuidIsProvided() {
		final String guid = UUID.randomUUID().toString();
		final String myKeyword = "myKeyword";
		final String quote = "My best quote.";


		try {
			quoteService.provideQuote(new Quote(guid, myKeyword, quote));
		} catch(BadRequestException ex) {
			return;
		}

		fail();
	}

	private void stubAcceptedBackendResponse(String guid) {
		WireMock.configureFor("localhost", 8020);
		stubFor(WireMock.post(urlEqualTo("/v1/quote"))
			.willReturn(aResponse()
				.withStatus(ACCEPTED.value())
				.withHeader("Content-type", "application/json")
				.withBody("{ \"guid\": \"" + guid+ "\" }")
			)
		);
	}
}
