package com.vdzon.administratie.rubriceren.model;

import com.vdzon.administratie.dto.AfschriftDto;
import lombok.*;

@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
public class RubriceerRegel {
    private RubriceerAction rubriceerAction;
    private String rekeningNummer;
    private String faktuurNummer;
    private AfschriftDto afschrift;

}
