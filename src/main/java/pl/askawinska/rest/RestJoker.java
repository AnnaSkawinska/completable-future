package pl.askawinska.rest;

import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestJoker {

	protected static final String api = "http://api.icndb.com/jokes/random";

	protected RestTemplate restTemplate;

	public RestJoker(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public CompletableFuture<String> joke(String firstName, String lastName) {
		return CompletableFuture.supplyAsync(() ->
				restTemplate.getForObject(api + "?firstName=" + firstName + "&lastName=" + lastName, Joke.class)
						.getJoke());
	}
}
