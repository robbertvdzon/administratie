'use strict';

module Application.Services {

    import FactuurData = Application.Model.FactuurData;
    import BestellingData = Application.Model.BestellingData;
    import ContactData = Application.Model.ContactData;
    import FactuurRegelData = Application.Model.FactuurRegelData;
    import Administratie = Application.Model.Administratie;
    import ContactDataService = Application.Services.ContactDataService;
    import FactuurGuiService = Application.Services.FactuurGuiService;
    import BoekingData = Application.Model.BoekingData;

    export class FactuurDataService {

        constructor(private $rootScope, private $http, private dataService:Application.Services.MyDataservice, private contactDataService:ContactDataService, private $filter, private factuurGuiService:FactuurGuiService) {
        }

        public setSelectedFactuur(factuur:FactuurData) {
            this.factuurGuiService.getFactuurGui().data.selectedfactuur = factuur;
            this.factuurGuiService.getFactuurGui().data.addMode = factuur.uuid == null;
            this.resetFactuurToEdit();
        }

        private getSelectedFactuur():FactuurData{
            return this.factuurGuiService.getFactuurGui().data.selectedfactuur;
        }

        private getSelectedFactuurRegel():FactuurRegelData{
            return this.factuurGuiService.getFactuurGui().data.selectedfactuurregel;
        }

        private setSelectedFactuurRegel(selectedfactuurregel:FactuurRegelData){
            this.factuurGuiService.getFactuurGui().data.selectedfactuurregel = selectedfactuurregel;
            this.resetFactuurRegelToEdit();
        }

        public resetFactuurToEdit() {
            this.factuurGuiService.getFactuurGui().data.factuurToEdit = this.cloneFactuur(this.getSelectedFactuur());
        }

        public resetFactuurRegelToEdit() {
            this.factuurGuiService.getFactuurGui().data.factuurregelToEdit = this.cloneFactuurRegel(this.getSelectedFactuurRegel());
        }

        public setFactuurAsSelected(uuid) {
            var factuur:FactuurData = this.getFactuurByUuid(uuid);
            if (factuur != null) {
                factuur = this.cloneFactuur(factuur);
            }
            this.setSelectedFactuur(factuur);
        }

        public updateContact(contact:ContactData):void {
            var contactClone = this.contactDataService.cloneContact(contact);
            contactClone.uuid = this.createUuid()
            this.getSelectedFactuur().klant = contactClone;
        }

        public createAndSelectNewFactuur() {
            var factuur:FactuurData = new FactuurData();
            factuur.factuurNummer = this.findNextFactuurnummer();
            factuur.factuurDate = this.$filter('date')(new Date(), 'dd-MM-yyyy');
            factuur.factuurRegels = [];
            factuur.klant = new ContactData();
            factuur.uuid = this.createUuid();
            this.setSelectedFactuur(factuur);
            this.saveFactuur();
        }

        public getFactuurByUuid(uuid):FactuurData {
            var administratie:Administratie = this.dataService.getData().administratie;
            for (var i = 0; i < administratie.facturen.length; i++) {
                var factuur:FactuurData = administratie.facturen[i];
                if (factuur.uuid === uuid) {
                    return factuur;
                }
            }
            return null;
        }

        public addFactuurRegel(factuurregel:FactuurRegelData) {
            this.getSelectedFactuur().factuurRegels.push(factuurregel);
            this.saveFactuur();
        }

        public updateFactuurRegel(factuurregel:FactuurRegelData) {
            for (var i = 0; i < this.getSelectedFactuur().factuurRegels.length; i++) {
                var factuurRegel = this.getSelectedFactuur().factuurRegels[i];
                if (factuurRegel.uuid === factuurregel.uuid) {
                    factuurRegel.aantal = factuurregel.aantal;
                    factuurRegel.omschrijving = factuurregel.omschrijving;
                    factuurRegel.btwPercentage = factuurregel.btwPercentage;
                    factuurRegel.stuksPrijs = factuurregel.stuksPrijs;
                    factuurRegel.uuid = factuurregel.uuid;
                }
            }
            this.saveFactuur();
        }

        public deleteFactuurRegel(factuurregel:FactuurRegelData) {
            var selectedNumber = -1;
            for (var i = 0; i < this.getSelectedFactuur().factuurRegels.length; i++) {
                var factuurRegel = this.getSelectedFactuur().factuurRegels[i];
                if (factuurRegel.uuid === factuurregel.uuid) {
                    selectedNumber = i;
                }
            }
            if (selectedNumber >= 0) {
                this.getSelectedFactuur().factuurRegels.splice(selectedNumber, 1);
            }
            this.saveFactuur();
        }

        public loadExistingFactuurRegel(selectedfactuurregel : FactuurRegelData) {
            this.setSelectedFactuurRegel(selectedfactuurregel);
            this.factuurGuiService.getFactuurGui().data.addRegelMode = false;
            this.factuurGuiService.showPage(SCREEN_FACTUUR_REGEL);
        }

