package com.vdzon.administratie.checkandfix;


import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;

import java.util.Collection;

@FunctionalInterface
public interface CheckFunction {
    public Collection<? extends CheckAndFixRegel> checkFunction(CheckAndFixData data) ;
}
