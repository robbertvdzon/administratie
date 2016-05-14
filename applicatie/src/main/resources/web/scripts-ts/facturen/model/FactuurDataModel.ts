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
    }

    export class Administratie {
        name:String;
        uuid:String;
        facturen:FactuurData[];

    }

    //export class Gebruiker {
    //    name:String;
    //    username:String;
    //    password:String;
    //    uuid:String;
    //    administraties:Administratie[];
    //
    //}
}