        public loadNewFactuurRegel() {
            var nieuweRegel = new FactuurRegelData();
            nieuweRegel.omschrijving = "Werkzaamheden";
            nieuweRegel.aantal = 1;
            nieuweRegel.btwPercentage = 21;
            nieuweRegel.stuksPrijs = 0;
            nieuweRegel.uuid = this.createUuid();
            this.setSelectedFactuurRegel(nieuweRegel);
            this.factuurGuiService.getFactuurGui().data.addRegelMode = true;
            this.factuurGuiService.showPage(SCREEN_FACTUUR_REGEL);
        }

        public cloneFactuur(factuur:FactuurData):FactuurData {
            var factuurClone = new FactuurData();
            factuurClone.uuid = factuur.uuid;
            factuurClone.factuurNummer = factuur.factuurNummer;
            factuurClone.factuurRegels = factuur.factuurRegels;
            factuurClone.factuurDate = factuur.factuurDate;
            factuurClone.gekoppeldeBestellingNummer = factuur.gekoppeldeBestellingNummer;
            factuurClone.klant = this.contactDataService.cloneContact(factuur.klant);
            factuurClone.boekingen = [];
            if (!angular.isUndefined(factuur.boekingen)) {
                for (var i = 0; i < factuur.boekingen.length; i++) {
                    var boeking:BoekingData = factuur.boekingen[i];
                    factuurClone.boekingen.push(this.cloneBoeking(boeking));
                }
            }
            return factuurClone;
        }


        public cloneBoeking(boeking:BoekingData):BoekingData{
            if (boeking==null) return null;
            var boekingClone = new BoekingData();
            boekingClone.uuid = boeking.uuid;
            boekingClone.afschriftNummer = boeking.afschriftNummer;
            boekingClone.factuurNummer = boeking.factuurNummer;
            boekingClone.omschrijving = boeking.omschrijving;
            boekingClone.rekeningNummer = boeking.rekeningNummer;
            return boekingClone;
        }

        public addFactuurFromBestelling(bestelling:BestellingData):ng.IPromise<any> {
            var factuur = new FactuurData();
            factuur.uuid = bestelling.uuid;
            factuur.factuurNummer = this.findNextFactuurnummer();
            factuur.factuurRegels = bestelling.bestellingRegels;
            factuur.factuurDate = this.$filter('date')(new Date(), 'dd-MM-yyyy');
            factuur.gekoppeldeBestellingNummer = bestelling.bestellingNummer;
            factuur.klant = this.contactDataService.cloneContact(bestelling.klant);
            return this.addFactuur(factuur);
        };



        public saveFactuur():ng.IPromise<any> {
            return this.$http({
                url: "/rest/factuur/",
                method: "POST",
                data: this.getSelectedFactuur(),
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => {
                this.dataService.reload();
            });
        };

        public deleteFactuur():ng.IPromise<any> {
            return this.$http({
                url: "/rest/factuur/" + this.getSelectedFactuur().uuid,
                method: "DELETE"
            }).then((response) => {
                this.dataService.reload();
            });
        };


        public addSelectedFactuur():ng.IPromise<any> {
            return this.addFactuur(this.getSelectedFactuur());
        };

        public addFactuur(factuur:FactuurData):ng.IPromise<any> {
            return this.$http({
                url: "/rest/factuur/",
                method: "PUT",
                data: factuur,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            });
        };

        public findNextFactuurnummer():string {
            var administratie:Administratie = this.dataService.getData().administratie;
            var hoogste:number = 0;
            for (var i = 0; i < administratie.facturen.length; i++) {
                var factuurNr:number = parseInt(String(administratie.facturen[i].factuurNummer), 10);
                if (factuurNr > hoogste) {
                    hoogste = factuurNr;
                }
            }
            var nieuwNummer = hoogste + 1;
            return "" + nieuwNummer;
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

        public cloneFactuurRegel(regel:FactuurRegelData):FactuurRegelData {
            if (regel == null) {
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

        public getContactByUuid(uuid:String):ContactData {
            var administratie:Administratie = this.dataService.getData().administratie;
            for (var i = 0; i < administratie.adresboek.length; i++) {
                var contact:ContactData = administratie.adresboek[i];
                if (contact.uuid === uuid) {
                    return contact;
                }
            }
            return null;
        }

        public copyContactFromSelectedFactuurToAdresboek(){
            var nextNr = this.contactDataService.findNextKlantnummer();
            this.getSelectedFactuur().klant.klantNummer = nextNr;
            this.getSelectedFactuur().klant.uuid= this.createUuid();
            this.contactDataService.addNewContact(this.getSelectedFactuur().klant);
        }

        public createUuid():String {
            var d = new Date().getTime();
            var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                var r = (d + Math.random() * 16) % 16 | 0;
                d = Math.floor(d / 16);
                return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
            });
            return uuid;
        };
    }
}


angular.module('mswFrontendApp').service('factuurDataService', Application.Services.FactuurDataService);
