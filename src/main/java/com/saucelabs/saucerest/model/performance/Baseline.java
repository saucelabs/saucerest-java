package com.saucelabs.saucerest.model.performance;

import java.util.List;

public class Baseline {
    public Double upperBoundary;
    public Double lowerBoundary;
    public Double baseline;
    public List<BaselineValue> values;
}
