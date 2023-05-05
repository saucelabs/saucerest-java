package com.saucelabs.saucerest.model.platform;

import java.util.List;

public class SupportedPlatforms {

    public List<Platform> getPlatforms() {
        return platforms;
    }

    private final List<Platform> platforms;

    public SupportedPlatforms(List<Platform> platforms) {
        super();
        this.platforms = platforms;
    }
}