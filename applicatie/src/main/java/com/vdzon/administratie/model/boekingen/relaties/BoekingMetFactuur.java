package com.vdzon.administratie.model.boekingen.relaties;

import com.vdzon.administratie.model.Factuur;

import java.util.List;

public interface BoekingMetFactuur extends BoekingMetAfschrift{
    String getUuid();
    String getFactuurNummer();
}
