package com.twilio.quote.proxy.resource;

import com.twilio.quote.proxy.exception.BadRequestException;
import com.twilio.quote.proxy.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/application/v1")
public class ApplicationResource {
	static final String KEYWORD_MANDATORY_PARAM_ERROR_MESSAGE = "'keyword' is a mandatory request parameter.";

	@Autowired
	 private QuoteService quoteService;

	@RequestMapping(value = "/quote", produces = {"application/json"})
	public DeferredResult<ResponseEntity<Quote>> getRandomQuote(@RequestParam(value = "keyword", defaultValue = "", required = true) String keyword) {
		if(keyword.isEmpty()) {
			throw new BadRequestException(KEYWORD_MANDATORY_PARAM_ERROR_MESSAGE);
		}

		return quoteService.getQuote(keyword);
	}
}
