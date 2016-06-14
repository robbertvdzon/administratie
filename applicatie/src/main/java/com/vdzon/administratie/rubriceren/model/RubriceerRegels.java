package com.vdzon.administratie.rubriceren.model;

import lombok.*;

import java.util.List;

@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class RubriceerRegels {
    private List<RubriceerRegel> rubriceerRegelList;
}
