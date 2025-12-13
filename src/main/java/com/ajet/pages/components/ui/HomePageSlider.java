package com.ajet.pages.components.ui;

public enum HomePageSlider {
    NEXT("Next Slide"),
    PREVIOUS("Previous Slide"),
    FIRST("First Slide"),
    LAST("Last Slide");

    private final String description;

    HomePageSlider(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
