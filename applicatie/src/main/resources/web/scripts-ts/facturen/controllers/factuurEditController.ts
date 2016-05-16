'use strict';

module Application.Controllers {

    import FactuurData = Application.Model.FactuurData;
    import ContactData = Application.Model.ContactData;
    import FactuurRegelData = Application.Model.FactuurRegelData;
    import FactuurDataService = Application.Services.FactuurDataService;
    import FactuurGuiService = Application.Services.FactuurGuiService;
    import ContactDataService = Application.Services.ContactDataService;
    import FactuurGui = Application.Services.FactuurGui;
    import SCREEN_FACTUUR_EDIT_DETAIL = Application.SCREEN_FACTUUR_EDIT_DETAIL;
    import SCREEN_FACTUUR_EDIT_CONTACT = Application.SCREEN_FACTUUR_EDIT_CONTACT;
    import SCREEN_FACTUUR_CONTACT = Application.SCREEN_FACTUUR_CONTACT;

    interface MyScope extends ng.IScope {
        factuurGui:FactuurGui;

    }

    export class FactuurEditController {

        constructor(private $scope:MyScope, private $rootScope, private factuurDataService:FactuurDataService, private contactDataService:ContactDataService, private factuurGuiService:FactuurGuiService) {
            this.$scope.factuurGui = factuurGuiService.getFactuurGui();
        }
        //
        //initialize() {
        //    var newFactuurAvailableEvent = this.$rootScope.$on('new_selected_factuur_available', (event, factuur : FactuurData)=> {
        //        this.setSelectedFactuur(factuur);
        //    });
        //
        //    this.$scope.$on("$destroy", function () {
        //        newFactuurAvailableEvent();
        //    });
        //
        //}

        private setSelectedFactuur(factuur:FactuurData):void {
            this.$scope.factuurGui.selectedfactuur = factuur;
            this.$scope.factuurGui.factuurToEdit = this.factuurDataService.cloneFactuur(factuur);
            this.$scope.factuurGui.addMode = factuur.uuid == null;
        }

        save() {
            // TODO: factuur hoef ik niet meer mee te geven
            this.factuurDataService.saveFactuur(this.$scope.factuurGui.selectedfactuur).then((response) => {
                this.factuurGuiService.closePage(SCREEN_FACTUUR_EDIT);
            }).catch((response) => {
                alert("Opslaan mislukt");
            })
        }

        add() {
            this.factuurDataService.addFactuur(this.$scope.factuurGui.selectedfactuur).then((response) => {
                this.factuurGuiService.closePage(SCREEN_FACTUUR_EDIT);
            }).catch((response) => {
                alert("Toevoegen mislukt");
            })
        }

        delete() {
            this.factuurDataService.deleteFactuur(this.$scope.factuurGui.selectedfactuur).then((response) => {
                this.factuurGuiService.closePage(SCREEN_FACTUUR_EDIT);
            }).catch((response) => {
                alert("Delete mislukt");
            })
        }

        cancel() {
            this.factuurGuiService.closePage(SCREEN_FACTUUR_EDIT);
        }

        addRegel(ev) {
            this.$rootScope.$broadcast('load-factuurregel-to-add');
        }

        editRegel(uuid) {
            var selectedfactuurregel:FactuurRegelData = this.factuurDataService.cloneFactuurRegel(this.factuurDataService.getRegelByUuid(this.$scope.factuurGui.selectedfactuur, uuid));
            this.$rootScope.$broadcast('load-factuurregel-to-edit', selectedfactuurregel, uuid);
        }

        searchContact(){
            this.factuurGuiService.showPage(SCREEN_FACTUUR_CONTACT);
        }

        editDetails(){
            this.factuurGuiService.showPage(SCREEN_FACTUUR_EDIT_DETAIL);
        }

        editContactDetails(){
            this.$scope.factuurGui.addToAdresboek = false;
            this.factuurGuiService.showPage(SCREEN_FACTUUR_EDIT_CONTACT);
        }

        saveDetails(){
            this.copyFactuurDetailsInto(this.$scope.factuurGui.factuurToEdit, this.$scope.factuurGui.selectedfactuur);
            this.factuurGuiService.closePage(SCREEN_FACTUUR_EDIT_DETAIL);
        }

        saveContactDetails(){
            this.copyContactDetailsInto(this.$scope.factuurGui.factuurToEdit, this.$scope.factuurGui.selectedfactuur);
            this.factuurGuiService.closePage(SCREEN_FACTUUR_EDIT_DETAIL);
            if (this.$scope.factuurGui.addToAdresboek){
                this.factuurDataService.copyContactFromSelectedFactuurToAdresboek();
                this.$scope.factuurGui.addToAdresboek = false;
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
            this.$scope.factuurGui.factuurToEdit = this.factuurDataService.cloneFactuur(this.$scope.factuurGui.selectedfactuur);
            this.factuurGuiService.closePage(SCREEN_FACTUUR_EDIT_DETAIL);
        }

        //private updateContact(contact:ContactData):void {
        //    var contactClone = this.contactDataService.cloneContact(contact);
        //    contactClone.uuid = this.factuurDataService.createUuid()
        //    this.$scope.selectedfactuur.klant = contactClone;
        //}

    }
}


angular.module('mswFrontendApp').controller('FactuurEditController', Application.Controllers.FactuurEditController);


