package ar.edu.itba.paw.models.utils;

public enum BookState {
	
	NEW(0),
	LIKE_NEW(1),
	VERY_GOOD(2),
	GOOD(3),
	ACCEPTABLE(4),
	WORN(5);
	
	private final int value;
	
	BookState(int value) {
		
		this.value = value;
    }
	
	public static BookState fromInt(int i) {
		for (BookState state : BookState.values()) {
			if (state.getValue() == i) {
				return state;
			}
		}
		return null;
	}
	
	public int getValue() {
		
		return this.value;
	}
	
	public String getKey() {
		
		return "genre." + name();
	}
}