package com.vdzon.administratie.rubriceren.model;

import com.vdzon.administratie.dto.AfschriftDto;
import com.vdzon.administratie.model.Afschrift;
import lombok.*;

@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class RubriceerRegel {
    private RubriceerAction rubriceerAction;
    private String rekeningNummer;
    private String faktuurNummer;
    private AfschriftDto afschrift;

}
