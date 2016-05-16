'use strict';

module Application.Controllers {

    import FactuurData = Application.Model.FactuurData;
    import ContactData = Application.Model.ContactData;
    import FactuurRegelData = Application.Model.FactuurRegelData;
    import FactuurDataService = Application.Services.FactuurDataService;
    import ContactDataService = Application.Services.ContactDataService;

    interface MyScope extends ng.IScope {
        selectedfactuur : FactuurData;
        factuurToEdit : FactuurData;
        addMode : boolean;
        addToAdresboek : boolean;
    }

    export class FactuurEditController {

        constructor(private $scope, private $rootScope, private factuurDataService:FactuurDataService, private contactDataService:ContactDataService) {
            this.initialize();
        }

        initialize() {
            var newFactuurAvailableEvent = this.$rootScope.$on('new_selected_factuur_available', (event, factuur : FactuurData)=> {
                this.setSelectedFactuur(factuur);
            });

            var addFactuurRegelEvent = this.$rootScope.$on('update-contact', (event, contact : ContactData)=> {
                this.updateContact(contact);
            });

            this.$scope.$on("$destroy", function () {
                newFactuurAvailableEvent();
                addFactuurRegelEvent();
            });

        }

        private setSelectedFactuur(factuur:FactuurData):void {
            this.$scope.selectedfactuur = factuur;
            this.$scope.factuurToEdit = this.factuurDataService.cloneFactuur(factuur);
            this.$scope.addMode = factuur.uuid == null;
        }

        save() {
            this.factuurDataService.saveFactuur(this.$scope.selectedfactuur).then((response) => {
                //this.$rootScope.$broadcast('close-edit-factuur');
                this.$rootScope.$broadcast('factuur-close-page', SCREEN_FACTUUR_EDIT);
            }).catch((response) => {
                alert("Opslaan mislukt");
            })
        }

        add() {
            this.factuurDataService.addFactuur(this.$scope.selectedfactuur).then((response) => {
                //this.$rootScope.$broadcast('close-edit-factuur');
                this.$rootScope.$broadcast('factuur-close-page', SCREEN_FACTUUR_EDIT);
            }).catch((response) => {
                alert("Toevoegen mislukt");
            })
        }

        delete() {
            this.factuurDataService.deleteFactuur(this.$scope.selectedfactuur).then((response) => {
                //this.$rootScope.$broadcast('close-edit-factuur');
                this.$rootScope.$broadcast('factuur-close-page', SCREEN_FACTUUR_EDIT);
            }).catch((response) => {
                alert("Delete mislukt");
            })
        }

        cancel() {
            //this.$rootScope.$broadcast('close-edit-factuur');
            this.$rootScope.$broadcast('factuur-close-page', SCREEN_FACTUUR_EDIT);
        }

        addRegel(ev) {
            this.$rootScope.$broadcast('load-factuurregel-to-add');
        }

        editRegel(uuid) {
            var selectedfactuurregel:FactuurRegelData = this.factuurDataService.cloneFactuurRegel(this.factuurDataService.getRegelByUuid(this.$scope.selectedfactuur, uuid));
            this.$rootScope.$broadcast('load-factuurregel-to-edit', selectedfactuurregel, uuid);
        }

        //addFactuurRegel(factuurregel:FactuurRegelData){
        //    alert("add "+factuurregel.omschrijving);
        //    this.$scope.selectedfactuur.factuurRegels.push(factuurregel);
        //    alert("add "+this.$scope.selectedfactuur.factuurRegels.length);
        //}
        //
        //updateFactuurRegel(factuurregel:FactuurRegelData){
        //    this.factuurDataService.updateFactuurRegel(this.$scope.selectedfactuur, factuurregel);
        //}
        //
        //deleteFactuurRegel(factuurregel:FactuurRegelData) {
        //    this.factuurDataService.deleteFactuurRegel(this.$scope.selectedfactuur, factuurregel);
        //}
        //
        searchContact(){
            this.$rootScope.$broadcast('factuur-show-page', SCREEN_FACTUUR_CONTACT);
            //this.$rootScope.$broadcast('show-search-contact-screen');
        }

        editDetails(){
            this.$rootScope.$broadcast('factuur-show-page', SCREEN_FACTUUR_EDIT_DETAIL);
        }

        editContactDetails(){
            this.$scope.addToAdresboek = false;
            this.$rootScope.$broadcast('factuur-show-page', SCREEN_FACTUUR_EDIT_CONTACT);
        }

        saveDetails(){
            this.copyFactuurDetailsInto(this.$scope.factuurToEdit, this.$scope.selectedfactuur);
            this.$rootScope.$broadcast('factuur-close-page', SCREEN_FACTUUR_EDIT_DETAIL);
        }

        saveContactDetails(){
            this.copyContactDetailsInto(this.$scope.factuurToEdit, this.$scope.selectedfactuur);
            this.$rootScope.$broadcast('factuur-close-page', SCREEN_FACTUUR_EDIT_DETAIL);
            if (this.$scope.addToAdresboek){
                this.factuurDataService.copyContactFromSelectedFactuurToAdresboek();
                this.$scope.addToAdresboek = false;
            }
        }



        copyFactuurDetailsInto(factuurSrc:FactuurData, factuurDst:FactuurData):void {
            factuurDst.factuurNummer = factuurSrc.factuurNummer;
            factuurDst.betaald = factuurSrc.betaald;
            factuurDst.factuurDate = factuurSrc.factuurDate;
            factuurDst.klant = this.contactDataService.cloneContact(factuurSrc.klant);
        }


        copyContactDetailsInto(factuurSrc:FactuurData, factuurDst:FactuurData):void {
            factuurDst.klant.naam = factuurSrc.klant.naam;
            factuurDst.klant.adres = factuurSrc.klant.adres;
            factuurDst.klant.klantNummer = factuurSrc.klant.klantNummer;
            factuurDst.klant.land = factuurSrc.klant.land;
            factuurDst.klant.postcode = factuurSrc.klant.postcode;
            factuurDst.klant.woonplaats = factuurSrc.klant.woonplaats;
        }

        cancelEditDetails(){
            this.$scope.factuurToEdit = this.factuurDataService.cloneFactuur(this.$scope.selectedfactuur);
            this.$rootScope.$broadcast('factuur-close-page', SCREEN_FACTUUR_EDIT_DETAIL);
        }

        private updateContact(contact:ContactData):void {
            var contactClone = this.contactDataService.cloneContact(contact);
            contactClone.uuid = this.factuurDataService.createUuid()
            this.$scope.selectedfactuur.klant = contactClone;
        }

    }
}


angular.module('mswFrontendApp').controller('FactuurEditController', Application.Controllers.FactuurEditController);


