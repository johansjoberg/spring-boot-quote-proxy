package com.twilio.quote.proxy.client;

import java.util.Objects;

public class GuidResponse {
	private String guid;

	public GuidResponse() {
	}

	public GuidResponse(String guid) {
		this.guid = guid;
	}

	public String getGuid() {
		return guid;
	}

	public GuidResponse setGuid(String guid) {
		this.guid = guid;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof GuidResponse)) {
			return false;
		}
		GuidResponse that = (GuidResponse) o;
		return Objects.equals(guid, that.guid);
	}

	@Override
	public int hashCode() {

		return Objects.hash(guid);
	}

	@Override
	public String toString() {
		return "GuidResponse{" +
			"guid='" + guid + '\'' +
			'}';
	}
}
