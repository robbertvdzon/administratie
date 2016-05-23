'use strict';

module Application.Controllers {
    declare var pdfMake:any;

    import FactuurData = Application.Model.FactuurData;
    import FactuurRegelData = Application.Model.FactuurRegelData;
    import FactuurDataService = Application.Services.FactuurDataService;
    import FactuurGuiService = Application.Services.FactuurGuiService;
    import SCREEN_FACTUUR_EDIT_DETAIL = Application.SCREEN_FACTUUR_EDIT_DETAIL;
    import SCREEN_FACTUUR_EDIT_CONTACT = Application.SCREEN_FACTUUR_EDIT_CONTACT;
    import SCREEN_FACTUUR_CONTACT = Application.SCREEN_FACTUUR_CONTACT;
    import FactuurGuiData = Application.Services.FactuurGuiData;

    interface MyScope extends ng.IScope {
        data:FactuurGuiData;
    }

    export class FactuurEditController {

        constructor(private $scope:MyScope, private factuurDataService:FactuurDataService, private factuurGuiService:FactuurGuiService, private $window) {
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

        searchContact() {
            this.factuurGuiService.showPage(SCREEN_FACTUUR_CONTACT);
        }

        editDetails() {
            this.factuurDataService.resetFactuurToEdit();
            this.factuurGuiService.showPage(SCREEN_FACTUUR_EDIT_DETAIL);
        }

        editContactDetails() {
            this.factuurDataService.resetFactuurToEdit();
            this.$scope.data.addToAdresboek = false;
            this.factuurGuiService.showPage(SCREEN_FACTUUR_EDIT_CONTACT);
        }

        print(uuid) {
            this.$window.open('/rest/factuur/pdf/'+uuid, '_blank');
        }
    }
}


angular.module('mswFrontendApp').controller('FactuurEditController', Application.Controllers.FactuurEditController);


