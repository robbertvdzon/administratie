'use strict';

module Application.Controllers {
    declare var pdfMake:any;

    import BestellingData = Application.Model.BestellingData;
    import BestellingRegelData = Application.Model.BestellingRegelData;
    import BestellingDataService = Application.Services.BestellingDataService;
    import BestellingGuiService = Application.Services.BestellingGuiService;
    import SCREEN_BESTELLING_EDIT_DETAIL = Application.SCREEN_BESTELLING_EDIT_DETAIL;
    import SCREEN_BESTELLING_EDIT_CONTACT = Application.SCREEN_BESTELLING_EDIT_CONTACT;
    import SCREEN_BESTELLING_CONTACT = Application.SCREEN_BESTELLING_CONTACT;
    import BestellingGuiData = Application.Services.BestellingGuiData;

    interface MyScope extends ng.IScope {
        data:BestellingGuiData;
    }

    export class BestellingEditController {

        constructor(private $scope:MyScope, private bestellingDataService:BestellingDataService, private bestellingGuiService:BestellingGuiService, private $window) {
            this.$scope.data = bestellingGuiService.getBestellingGui().data;
        }

        save() {
            this.bestellingDataService.saveBestelling().then((response) => {
                this.bestellingGuiService.closePage(SCREEN_BESTELLING_EDIT);
            }).catch((response) => {
                alert("Opslaan mislukt");
            })
        }

        add() {
            this.bestellingDataService.addBestelling().then((response) => {
                this.bestellingGuiService.closePage(SCREEN_BESTELLING_EDIT);
            }).catch((response) => {
                alert("Toevoegen mislukt");
            })
        }

        delete() {
            this.bestellingDataService.deleteBestelling().then((response) => {
                this.bestellingGuiService.closePage(SCREEN_BESTELLING_EDIT);
            }).catch((response) => {
                alert("Delete mislukt");
            })
        }

        back() {
            this.bestellingGuiService.closePage(SCREEN_BESTELLING_EDIT);
        }

        addRegel(ev) {
            this.bestellingDataService.loadNewBestellingRegel();
        }

        editRegel(uuid) {
            var selectedbestellingregel:BestellingRegelData = this.bestellingDataService.cloneBestellingRegel(this.bestellingDataService.getRegelByUuid(this.$scope.data.selectedbestelling, uuid));
            this.bestellingDataService.loadExistingBestellingRegel(selectedbestellingregel);
        }

        searchContact() {
            this.bestellingGuiService.showPage(SCREEN_BESTELLING_CONTACT);
        }

        editDetails() {
            this.bestellingDataService.resetBestellingToEdit();
            this.bestellingGuiService.showPage(SCREEN_BESTELLING_EDIT_DETAIL);
        }

        editContactDetails() {
            this.bestellingDataService.resetBestellingToEdit();
            this.$scope.data.addToAdresboek = false;
            this.bestellingGuiService.showPage(SCREEN_BESTELLING_EDIT_CONTACT);
        }
    }
}


angular.module('mswFrontendApp').controller('BestellingEditController', Application.Controllers.BestellingEditController);


