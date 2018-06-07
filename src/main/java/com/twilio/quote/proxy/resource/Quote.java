package com.twilio.quote.proxy.resource;

import java.util.Objects;

public class Quote {
	private String guid;
	private String keyword;
	private String quote;

	public Quote() {
	}

	public Quote(String guid, String keyword, String quote) {
		this.guid = guid;
		this.keyword = keyword;
		this.quote = quote;
	}

	public String getGuid() {
		return guid;
	}

	public String getKeyword() {
		return keyword;
	}

	public String getQuote() {
		return quote;
	}

	public Quote setGuid(String guid) {
		this.guid = guid;
		return this;
	}

	public Quote setKeyword(String keyword) {
		this.keyword = keyword;
		return this;
	}

	public Quote setQuote(String quote) {
		this.quote = quote;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Quote)) {
			return false;
		}
		Quote quote1 = (Quote) o;
		return Objects.equals(guid, quote1.guid) &&
			Objects.equals(keyword, quote1.keyword) &&
			Objects.equals(quote, quote1.quote);
	}

	@Override
	public int hashCode() {

		return Objects.hash(guid, keyword, quote);
	}

	@Override
	public String toString() {
		return "Quote{" +
			"guid='" + guid + '\'' +
			", keyword='" + keyword + '\'' +
			", quote='" + quote + '\'' +
			'}';
	}
}
