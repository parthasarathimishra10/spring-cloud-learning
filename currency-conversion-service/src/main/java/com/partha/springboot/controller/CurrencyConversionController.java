package com.partha.springboot.controller;

import com.partha.springboot.entities.CurrencyConversion;
import com.partha.springboot.proxy.CurrencyExchangeProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/currency-conversion")
public class CurrencyConversionController {

    @Autowired
    private Environment environment;

    @Autowired
    private CurrencyExchangeProxy currencyExchangeProxy;

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @GetMapping("/rest-template/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion getConversionByRestTemplate(@PathVariable String from, @PathVariable String to,
                                                          @PathVariable BigDecimal quantity) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);

        CurrencyConversion currencyConversion = restTemplate.getForObject("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, uriVariables);
        currencyConversion.setQuantity(quantity);
        currencyConversion.setTotalCalculatedAmount(quantity.multiply(currencyConversion.getConversionMultiple()));
        currencyConversion.setEnvironment(environment.getProperty("local.server.port").concat(" :: Rest Template"));
        return currencyConversion;
    }

    @GetMapping("/feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion getConversionByFeign(@PathVariable String from, @PathVariable String to,
                                                   @PathVariable BigDecimal quantity) {
        CurrencyConversion currencyConversion = currencyExchangeProxy.getExchangeRate(from, to);
        currencyConversion.setQuantity(quantity);
        currencyConversion.setTotalCalculatedAmount(quantity.multiply(currencyConversion.getConversionMultiple()));
        currencyConversion.setEnvironment(environment.getProperty("local.server.port").concat(" :: Feign"));
        return currencyConversion;
    }
}
