package com.currencyrateapplication.currencyrateapplication;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CurrencyRateApplicationTests {

	CurrencyDataSource currencyDataSource = new CurrencyDataSource();

	@Test
	void contextLoads() {
	}

	@Test
	void testGetLatestRates() throws ParserConfigurationException, IOException, SAXException {
		String url = "http://www.lb.lt/webservices/FxRates/FxRates.asmx/getCurrentFxRates?tp=eu";
		assertThat(currencyDataSource.getLatestRates(url)).isNotEmpty();
	}

	@Test
	void testCalculateCurrency() throws ParserConfigurationException, IOException, SAXException {
		String url = "http://www.lb.lt/webservices/FxRates/FxRates.asmx/getCurrentFxRates?tp=eu";
		String ccyFrom = "USD";
		String sum = "100";
		String ccyTo = "EUR";
		currencyDataSource = new CurrencyDataSource();

		double answer = currencyDataSource.calculateCurrency(url, ccyFrom, sum, ccyTo);

		assertTrue(answer == 94.6790380609733);
	}
}
