package pl.askawinska;


import org.springframework.stereotype.Component;

@Component
public class Sleeper {
	public static void sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
