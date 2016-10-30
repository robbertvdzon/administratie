package com.vdzon.administratie.checkandfix;


import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.model.Administratie;

@FunctionalInterface
public interface FixFunction {
    public Administratie fixAfschrift(CheckAndFixRegel regel, Administratie administratie);
}
