package com.twilio.quote.proxy.service;

import com.twilio.quote.proxy.client.GuidResponse;
import com.twilio.quote.proxy.resource.Quote;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public interface QuoteService {
	DeferredResult<ResponseEntity<Quote>> getQuote(String keyword);

	GuidResponse provideQuote(Quote quote);
}
