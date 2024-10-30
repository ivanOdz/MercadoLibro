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
		try {
			return BookState.values()[i];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public int getValue() {
		
		return this.value;
	}
	
	public String getKey() {
		
		return "genre." + name();
	}

	public static BookState fromString(String bookStateFilter) {
		try {
			int intValue = Integer.parseInt(bookStateFilter);
			return BookState.fromInt(intValue);
		} catch (Exception e) {
			return null;
		}
	}

}