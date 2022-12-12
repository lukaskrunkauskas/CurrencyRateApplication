package com.currencyrateapplication.CurrencyRateApplication;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyDataSource {

    public List<com.currencyrateapplication.CurrencyRateApplication.FxRate> getLatestRates(String url) throws ParserConfigurationException, IOException, SAXException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(response.toString())));
        NodeList nList1 = doc.getElementsByTagName("CcyAmt");
        NodeList nList2 = doc.getElementsByTagName("FxRate");
        List<com.currencyrateapplication.CurrencyRateApplication.FxRate> ccyAndAmt = new ArrayList<>();
        String a;
        String b;
        String c;
        int i = 0;
        for (int temp = 0; temp < nList1.getLength(); temp++) {
            Node node1 = nList1.item(temp);
            Node node2 = nList2.item(i);
            if (node1.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement1 = (Element) node1;
                Element eElement2 = (Element) node2;

                a = eElement1.getElementsByTagName("Ccy").item(0).getTextContent();
                b = eElement1.getElementsByTagName("Amt").item(0).getTextContent();
                if (temp % 2 != 0) {
                    c = eElement2.getElementsByTagName("Dt").item(0).getTextContent();
                    i++;
                } else {
                    c = " ";
                }
                com.currencyrateapplication.CurrencyRateApplication.FxRate single = new com.currencyrateapplication.CurrencyRateApplication.FxRate(a, b, c);
                ccyAndAmt.add(single);
            }
        }
        return ccyAndAmt;
    }

    public double calculateCurrency(String url, String ccyFrom, String sum, String ccyTo) throws ParserConfigurationException, IOException, SAXException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        StringBuilder xmlString = response;
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xmlString.toString())));
        NodeList nList = doc.getElementsByTagName("CcyAmt");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node node = nList.item(temp);
            Element eElement = (Element) node;
            if (eElement.getElementsByTagName("Ccy").item(0).getTextContent().equals(ccyFrom)) {
                ccyFrom = eElement.getElementsByTagName("Amt").item(0).getTextContent();
            }
            if (eElement.getElementsByTagName("Ccy").item(0).getTextContent().equals(ccyTo)) {
                ccyTo = eElement.getElementsByTagName("Amt").item(0).getTextContent();
            }
        }
        return Double.parseDouble(ccyTo) / Double.parseDouble(ccyFrom) * Double.parseDouble(sum);
    }
}

