package ai.monitorize.chatbot4j.tools;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BingSearchService {

    private static final String BING_API_URL = "https://api.bing.microsoft.com/v7.0/search";
    private static final String API_KEY = "YOUR_BING_API_KEY";
    private static final String API_KEY_HEADER = "Ocp-Apim-Subscription-Key";

    public String search(String query) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Ocp-Apim-Subscription-Key", API_KEY);

        String requestUrl = BING_API_URL + "?q=" + query;
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }
}
