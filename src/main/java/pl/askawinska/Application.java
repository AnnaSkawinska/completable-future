package pl.askawinska;

import com.google.common.base.Stopwatch;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import pl.askawinska.rest.RestJoker;

@SpringBootApplication
public class Application {

	protected RestJoker restJoker;

	public static void main(String args[]) {
		new SpringApplicationBuilder(Application.class).web(false).run();
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestJoker restJoker) throws Exception {
		this.restJoker = restJoker;

		return args -> {
			manualAsync();
//			supplyAsync();
//			separateThread();
//			kamilWasRight();
//			exceptionHandling();
//			anyOf();
//			allOf(); //TODO
//			thenAcceptBoth();
//			applyToEither();
//			anyCompleteWithStreams();
		};
	}

	protected void manualAsync() throws InterruptedException, ExecutionException {
		System.out.println("My son: -  Mooom!");
		System.out.println("Me: - Just a second, honey!");
		System.out.println("-- poor child has to wait --");

		Stopwatch stopWatch = Stopwatch.createStarted();
		CompletableFuture<String> momsAttention = momKeepsDoingSomethingElse();

		String answer = momsAttention.get();
		stopWatch.stop();
		System.out.println(answer);
		System.out.println(String.format("My son: - You LIED! It was WHOLE %d SECONDS!", stopWatch.elapsed(TimeUnit.SECONDS)));
	}

	protected CompletableFuture<String> momKeepsDoingSomethingElse() throws ExecutionException, InterruptedException {
		CompletableFuture<String> blocking = new CompletableFuture<>();
		ExecutorService executorService = Executors.newCachedThreadPool();
		executorService.submit(() -> {
			Thread.sleep(5000);
			blocking.complete("Me: - What is it, honey?");
			executorService.shutdown();
			return null;
		});
		return blocking;
	}

	protected void supplyAsync() throws ExecutionException, InterruptedException {
		System.out.println("Me: Put on your panties!");

		CompletableFuture<String> asyncPanties = CompletableFuture.supplyAsync(() -> {
			System.out.println("Son: <starts putting on panties>");
			Sleeper.sleep(3000);
			System.out.println("Son: <starts fighting his brother>");
			Sleeper.sleep(3000);
			System.out.println("Son: <starts playing with LEGO>");
			Sleeper.sleep(3000);
			return "Son: <finally puts on his panties>";
		});
		Sleeper.sleep(5000);
		System.out.println("Me: I said put on your panties NOW!");
		Sleeper.sleep(2000);
		System.out.println("Me: WHY DON'T YOU HAVE PANTIES YET?");
		Sleeper.sleep(1000);
		System.out.println(asyncPanties.get());
		System.out.println("My son: What do you mean? I've had them FOR AGES.");
	}

	protected void separateThread() throws ExecutionException, InterruptedException {
		CompletableFuture<Integer> async = CompletableFuture.supplyAsync(() -> {
			System.out.println(String.format("Async: - My name is %d", Thread.currentThread().getId()));
			Sleeper.sleep(2000);
			return 44;
		});

		System.out.println(String.format("Main thread: - My name is %d", Thread.currentThread().getId()));
		Thread.sleep(5000);
		System.out.println(async.get());
	}

	protected void kamilWasRight() {
		Runnable runnable = () -> {
			System.out.println("I believe I can fly!");
		};

		CompletableFuture<Void> async = CompletableFuture.runAsync(runnable);

		System.out.println(String.format("Did it fly?"));
	}

	protected void exceptionHandling() throws ExecutionException, InterruptedException {
		CompletableFuture<Integer> thrower = CompletableFuture.supplyAsync(() -> {
			int a = 6 / 2;
			if (a == 1) {
				return 3;
			} else {
				throw new RuntimeException("why?!");
			}
		}).exceptionally(throwable -> {
			System.out.println("Everything is under control.");
			return 1;
		});

		System.out.println("Calculation result: " + thrower.get());
	}

	protected void anyOf() throws ExecutionException, InterruptedException {
		CompletableFuture<String> one = restJoker.joke("Joanna", "Elastic");
		CompletableFuture<String> two = restJoker.joke("Anna", "Skawinska");
		CompletableFuture<String> three = restJoker.joke("Krolowa", "Sucharow");
		System.out.println(CompletableFuture.anyOf(one, two, three).get());
	}

	protected void allOf() throws ExecutionException, InterruptedException {
		CompletableFuture<String> one = restJoker.joke("Joanna", "Elastic");
		CompletableFuture<String> two = restJoker.joke("Anna", "Skawinska");
		CompletableFuture<String> three = restJoker.joke("Krolowa", "Sucharow");
		System.out.println(CompletableFuture.allOf(one, two, three).get());
	}

	protected void thenAcceptBoth() throws ExecutionException, InterruptedException {
		CompletableFuture<String> one = restJoker.joke("Joanna", "Elastic");
		CompletableFuture<String> two = restJoker.joke("Anna", "Skawinska");
		CompletableFuture<String> three = restJoker.joke("Krolowa", "Sucharow");

		one.thenAcceptBoth(two, (s, t) -> {
			System.out.println("s: " + s);
			System.out.println("t: " + t);
		}).thenAcceptBothAsync(three, (onetwo, u) -> {
			System.out.println("onetwo: " + onetwo);
			System.out.println("u: " + u);
		}).handle((aVoid, throwable) -> {
			if (throwable != null) {
				throwable.printStackTrace();
			}
			return aVoid;
		}).get();

	}

	protected void applyToEither() throws ExecutionException, InterruptedException {
		CompletableFuture<String> one = restJoker.joke("Joanna", "Elastic");
		CompletableFuture<String> two = restJoker.joke("Anna", "Skawinska");
		CompletableFuture<String> three = restJoker.joke("Krolowa", "Sucharow");
		System.out.println(
				two.applyToEither(one, s -> s)
						.applyToEither(three, s -> s).get());

	}

	protected void anyCompleteWithStreams() {
		CompletableFuture<String> one = restJoker.joke("Joanna", "Elastic");
		CompletableFuture<String> two = restJoker.joke("Anna", "Skawinska");
		CompletableFuture<String> three = restJoker.joke("Krolowa", "Sucharow");
		System.out.println(Stream.of(two, three, one).map(CompletableFuture::join).findAny().get());
		/// always the first in stream is first!
	}

}
