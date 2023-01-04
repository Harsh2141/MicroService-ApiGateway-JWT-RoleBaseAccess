package io.fruitmart.enums;

public enum Urls {
	GET_DATA("order/getData");

	public final String label;

    private Urls(String label) {
        this.label = label;
    }

}
