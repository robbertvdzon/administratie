package com.vdzon.administratie.rubriceren.rubriceerRegels;

import com.vdzon.administratie.dto.AfschriftDto;
import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.BoekingenCache;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.model.Rekening;
import com.vdzon.administratie.model.boekingen.BetaaldeRekeningBoeking;
import com.vdzon.administratie.model.boekingen.BetalingZonderFactuurBoeking;
import com.vdzon.administratie.model.boekingen.Boeking;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift;
import com.vdzon.administratie.rubriceren.model.RubriceerAction;
import com.vdzon.administratie.rubriceren.model.RubriceerRegel;

import java.util.List;
import java.util.UUID;

public class RubriceerRekeningRegels extends RubriceerHelper {

    //TODO: deze class kan nog steeds mooier

    @RubriceerRule
    public void updateRegels(Gebruiker gebruiker, List<RubriceerRegel> regels, Afschrift afschrift, BoekingenCache boekingenCache) {
        List<BoekingMetAfschrift> boekingenVanAfschrift = boekingenCache.getBoekingenVanAfschrift(afschrift.getNummer());
        if (hasNoBoekingen(boekingenVanAfschrift)) {
            if (afschrift.getBedrag() < 0) {
                RubriceerAction rubriceerAction = RubriceerAction.BETALING_ZONDER_FACTUUR;
                String factuurNummer = null;
                String rekeningNummer = null;
                for (Rekening rekening : gebruiker.getDefaultAdministratie().getRekeningen()) {
                    if (boekingenCache.getBoekingenVanRekening(rekening.getRekeningNummer()).isEmpty()
                            &&
                            !rekeningAlreadyUsed(regels, rekening.getRekeningNummer())
                            &&
                            (rekening.getBedragIncBtw() == afschrift.getBedrag() * -1)
                            &&
                            (
                                    (afschrift.getOmschrijving().contains(rekening.getRekeningNummer()))
                                            ||
                                    (afschrift.getOmschrijving().equals(rekening.getOmschrijving()))
                            )
                        )

                    {
                        rubriceerAction = RubriceerAction.CONNECT_EXISTING_REKENING;
                        rekeningNummer = rekening.getRekeningNummer();
                    }
                }
                RubriceerRegel rubriceerRegel = RubriceerRegel.newBuilder()
                        .rubriceerAction(rubriceerAction)
                        .rekeningNummer(rekeningNummer)
                        .faktuurNummer(factuurNummer)
                        .afschrift(new AfschriftDto(afschrift, boekingenCache))
                        .build();
                regels.add(rubriceerRegel);
            }
        }
    }

    private boolean rekeningAlreadyUsed(List<RubriceerRegel> regels, String rekeningNummer) {
        return regels.stream().filter(regel -> rekeningNummer.equals(regel.getRekeningNummer())).count() != 0;
    }

    private boolean hasNoBoekingen(List<BoekingMetAfschrift> boekingenVanAfschrift) {
        return boekingenVanAfschrift == null || boekingenVanAfschrift.isEmpty();
    }

    @RubriceerRuleCommit
    public void processRegel(RubriceerRegel regel, Gebruiker gebruiker) {
        Afschrift afschrift = regel.getAfschrift().toAfschrift();
        Boeking boeking;
        switch (regel.getRubriceerAction()) {
            case BETALING_ZONDER_FACTUUR:
                boeking = BetalingZonderFactuurBoeking.newBuilder()
                        .uuid(UUID.randomUUID().toString())
                        .afschriftNummer(regel.getAfschrift().getNummer())
                        .build();
                gebruiker.getDefaultAdministratie().addBoeking(boeking);
                break;
            case CONNECT_EXISTING_REKENING:
                boeking = BetaaldeRekeningBoeking.newBuilder()
                        .uuid(UUID.randomUUID().toString())
                        .afschriftNummer(regel.getAfschrift().getNummer())
                        .rekeningNummer(regel.getRekeningNummer())
                        .build();
                gebruiker.getDefaultAdministratie().addBoeking(boeking);
                break;
            case CREATE_REKENING:
                Rekening rekening = Rekening
                        .newBuilder()
                        .uuid(UUID.randomUUID().toString())
                        .rekeningNummer("" + findNextRekeningNummer(gebruiker))
                        .naam(afschrift.getRelatienaam())
                        .omschrijving(afschrift.getOmschrijving())
                        .rekeningDate(afschrift.getBoekdatum())
                        .bedragExBtw(afschrift.getBedrag() * -1)
                        .bedragIncBtw(afschrift.getBedrag() * -1)
                        .btw(0)
                        .build();
                gebruiker.getDefaultAdministratie().addRekening(rekening);

                boeking = BetaaldeRekeningBoeking.newBuilder()
                        .uuid(UUID.randomUUID().toString())
                        .afschriftNummer(regel.getAfschrift().getNummer())
                        .rekeningNummer(rekening.getRekeningNummer())
                        .build();
                gebruiker.getDefaultAdministratie().addBoeking(boeking);
                break;
        }
    }

}
