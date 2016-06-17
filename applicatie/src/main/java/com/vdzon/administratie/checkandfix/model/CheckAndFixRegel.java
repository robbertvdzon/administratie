package com.vdzon.administratie.checkandfix.model;

import com.vdzon.administratie.dto.AfschriftDto;
import lombok.*;

import java.time.LocalDate;

@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class CheckAndFixRegel {
    private FixAction rubriceerAction;
    private String omschrijving;
    private String data;
    private CheckType checkType;
    private AfschriftDto afschrift;
    private LocalDate date;
}
