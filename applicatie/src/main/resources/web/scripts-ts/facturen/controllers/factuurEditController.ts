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
    import FactuurGuiData = Application.Services.FactuurGuiData;

    interface MyScope extends ng.IScope {
        data:FactuurGuiData;
    }

    export class FactuurEditController {

        constructor(private $scope:MyScope, private $rootScope, private factuurDataService:FactuurDataService, private contactDataService:ContactDataService, private factuurGuiService:FactuurGuiService) {
            this.$scope.data = factuurGuiService.getFactuurGui().data;
        }

        save() {
            this.factuurDataService.saveFactuur().then((response) => {
                this.factuurGuiService.closePage(SCREEN_FACTUUR_EDIT);
            }).catch((response) => {
                alert("Opslaan mislukt");
            })
        }

        add() {
            this.factuurDataService.addFactuur().then((response) => {
                this.factuurGuiService.closePage(SCREEN_FACTUUR_EDIT);
            }).catch((response) => {
                alert("Toevoegen mislukt");
            })
        }

        delete() {
            this.factuurDataService.deleteFactuur().then((response) => {
                this.factuurGuiService.closePage(SCREEN_FACTUUR_EDIT);
            }).catch((response) => {
                alert("Delete mislukt");
            })
        }

        cancel() {
            this.factuurGuiService.closePage(SCREEN_FACTUUR_EDIT);
        }

        addRegel(ev) {
            this.factuurDataService.loadNewFactuurRegel();
        }

        editRegel(uuid) {
            var selectedfactuurregel:FactuurRegelData = this.factuurDataService.cloneFactuurRegel(this.factuurDataService.getRegelByUuid(this.$scope.data.selectedfactuur, uuid));
            this.factuurDataService.loadExistingFactuurRegel(selectedfactuurregel);
        }

        searchContact(){
            this.factuurGuiService.showPage(SCREEN_FACTUUR_CONTACT);
        }

        editDetails(){
            this.factuurGuiService.showPage(SCREEN_FACTUUR_EDIT_DETAIL);
        }

        editContactDetails(){
            this.$scope.data.addToAdresboek = false;
            this.factuurGuiService.showPage(SCREEN_FACTUUR_EDIT_CONTACT);
        }

        saveDetails(){
            this.copyFactuurDetailsInto(this.$scope.data.factuurToEdit, this.$scope.data.selectedfactuur);
            this.factuurGuiService.closePage(SCREEN_FACTUUR_EDIT_DETAIL);
        }

        saveContactDetails(){
            this.copyContactDetailsInto(this.$scope.data.factuurToEdit, this.$scope.data.selectedfactuur);
            this.factuurGuiService.closePage(SCREEN_FACTUUR_EDIT_DETAIL);
            if (this.$scope.data.addToAdresboek){
                this.factuurDataService.copyContactFromSelectedFactuurToAdresboek();
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
            this.$scope.data.factuurToEdit = this.factuurDataService.cloneFactuur(this.$scope.data.selectedfactuur);
            this.factuurGuiService.closePage(SCREEN_FACTUUR_EDIT_DETAIL);
        }

    }
}


angular.module('mswFrontendApp').controller('FactuurEditController', Application.Controllers.FactuurEditController);


