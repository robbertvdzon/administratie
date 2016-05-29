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
        factuurRegels:FactuurRegelData[];
        uuid:String;
        betaald:boolean;
        factuurDate:String;
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
    }

    export class Administratie {
        uuid:String;
        facturen:FactuurData[];
        adresboek:ContactData[];
        rekeningen:RekeningData[];
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

