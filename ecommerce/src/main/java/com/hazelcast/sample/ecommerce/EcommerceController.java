package com.hazelcast.sample.ecommerce;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class EcommerceController {
    @Value("${spring.application.name}")
    String appName;
    @Value("${USER_SERVICE:localhost}")
    private String userService;
    @Value("${PRODUCT_SERVICE:localhost}")
    private String productService;
    @Value("${BANKING_SERVICE:localhost}")
    private String bankingService;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("userCredits", callService(userService, "user-credits", HttpMethod.GET, null, null));
        model.addAttribute("stock", callService(productService, "stock", HttpMethod.GET, null, null));
        return "home";
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("userCredits", callService(userService, "user-credits", HttpMethod.GET, null, null));
        return "cart";
    }

    @PostMapping("/buy")
    public ResponseEntity<Object> buy(@RequestParam("quantity") Integer quantity, @RequestParam("amount") Double amount) {
        String transactionId = UUID.randomUUID().toString();
        System.out.println("transactionId: " + transactionId);
        HashMap<String, Object> creditsParams = new HashMap<>();
        creditsParams.put("amount", -1 * amount);
        callService(userService, "user-credits", HttpMethod.POST, creditsParams, transactionId);

        HashMap<String, Object> stockParams = new HashMap<>();
        stockParams.put("quantity", -1 * quantity);
        callService(productService, "stock", HttpMethod.POST, stockParams, transactionId);

        callService(bankingService, "bank-transaction", HttpMethod.POST, stockParams, transactionId);

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/").build();
    }

    private String callService(String service, String endpoint, HttpMethod httpMethod, Map<String, Object> parameters,
                               String transactionId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://" + service + ":8080/" + endpoint);

        if (parameters != null) {
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                builder.queryParam(parameter.getKey(), parameter.getValue());
            }
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        if (transactionId != null) {
            headers.set("transactionId", transactionId);
        }
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(builder.toUriString(), httpMethod, new HttpEntity(headers), String.class);
        return responseEntity.getBody();
    }
}