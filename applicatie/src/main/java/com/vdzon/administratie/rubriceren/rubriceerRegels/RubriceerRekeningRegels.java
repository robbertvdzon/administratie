package com.vdzon.administratie.rubriceren.rubriceerRegels;

import com.vdzon.administratie.dto.AfschriftDto;
import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.BoekingType;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.model.Rekening;
import com.vdzon.administratie.rubriceren.model.RubriceerAction;
import com.vdzon.administratie.rubriceren.model.RubriceerRegel;

import java.util.List;
import java.util.UUID;

public class RubriceerRekeningRegels extends RubriceerHelper {

    //TODO: deze class kan nog steeds mooier

    @RubriceerRule
    public void updateRegels(Gebruiker gebruiker, List<RubriceerRegel> regels, Afschrift afschrift) {
        if (afschrift.getBoekingType() == null || afschrift.getBoekingType() == BoekingType.NONE) {
            if (afschrift.getBedrag() < 0) {
                RubriceerAction rubriceerAction = RubriceerAction.CREATE_REKENING;
                String factuurNummer = null;
                String rekeningNummer = null;
                for (Rekening rekening : gebruiker.getDefaultAdministratie().getRekeningen()) {
                    if ((rekening.getBedragIncBtw() == afschrift.getBedrag() * -1) &&
                            (
                                    (afschrift.getOmschrijving().contains(rekening.getRekeningNummer()))) ||
                            (afschrift.getOmschrijving().equals(rekening.getOmschrijving()))
                            )

                    {
                        rubriceerAction = RubriceerAction.CONNECT_EXISTING_REKENING;
                        rekeningNummer = rekening.getRekeningNummer();
                    }
                }
                RubriceerRegel rubriceerRegel = RubriceerRegel.builder().rubriceerAction(rubriceerAction).rekeningNummer(rekeningNummer).faktuurNummer(factuurNummer).afschrift(new AfschriftDto(afschrift)).build();
                regels.add(rubriceerRegel);
            }
        }
    }

    @RubriceerRuleCommit
    public void processRegel(RubriceerRegel regel, Gebruiker gebruiker) {
        Afschrift afschrift = regel.getAfschrift().toAfschrift();
        switch (regel.getRubriceerAction()) {
            case CONNECT_EXISTING_REKENING:
                for (Rekening rekening : gebruiker.getDefaultAdministratie().getRekeningen()) {
                    if (regel.getRekeningNummer().equals(rekening.getRekeningNummer())) {
                        Rekening newRekening = rekening.toBuilder().gekoppeldAfschrift(afschrift.getNummer()).build();
                        gebruiker.getDefaultAdministratie().removeRekening(rekening.getUuid());
                        gebruiker.getDefaultAdministratie().addRekening(newRekening);
                        gebruiker.getDefaultAdministratie().removeAfschrift(afschrift.getNummer());
                        gebruiker.getDefaultAdministratie().addAfschrift(
                                afschrift
                                        .toBuilder()
                                        .boekingType(BoekingType.REKENING)
                                        .factuurNummer("")
                                        .rekeningNummer(rekening.getRekeningNummer())
                                        .build()
                        );
                    }
                }
                break;
            case CREATE_REKENING:
                Rekening rekening = Rekening
                        .builder()
                        .uuid(UUID.randomUUID().toString())
                        .rekeningNummer("" + findNextRekeningNummer(gebruiker)
                        ).naam(afschrift.getRelatienaam())
                        .omschrijving(afschrift.getOmschrijving())
                        .rekeningDate(afschrift.getBoekdatum())
                        .bedragExBtw(afschrift.getBedrag() * -1)
                        .bedragIncBtw(afschrift.getBedrag() * -1)
                        .btw(0)
                        .gekoppeldAfschrift(regel.getAfschrift().getNummer())
                        .build();


                gebruiker.getDefaultAdministratie().addRekening(rekening);
                gebruiker.getDefaultAdministratie().removeAfschrift(afschrift.getNummer());
                gebruiker.getDefaultAdministratie().addAfschrift(
                        afschrift
                                .toBuilder()
                                .boekingType(BoekingType.REKENING)
                                .factuurNummer("")
                                .rekeningNummer(rekening.getRekeningNummer())
                                .build()
                );

                break;
        }
    }

}
