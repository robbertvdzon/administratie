package com.vdzon.administratie.checkandfix.model;

import java.util.List;

public class CheckAndFixRegels {
    private List<CheckAndFixRegel> checkAndFixRegels;

    public List<CheckAndFixRegel> getCheckAndFixRegels() {
        return checkAndFixRegels;
    }

    private CheckAndFixRegels(Builder builder) {
        checkAndFixRegels = builder.checkAndFixRegels;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(CheckAndFixRegels copy) {
        Builder builder = new Builder();
        builder.checkAndFixRegels = copy.checkAndFixRegels;
        return builder;
    }

    public static final class Builder {
        private List<CheckAndFixRegel> checkAndFixRegels;

        private Builder() {
        }

        public Builder checkAndFixRegels(List<CheckAndFixRegel> val) {
            checkAndFixRegels = val;
            return this;
        }

        public CheckAndFixRegels build() {
            return new CheckAndFixRegels(this);
        }
    }
}
