'use strict';

module Application.Services {

    import BestellingData = Application.Model.BestellingData;
    import ContactData = Application.Model.ContactData;
    import BestellingRegelData = Application.Model.BestellingRegelData;
    import Administratie = Application.Model.Administratie;
    import ContactDataService = Application.Services.ContactDataService;
    import BestellingGuiService = Application.Services.BestellingGuiService;


    export class BestellingDataService {

        constructor(private $rootScope, private $http, private dataService:Application.Services.MyDataservice, private contactDataService:ContactDataService, private $filter, private bestellingGuiService:BestellingGuiService, private factuurDataService:FactuurDataService) {
        }

        public setSelectedBestelling(bestelling:BestellingData) {
            this.bestellingGuiService.getBestellingGui().data.selectedbestelling = bestelling;
            this.bestellingGuiService.getBestellingGui().data.addMode = bestelling.uuid == null;
            this.resetBestellingToEdit();
        }

        private getSelectedBestelling():BestellingData {
            return this.bestellingGuiService.getBestellingGui().data.selectedbestelling;
        }

        private getSelectedBestellingRegel():BestellingRegelData {
            return this.bestellingGuiService.getBestellingGui().data.selectedbestellingregel;
        }

        private setSelectedBestellingRegel(selectedbestellingregel:BestellingRegelData) {
            this.bestellingGuiService.getBestellingGui().data.selectedbestellingregel = selectedbestellingregel;
            this.resetBestellingRegelToEdit();
        }

        public resetBestellingToEdit() {
            this.bestellingGuiService.getBestellingGui().data.bestellingToEdit = this.cloneBestelling(this.getSelectedBestelling());
        }

        public resetBestellingRegelToEdit() {
            this.bestellingGuiService.getBestellingGui().data.bestellingregelToEdit = this.cloneBestellingRegel(this.getSelectedBestellingRegel());
        }

        public setBestellingAsSelected(uuid) {
            var bestelling:BestellingData = this.getBestellingByUuid(uuid);
            if (bestelling != null) {
                bestelling = this.cloneBestelling(bestelling);
            }
            this.setSelectedBestelling(bestelling);
        }

        public updateContact(contact:ContactData):void {
            var contactClone = this.contactDataService.cloneContact(contact);
            contactClone.uuid = this.createUuid()
            this.getSelectedBestelling().klant = contactClone;
        }

        public maakFactuurVanBestelling() {
            this.factuurDataService.addFactuurFromBestelling(this.getSelectedBestelling()).then((response) => {
                this.dataService.reload().then((response2) => {
                    this.reloadBestelling();
                });
            });
        }

        public createAndSelectNewBestelling() {
            var bestelling:BestellingData = new BestellingData();
            bestelling.bestellingNummer = this.findNextBestellingnummer();
            bestelling.bestellingDate = this.$filter('date')(new Date(), 'dd-MM-yyyy');
            bestelling.bestellingRegels = [];
            bestelling.klant = new ContactData();
            bestelling.uuid = this.createUuid();
            this.setSelectedBestelling(bestelling);
            this.saveBestelling();
        }

        public getBestellingByUuid(uuid):BestellingData {
            var administratie:Administratie = this.dataService.getData().administratie;
            for (var i = 0; i < administratie.bestellingen.length; i++) {
                var bestelling:BestellingData = administratie.bestellingen[i];
                if (bestelling.uuid === uuid) {
                    return bestelling;
                }
            }
            return null;
        }

        public addBestellingRegel(bestellingregel:BestellingRegelData) {
            this.getSelectedBestelling().bestellingRegels.push(bestellingregel);
            this.saveBestelling();
        }

        public updateBestellingRegel(bestellingregel:BestellingRegelData) {
            for (var i = 0; i < this.getSelectedBestelling().bestellingRegels.length; i++) {
                var bestellingRegel = this.getSelectedBestelling().bestellingRegels[i];
                if (bestellingRegel.uuid === bestellingregel.uuid) {
                    bestellingRegel.aantal = bestellingregel.aantal;
                    bestellingRegel.omschrijving = bestellingregel.omschrijving;
                    bestellingRegel.btwPercentage = bestellingregel.btwPercentage;
                    bestellingRegel.stuksPrijs = bestellingregel.stuksPrijs;
                    bestellingRegel.uuid = bestellingregel.uuid;
                }
            }
            this.saveBestelling();
        }

