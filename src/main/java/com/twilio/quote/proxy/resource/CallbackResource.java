package com.twilio.quote.proxy.resource;

import com.twilio.quote.proxy.client.GuidResponse;
import com.twilio.quote.proxy.exception.BadRequestException;
import com.twilio.quote.proxy.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.accepted;

@RestController
@RequestMapping("/callback/v1")
public class CallbackResource {
	static final String GUID_MISSING_ERROR_MESSAGE = "Guid is a mandatory element.";
	static final String QUOTE_MISSING_ERROR_MESSAGE = "quote is a mandatory element.";

	@Autowired
	 private QuoteService quoteService;

	@RequestMapping(value = "/quoteResult", produces = {"application/json"})
	public ResponseEntity<GuidResponse> postRandomQuote(@RequestBody Quote quote) {
		validateBody(quote);

		return accepted().body(quoteService.provideQuote(quote));
	}

	private void validateBody(Quote quote) {
		if(quote.getGuid() == null || quote.getGuid().isEmpty()) {
			throw new BadRequestException(GUID_MISSING_ERROR_MESSAGE);
		}

		if(quote.getQuote() == null || quote.getQuote().isEmpty()) {
			throw new BadRequestException(QUOTE_MISSING_ERROR_MESSAGE);
		}
	}
}
