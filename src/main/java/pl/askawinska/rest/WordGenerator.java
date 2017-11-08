package pl.askawinska.rest;

import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WordGenerator {

	protected static final String api ="http://setgetgo.com/randomword/get.php";

	protected RestTemplate restTemplate;

	public WordGenerator(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public CompletableFuture<String> generate() {
		return CompletableFuture.supplyAsync(() ->
				restTemplate.getForObject(api, String.class));
	}
}
