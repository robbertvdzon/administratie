package com.vdzon.administratie.model.boekingen;

import com.vdzon.administratie.model.Boeking;
import com.vdzon.administratie.model.boekingen.relaties.HasAfschrift;
import com.vdzon.administratie.model.boekingen.relaties.HasFactuur;
import lombok.*;
import org.mongodb.morphia.annotations.Id;

@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
public class BetaaldeFactuurBoeking extends Boeking implements HasFactuur, HasAfschrift {
    String factuurUuid;
    String afschriftUuid;
    @Id
    private String uuid;

    @Override
    public String getFactuurUuid() {
        return factuurUuid;
    }

    @Override
    public String getAfschriftUuid() {
        return afschriftUuid;
    }
}
