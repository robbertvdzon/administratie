'use strict';

module Application.Model {

    export class GuiData {
        gebruikers:GebruikerData[];
        administratie:Administratie;
        huidigeGebruiker:GebruikerData;
    }

    export class GebruikerData {
        uuid:String;
        username:String;
        name:String;
        admin:boolean;
    }

    export class FactuurRegelData {
        uuid:String;
        omschrijving:String;
        aantal:number;
        stuksPrijs:number;
        btwPercentage:number;
    }

    export class FactuurData {
        factuurNummer:String;
        gekoppeldeBestellingNummer:String;
        factuurRegels:FactuurRegelData[];
        uuid:String;
        betaald:boolean;
        factuurDate:String;
        klant:ContactData;
        gekoppeldAfschrift:String;
    }

    export class BestellingRegelData {
        uuid:String;
        omschrijving:String;
        aantal:number;
        stuksPrijs:number;
        btwPercentage:number;
    }

    export class BestellingData {
        bestellingNummer:String;
        gekoppeldFactuurNummer:String;
        bestellingRegels:BestellingRegelData[];
        uuid:String;
        bestellingDate:String;
        klant:ContactData;
    }

    export class ContactData {
        naam:String;
        klantNummer:String;
        uuid:String;
        woonplaats:String;
        adres:String;
        postcode:String;
        land :String;
    }

    export class RekeningData {
        uuid:String;
        rekeningNummer:String;
        naam:String;
        omschrijving:String;
        rekeningDate:String;
        bedragExBtw:String;
        bedragIncBtw:String;
        btw:String;
        gekoppeldAfschrift:String;
    }

    export class DeclaratieData {
        uuid:String;
        declaratieNummer:String;
        omschrijving:String;
        declaratieDate:String;
        bedragExBtw:String;
        bedragIncBtw:String;
        btw:String;
    }

    export class AfschriftData {
        uuid:String;
        rekening:String;
        omschrijving:String;
        relatienaam:String;
        boekdatum:String;
        bedrag:String;
        boekingType:String;
        factuurNummer:String;
        rekeningNummer:String;
    }

    export class Administratie {
        uuid:String;
        facturen:FactuurData[];
        adresboek:ContactData[];
        rekeningen:RekeningData[];
        declaraties:DeclaratieData[];
        afschriften:AfschriftData[];
        bestellingen:BestellingData[];
        administratieGegevens:AdministratieGegevens;
    }

    export class AdministratieGegevens {
        uuid:String;
        name:String;
        rekeningNummer:String;
        btwNummer:String;
        handelsRegister:String;
        adres:String;
        postcode:String;
        woonplaats:String;
        logoUrl:String;
    }


}

