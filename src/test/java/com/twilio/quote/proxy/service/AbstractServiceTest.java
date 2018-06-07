package com.twilio.quote.proxy.service;

import com.twilio.quote.proxy.config.ApplicationConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DelegatingSmartContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.springframework.test.context.TestExecutionListeners.MergeMode.REPLACE_DEFAULTS;

@RunWith(SpringRunner.class)
@ContextConfiguration(
	loader = DelegatingSmartContextLoader.class,
	inheritInitializers = false,
	inheritLocations = false)
@TestExecutionListeners(
	value = {DependencyInjectionTestExecutionListener.class, MockBackendTestExecutionListener.class},
	inheritListeners = false,
	mergeMode = REPLACE_DEFAULTS)
@SpringBootTest(
	properties = "quote.backend.url=http://localhost:8020/v1",
	classes = ApplicationConfiguration.class,
	webEnvironment = SpringBootTest.WebEnvironment.NONE)
public abstract class AbstractServiceTest extends AbstractJUnit4SpringContextTests {

}
