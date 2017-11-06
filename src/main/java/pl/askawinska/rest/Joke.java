package pl.askawinska.rest;

public class Joke {
	protected Value value;

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public class Value {
		protected String joke;

		public String getJoke() {
			return joke;
		}

		public void setJoke(String joke) {
			this.joke = joke;
		}
	}

	public String getJoke(){
		return value != null ? value.getJoke().replace("&quot;", "\"") : null;
	}
}