        public deleteBestellingRegel(bestellingregel:BestellingRegelData) {
            var selectedNumber = -1;
            for (var i = 0; i < this.getSelectedBestelling().bestellingRegels.length; i++) {
                var bestellingRegel = this.getSelectedBestelling().bestellingRegels[i];
                if (bestellingRegel.uuid === bestellingregel.uuid) {
                    selectedNumber = i;
                }
            }
            if (selectedNumber >= 0) {
                this.getSelectedBestelling().bestellingRegels.splice(selectedNumber, 1);
            }
            this.saveBestelling();
        }

        public loadExistingBestellingRegel(selectedbestellingregel:BestellingRegelData) {
            this.setSelectedBestellingRegel(selectedbestellingregel);
            this.bestellingGuiService.getBestellingGui().data.addRegelMode = false;
            this.bestellingGuiService.showPage(SCREEN_BESTELLING_REGEL);
        }

        public loadNewBestellingRegel() {
            var nieuweRegel = new BestellingRegelData();
            nieuweRegel.omschrijving = "Werkzaamheden";
            nieuweRegel.aantal = 1;
            nieuweRegel.btwPercentage = 21;
            nieuweRegel.stuksPrijs = 72.5;
            nieuweRegel.uuid = this.createUuid();
            this.setSelectedBestellingRegel(nieuweRegel);
            this.bestellingGuiService.getBestellingGui().data.addRegelMode = true;
            this.bestellingGuiService.showPage(SCREEN_BESTELLING_REGEL);
        }

        public cloneBestelling(bestelling:BestellingData):BestellingData {
            var bestellingClone = new BestellingData();
            bestellingClone.uuid = bestelling.uuid;
            bestellingClone.bestellingNummer = bestelling.bestellingNummer;
            bestellingClone.bestellingRegels = bestelling.bestellingRegels;
            bestellingClone.gekoppeldFactuurNummer = bestelling.gekoppeldFactuurNummer;
            bestellingClone.bestellingDate = bestelling.bestellingDate;
            bestellingClone.klant = this.contactDataService.cloneContact(bestelling.klant);
            return bestellingClone;
        }

        public saveBestelling():ng.IPromise<any> {
            return this.$http({
                url: "/rest/bestelling/",
                method: "POST",
                data: this.getSelectedBestelling(),
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => {
                this.dataService.reload();
            });
        };

        public deleteBestelling():ng.IPromise<any> {
            return this.$http({
                url: "/rest/bestelling/" + this.getSelectedBestelling().uuid,
                method: "DELETE"
            }).then((response) => {
                this.dataService.reload();
            });
        };

        public addBestelling():ng.IPromise<any> {
            return this.$http({
                url: "/rest/bestelling/",
                method: "PUT",
                data: this.getSelectedBestelling(),
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => {
                this.dataService.reload();
            });
        };

        public findNextBestellingnummer():string {
            var administratie:Administratie = this.dataService.getData().administratie;
            var hoogste:number = 0;
            for (var i = 0; i < administratie.bestellingen.length; i++) {
                var bestellingNr:number = parseInt(String(administratie.bestellingen[i].bestellingNummer), 10);
                if (bestellingNr > hoogste) {
                    hoogste = bestellingNr;
                }
            }
            var nieuwNummer = hoogste + 1;
            return "" + nieuwNummer;
        }

        public getRegelByUuid(bestelling:BestellingData, uuid:String):BestellingRegelData {
            for (var i = 0; i < bestelling.bestellingRegels.length; i++) {
                var bestellingRegel:BestellingRegelData = bestelling.bestellingRegels[i];
                if (bestellingRegel.uuid === uuid) {
                    return bestellingRegel;
                }
            }
            return null;
        }

        public cloneBestellingRegel(regel:BestellingRegelData):BestellingRegelData {
            if (regel == null) {
                return null;
            }
            var clonedRegel = new BestellingRegelData();
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

        public copyContactFromSelectedBestellingToAdresboek() {
            var nextNr = this.contactDataService.findNextKlantnummer();
            this.getSelectedBestelling().klant.klantNummer = nextNr;
            this.getSelectedBestelling().klant.uuid = this.createUuid();
            this.contactDataService.addNewContact(this.getSelectedBestelling().klant);
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

        private reloadBestelling():void {
            this.setBestellingAsSelected(this.getSelectedBestelling().uuid);
        }
    }
}


angular.module('mswFrontendApp').service('bestellingDataService', Application.Services.BestellingDataService);
