package ar.edu.itba.paw.models.utils;

public enum BookState {

	NEW("bookstate.new"),
	LIKE_NEW("bookstate.like.new"),
	VERY_GOOD("bookstate.very.good"),
	GOOD("bookstate.good"),
	ACCEPTABLE("bookstate.acceptable"),
	WORN("bookstate.worn");

	private final String value;

	BookState(String value) {
		this.value = value;
	}

	public static BookState fromString(String bookStateFilter) {
		for (BookState state : BookState.values()) {
			if (state.value.equalsIgnoreCase(bookStateFilter)) {
				return state;
			}
		}
		return null;
	}

	public String getValue() {
		return this.value;
	}
}
