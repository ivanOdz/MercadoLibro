package ar.edu.itba.paw.models.utils;

import java.util.List;

public enum BookState {

	NEW("bookstate.new", "new"),
	LIKE_NEW("bookstate.like.new", "like_new"),
	VERY_GOOD("bookstate.very.good", "very_good"),
	GOOD("bookstate.good", "good"),
	ACCEPTABLE("bookstate.acceptable", "acceptable"),
	WORN("bookstate.worn", "worn");

	private final String value;
	private final String string;

	BookState(String value, String string) {
		this.value = value;
        this.string = string;
    }

	public static BookState fromString(String bookStateFilter) {
		for (BookState state : BookState.values()) {
			if (state.string.equalsIgnoreCase(bookStateFilter)) {
				return state;
			}
		}
		return null;
	}

	public String getValue() {
		return this.value;
	}
}
