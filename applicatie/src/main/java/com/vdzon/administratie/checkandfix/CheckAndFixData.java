package com.vdzon.administratie.checkandfix;

import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Rekening;

import java.util.List;
import java.util.Map;

public class CheckAndFixData {
    public List<Afschrift> alleAfschriften = null;
    public List<Rekening> alleRekeningen = null;
    public List<Factuur> alleFacturen = null;
    public Map<String, Afschrift> afschriftMap = null;
    public Map<String, Rekening> rekeningMap = null;
    public Map<String, Factuur> factuurMap = null;

}
