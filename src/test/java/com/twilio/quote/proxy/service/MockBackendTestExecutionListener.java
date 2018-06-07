package com.twilio.quote.proxy.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class MockBackendTestExecutionListener extends AbstractTestExecutionListener {
	// Need to keep the server static to ensure that there is only one instance running. NOTE! Not possible to run tests using wire mock in parallel.
	private static WireMockServer wireMockServer = new WireMockServer(options().port(8020));

	@Override
	public void beforeTestClass(TestContext testContext) {
		// start the server when it is not running and the first test class is setup
		if(wireMockServer != null && !wireMockServer.isRunning()) {
			wireMockServer.start();
		}
	}

	@Override
	public void beforeTestMethod(TestContext testContext) {
	  // need to reset all mapping when there is a running wire mock server before a test to make sure no mappings from previous test remain
		if(wireMockServer != null && wireMockServer.isRunning()) {
		  wireMockServer.resetMappings();
		  wireMockServer.resetRequests();
		  wireMockServer.resetScenarios();
	  }
	}
}