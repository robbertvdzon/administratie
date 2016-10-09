package com.vdzon.administratie.model.boekingen;

import com.vdzon.administratie.model.Boeking;
import com.vdzon.administratie.model.boekingen.relaties.HasDeclaratie;
import com.vdzon.administratie.model.boekingen.relaties.HasFactuur;
import lombok.*;
import org.mongodb.morphia.annotations.Id;

@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
public class DeclaratieBoeking extends Boeking implements HasDeclaratie {
    String declaratieUuid;
    @Id
    private String uuid;

    @Override
    public String getDeclaratieUuid() {
        return declaratieUuid;
    }
}
