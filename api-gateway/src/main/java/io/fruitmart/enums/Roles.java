package io.fruitmart.enums;

public enum Roles {
	USER("USER"),ADMIN("ADMIN"),CUSTOMER("CUSTOMER");
	
	public final String label;

    private Roles(String label) {
        this.label = label;
    }
}
