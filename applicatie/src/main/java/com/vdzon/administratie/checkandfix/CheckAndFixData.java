package com.vdzon.administratie.checkandfix;

import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.BoekingenCache;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Rekening;
import com.vdzon.administratie.model.boekingen.Boeking;

import java.util.List;
import java.util.Map;

public class CheckAndFixData {
    public List<Afschrift> alleAfschriften = null;
    public List<Rekening> alleRekeningen = null;
    public List<Factuur> alleFacturen = null;
    public List<Boeking> alleBoekingen = null;
    public BoekingenCache boekingenCache = null;

}
