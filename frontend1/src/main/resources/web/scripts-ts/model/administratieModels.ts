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
        factuurDate:String;
        klant:ContactData;
        boekingen:BoekingData[];
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
        tenNameVan:String;
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
        factuurNummer:String;
        naam:String;
        omschrijving:String;
        rekeningDate:String;
        bedragExBtw:String;
        bedragIncBtw:String;
        btw:String;
        maandenAfschrijving:Number;
        boekingen:BoekingData[];
    }

    export class BoekingData {
        uuid:String
        omschrijving:String;
        factuurNummer:String;
        rekeningNummer:String;
        afschriftNummer:String;
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
        nummer:String;
        rekening:String;
        omschrijving:String;
        relatienaam:String;
        boekdatum:String;
        bedrag:String;
        boekingen:BoekingData[];
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
        rekeningNaam:String;
        btwNummer:String;
        handelsRegister:String;
        adres:String;
        postcode:String;
        woonplaats:String;
        logoUrl:String;
        betalingstermijn:Number;
    }

    export class RubriceerRegels {
        rubriceerRegelList:RubriceerRegel[];
    }

    export class RubriceerRegel {
        rubriceerAction:String;
        afschrift:AfschriftData;
        rekeningNummer:String;
        faktuurNummer:String;
    }

    export class CheckAndFixRegels {
        checkAndFixRegelList:CheckAndFixRegel[];
    }

    export class CheckAndFixRegel {
        rubriceerAction:String;
        checkType:String;
        afschift:AfschriftData;
        omschrijving:String;
        data:String;
    }

}

