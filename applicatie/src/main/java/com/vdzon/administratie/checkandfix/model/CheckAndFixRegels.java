package com.vdzon.administratie.checkandfix.model;

import java.util.List;

public class CheckAndFixRegels {
    private List<CheckAndFixRegel> checkAndFixRegels;

    public CheckAndFixRegels() {
    }

    public CheckAndFixRegels(List<CheckAndFixRegel> checkAndFixRegels) {
        this.checkAndFixRegels = checkAndFixRegels;
    }

    public List<CheckAndFixRegel> getCheckAndFixRegels() {
        return checkAndFixRegels;
    }
}
