'use strict';

module Application.Services {

    import FactuurData = Application.Model.FactuurData;
    import ContactData = Application.Model.ContactData;
    import FactuurRegelData = Application.Model.FactuurRegelData;
    import Administratie = Application.Model.Administratie;
    import ContactDataService = Application.Services.ContactDataService;


    export class FactuurDataService {


        constructor(private $rootScope,private $http, private dataService:Application.Services.MyDataservice, private contactDataService:ContactDataService) {
        }

        public getFactuurByUuid(uuid):FactuurData {
            var administratie:Administratie = this.dataService.getData();
            for (var i = 0; i < administratie.facturen.length; i++) {
                var factuur:FactuurData = administratie.facturen[i];
                if (factuur.uuid === uuid) {
                    return factuur;
                }
            }
            return null;
        }

        public cloneFactuur(factuur:FactuurData):FactuurData {
            var factuurClone = new FactuurData();
            factuurClone.uuid = factuur.uuid;
            factuurClone.factuurNummer = factuur.factuurNummer;
            factuurClone.factuurRegels = factuur.factuurRegels;
            factuurClone.betaald = factuur.betaald;
            factuurClone.factuurDate = factuur.factuurDate;
            factuurClone.klant = this.contactDataService.cloneContact(factuur.klant);
            return factuurClone;
        }


        public saveFactuur(factuur:FactuurData): ng.IPromise<any> {
            return this.$http({
                url: "/rest/factuur/",
                method: "POST",
                data: factuur,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => {
                this.dataService.reload();
            });
        };

        public deleteFactuur(factuur:FactuurData): ng.IPromise<any> {
            return this.$http({
                url: "/rest/factuur/" + factuur.uuid,
                method: "DELETE"
            }).then((response) => {
                this.dataService.reload();
            });
        };

        public addFactuur(factuur:FactuurData): ng.IPromise<any> {
            return this.$http({
                url: "/rest/factuur/",
                method: "PUT",
                data: factuur,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => {
                this.dataService.reload();
            });
        };

// Util services
        public findNextFactuurnummer():string {
            var administratie:Administratie = this.dataService.getData();
            var hoogste:number = 0;
            for (var i = 0; i < administratie.facturen.length; i++) {
                var factuurNr:number = parseInt(String(administratie.facturen[i].factuurNummer),10);
                if (factuurNr>hoogste){
                    hoogste = factuurNr;
                }
            }
            var nieuwNummer = hoogste+1;
            return ""+nieuwNummer;
        }

        public getRegelByUuid(factuur:FactuurData, uuid:String):FactuurRegelData {
            for (var i = 0; i < factuur.factuurRegels.length; i++) {
                var factuurRegel:FactuurRegelData = factuur.factuurRegels[i];
                if (factuurRegel.uuid === uuid) {
                    return factuurRegel;
                }
            }
            return null;
        }

        public cloneFactuurRegel(regel:FactuurRegelData):FactuurRegelData{
            if (regel == null){
                return null;
            }
            var clonedRegel = new FactuurRegelData();
            clonedRegel.aantal = regel.aantal;
            clonedRegel.omschrijving = regel.omschrijving;
            clonedRegel.btwPercentage = regel.btwPercentage;
            clonedRegel.stuksPrijs = regel.stuksPrijs;
            clonedRegel.uuid = regel.uuid;
            return clonedRegel;
        }

        updateFactuurRegel(factuur:FactuurData, factuurregel:FactuurRegelData){
            for (var i = 0; i < factuur.factuurRegels.length; i++) {
                var factuurRegel = factuur.factuurRegels[i];
                if (factuurRegel.uuid === factuurregel.uuid) {
                    factuurRegel.aantal = factuurregel.aantal;
                    factuurRegel.omschrijving = factuurregel.omschrijving;
                    factuurRegel.btwPercentage = factuurregel.btwPercentage;
                    factuurRegel.stuksPrijs = factuurregel.stuksPrijs;
                    factuurRegel.uuid = factuurregel.uuid;
                }
            }
        }

        deleteFactuurRegel(factuur:FactuurData, factuurregel:FactuurRegelData) {
            var selectedNumber = -1;
            for (var i = 0; i < factuur.factuurRegels.length; i++) {
                var factuurRegel = factuur.factuurRegels[i];
                if (factuurRegel.uuid === factuurregel.uuid) {
                    selectedNumber = i;
                }
            }
            if (selectedNumber >= 0) {
                factuur.factuurRegels.splice(selectedNumber, 1);
            }
        }


        getContactByUuid(uuid:String):ContactData {
            var administratie:Administratie = this.dataService.getData();
            for (var i = 0; i < administratie.adresboek.length; i++) {
                var contact:ContactData = administratie.adresboek[i];
                if (contact.uuid === uuid) {
                    return contact;
                }
            }
            return null;
        }
    }
}


angular.module('mswFrontendApp').service('factuurDataService', Application.Services.FactuurDataService);
