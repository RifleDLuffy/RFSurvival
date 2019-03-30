package com.rifledluffy.survival.domestication.animal.base;

public enum DomesticSize {

	SMALL,MEDIUM,LARGE;

	public double getScale() {
		switch (this) {
			case SMALL:
				return 2;
			default:
			case MEDIUM:
				return 1.5;
			case LARGE:
				return 1;
		}
	}
}
