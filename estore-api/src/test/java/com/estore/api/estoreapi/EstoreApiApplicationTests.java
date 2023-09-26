package com.estore.api.estoreapi;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class EstoreApiApplicationTests {
	SpringContext context = new SpringContext();
	ApplicationContext mockApplicationContext;
	EstoreApiApplication mockEstoreApiApplication;

	@Test
	void contextLoads() {
		mockApplicationContext = mock(ApplicationContext.class);
		when(mockApplicationContext.getBean(EstoreApiApplication.class)).thenReturn(mockEstoreApiApplication);
		context.setApplicationContext(mockApplicationContext);
	}

}
