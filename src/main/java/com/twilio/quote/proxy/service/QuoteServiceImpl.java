package com.twilio.quote.proxy.service;

import com.twilio.quote.proxy.client.GuidResponse;
import com.twilio.quote.proxy.client.QuoteBackendClient;
import com.twilio.quote.proxy.exception.BadRequestException;
import com.twilio.quote.proxy.resource.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
class QuoteServiceImpl implements QuoteService {

	private final ConcurrentMap<String, DeferredResult<ResponseEntity<Quote>>> deferredResultByGuid = new ConcurrentHashMap<>();

	@Autowired
	private QuoteBackendClient quoteBackendClient;

	@Override
	public DeferredResult<ResponseEntity<Quote>> getQuote(String keyword) {
		final GuidResponse guid = quoteBackendClient.asyncGetRandomQuote(keyword);
		final DeferredResult<ResponseEntity<Quote>> quoteDeferredResult = new DeferredResult<>(5000L);
		quoteDeferredResult.onCompletion(() -> deferredResultByGuid.remove(guid.getGuid()));
		quoteDeferredResult.onTimeout(() -> deferredResultByGuid.remove(guid.getGuid()));

		deferredResultByGuid.putIfAbsent(guid.getGuid(), quoteDeferredResult);

		return quoteDeferredResult;
	}

	@Override
	public GuidResponse provideQuote(Quote quote) {
		final DeferredResult<ResponseEntity<Quote>> quoteDeferredResult = deferredResultByGuid.get(quote.getGuid());
		if(quoteDeferredResult == null) {
			throw new BadRequestException("No guid found.");
		}

		final boolean succeeded = quoteDeferredResult.setResult(new ResponseEntity<>(quote, HttpStatus.OK));
		if(!succeeded) {
			throw new RuntimeException("Failed to provide quote.");
		}

		return new GuidResponse(quote.getGuid());
	}
}