package com.twilio.quote.proxy.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers({
	"Accept: application/json",
	"Content-Type: application/x-www-form-urlencoded"})
public interface QuoteBackendClient {

	@RequestLine("POST /quote")
	GuidResponse asyncGetRandomQuote(@Param("keyword") String keyword);
}