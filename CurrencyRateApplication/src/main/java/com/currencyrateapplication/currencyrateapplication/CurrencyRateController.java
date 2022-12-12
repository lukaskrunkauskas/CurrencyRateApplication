package com.currencyrateapplication.currencyrateapplication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.xml.sax.SAXException;

import javax.websocket.server.PathParam;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;


@Controller
public class CurrencyRateController {

    private final CurrencyDataSource currencyDataSource;

    public CurrencyRateController(CurrencyDataSource currencyDataSource) {
        this.currencyDataSource = currencyDataSource;
    }

    @GetMapping("/get_currency_history")
    public String currencyHistoryPage(Model model, @PathParam("get_currency_history") String ccy) throws ParserConfigurationException, IOException, SAXException {
        List<Dictionary<String, String>> currencies = new ArrayList<>();
        String url = "https://www.lb.lt/webservices/FxRates/FxRates.asmx/getFxRatesForCurrency?tp=eu&ccy=" + ccy + "&dtFrom=2015-01-01&dtTo=2021-01-01";
        List<com.currencyrateapplication.currencyrateapplication.FxRate> getData = currencyDataSource.getLatestRates(url);
        int loopLength = currencyDataSource.getLatestRates(url).size();
        for (int i = 0; i < loopLength; i++) {
            Dictionary<String, String> currencyAndRate = new Hashtable<>();
            if (!getData.get(i).getCurrency().equals("EUR")) {
                currencyAndRate.put("name", getData.get(i).getCurrency());
                currencyAndRate.put("rate", getData.get(i).getAmount());
                currencyAndRate.put("date", getData.get(i).getDate());
                currencies.add(currencyAndRate);
            }
        }
        model.addAttribute("currencies", currencies);
        return "currencyHistory";
    }

    @GetMapping("/")
    public String homePage(Model model) throws ParserConfigurationException, IOException, SAXException {
        List<Dictionary<String, String>> currencies = new ArrayList<>();
        String url = "http://www.lb.lt/webservices/FxRates/FxRates.asmx/getCurrentFxRates?tp=eu";
        List<com.currencyrateapplication.currencyrateapplication.FxRate> getData = currencyDataSource.getLatestRates(url);
        int loopLength = currencyDataSource.getLatestRates(url).size();
        for (int i = 0; i < loopLength; i++) {
            Dictionary<String, String> currencyAndRate = new Hashtable<>();
            if (!getData.get(i).getCurrency().equals("EUR")) {
                currencyAndRate.put("name", getData.get(i).getCurrency());
                currencyAndRate.put("rate", getData.get(i).getAmount());
                currencies.add(currencyAndRate);
            }
        }

        model.addAttribute("currencies", currencies);
        return "home";
    }

    @GetMapping("/calculator")
    public String calculatorPage(Model model, @PathParam("calculator") String ccyFrom, @PathParam("calculator") String sum, @PathParam("calculator") String ccyTo) throws ParserConfigurationException, IOException, SAXException {
        Dictionary<String, Double> answer = new Hashtable<>();

        String url = "http://www.lb.lt/webservices/FxRates/FxRates.asmx/getCurrentFxRates?tp=eu";
        answer.put("atsakymas", currencyDataSource.calculateCurrency(url, ccyFrom, sum, ccyTo));

        model.addAttribute("answer", answer);
        return "calculator";
    }

}
