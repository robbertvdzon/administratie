'use strict';

module Application.Model {

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

    export class Administratie {
        name:String;
        uuid:String;
        facturen:FactuurData[];
        adresboek:ContactData[];

    }

}

