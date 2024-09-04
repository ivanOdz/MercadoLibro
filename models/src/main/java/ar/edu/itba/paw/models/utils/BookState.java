package ar.edu.itba.paw.models.utils;

public enum BookState {
	
	NEW(1),
	LIKE_NEW(2),
	VERY_GOOD(3),
	GOOD(4),
	ACCEPTABLE(5),
	WORN(6);
	
	private final int value;
	
	BookState(int value) {
		
		this.value = value;
    }
	
	public static BookState fromInt(int i) {
		
		return BookState.values()[i];
	}
	
	public int getValue() {
		
		return this.value;
	}
	
	public String getKey() {
		
		return "genre." + name();
	}
}