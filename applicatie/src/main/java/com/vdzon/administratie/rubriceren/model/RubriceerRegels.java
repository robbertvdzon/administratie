package com.vdzon.administratie.rubriceren.model;

import java.util.List;

public class RubriceerRegels {
    private List<RubriceerRegel> rubriceerRegelList;

    public RubriceerRegels() {
    }

    private RubriceerRegels(Builder builder) {
        rubriceerRegelList = builder.rubriceerRegelList;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(RubriceerRegels copy) {
        Builder builder = new Builder();
        builder.rubriceerRegelList = copy.rubriceerRegelList;
        return builder;
    }

    public List<RubriceerRegel> getRubriceerRegelList() {
        return rubriceerRegelList;
    }

    public static final class Builder {
        private List<RubriceerRegel> rubriceerRegelList;

        private Builder() {
        }

        public Builder rubriceerRegelList(List<RubriceerRegel> val) {
            rubriceerRegelList = val;
            return this;
        }

        public RubriceerRegels build() {
            return new RubriceerRegels(this);
        }
    }
}
