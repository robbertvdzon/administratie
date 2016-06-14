package com.vdzon.administratie.checkandfix.model;

import lombok.*;

import java.util.List;

@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class CheckAndFixRegels {
    private List<CheckAndFixRegel> checkAndFixRegels;

}
