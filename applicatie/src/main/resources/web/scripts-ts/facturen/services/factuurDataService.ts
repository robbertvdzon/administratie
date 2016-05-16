'use strict';

module Application.Services {

    import FactuurData = Application.Model.FactuurData;
    import ContactData = Application.Model.ContactData;
    import FactuurRegelData = Application.Model.FactuurRegelData;
    import Administratie = Application.Model.Administratie;
    import ContactDataService = Application.Services.ContactDataService;
    import FactuurGuiService = Application.Services.FactuurGuiService;



    export class FactuurDataService {
        selectedFactuur:FactuurData;

        constructor(private $rootScope, private $http, private dataService:Application.Services.MyDataservice, private contactDataService:ContactDataService, private $filter, private factuurGuiService:FactuurGuiService) {
        }

        setSelectedFactuur(factuur:FactuurData) {
            // TODO: de selected factuur alleen in GuiObject en niet nog apart
            this.selectedFactuur = factuur;

            // TODO : onderstaande is voor editFactuur: kan dat algemener?
            this.factuurGuiService.getFactuurGui().data.selectedfactuur = factuur;
            this.factuurGuiService.getFactuurGui().data.factuurToEdit = this.cloneFactuur(factuur);
            this.factuurGuiService.getFactuurGui().data.addMode = factuur.uuid == null;

        }

        setFactuurAsSelected(uuid) {
            var factuur:FactuurData = this.getFactuurByUuid(uuid);
            if (factuur != null) {
                factuur = this.cloneFactuur(factuur);
            }
            this.setSelectedFactuur(factuur);
            //this.$rootScope.$broadcast('new_selected_factuur_available', factuur);
        }

        public updateContact(contact:ContactData):void {
            var contactClone = this.contactDataService.cloneContact(contact);
            contactClone.uuid = this.createUuid()
            this.selectedFactuur.klant = contactClone;
        }


        createAndSelectNewFactuur() {
            var factuur:FactuurData = new FactuurData();
            factuur.factuurNummer = this.findNextFactuurnummer();
            factuur.factuurDate = this.$filter('date')(new Date(), 'dd-MM-yyyy');
            factuur.uuid = this.createUuid();
            this.setSelectedFactuur(factuur);
            //this.$rootScope.$broadcast('new_selected_factuur_available', factuur);
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

        addFactuurRegel(factuurregel:FactuurRegelData) {
            this.selectedFactuur.factuurRegels.push(factuurregel);
        }

        updateFactuurRegel(factuurregel:FactuurRegelData) {
            for (var i = 0; i < this.selectedFactuur.factuurRegels.length; i++) {
                var factuurRegel = this.selectedFactuur.factuurRegels[i];
                if (factuurRegel.uuid === factuurregel.uuid) {
                    factuurRegel.aantal = factuurregel.aantal;
                    factuurRegel.omschrijving = factuurregel.omschrijving;
                    factuurRegel.btwPercentage = factuurregel.btwPercentage;
                    factuurRegel.stuksPrijs = factuurregel.stuksPrijs;
                    factuurRegel.uuid = factuurregel.uuid;
                }
            }

        }

        deleteFactuurRegel(factuurregel:FactuurRegelData) {
            var selectedNumber = -1;
            for (var i = 0; i < this.selectedFactuur.factuurRegels.length; i++) {
                var factuurRegel = this.selectedFactuur.factuurRegels[i];
                if (factuurRegel.uuid === factuurregel.uuid) {
                    selectedNumber = i;
                }
            }
            if (selectedNumber >= 0) {
                this.selectedFactuur.factuurRegels.splice(selectedNumber, 1);
            }
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

        public saveFactuur(factuur:FactuurData):ng.IPromise<any> {
            return this.$http({
                url: "/rest/factuur/",
                method: "POST",
                data: factuur,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => {
                this.dataService.reload();
            });
        };

        public deleteFactuur(factuur:FactuurData):ng.IPromise<any> {
            return this.$http({
                url: "/rest/factuur/" + factuur.uuid,
                method: "DELETE"
            }).then((response) => {
                this.dataService.reload();
            });
        };

        public addFactuur(factuur:FactuurData):ng.IPromise<any> {
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

        public copyContactFromSelectedFactuurToAdresboek(){
            var nextNr = this.contactDataService.findNextKlantnummer();
            this.selectedFactuur.klant.klantNummer = nextNr;
            this.selectedFactuur.klant.uuid= this.createUuid();
            this.contactDataService.addContact(this.selectedFactuur.klant);
        }


        createUuid():String {
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
