'use strict';

module Application.Controllers {

    import BestellingData = Application.Model.BestellingData;
    import BestellingDataService = Application.Services.BestellingDataService;
    import BestellingGuiService = Application.Services.BestellingGuiService;
    import ContactDataService = Application.Services.ContactDataService;
    import SCREEN_BESTELLING_EDIT_DETAIL = Application.SCREEN_BESTELLING_EDIT_DETAIL;
    import BestellingGuiData = Application.Services.BestellingGuiData;

    interface MyScope extends ng.IScope {
        data:BestellingGuiData;
    }

    export class BestellingEditDetailsController {

        constructor(private $scope:MyScope, private bestellingDataService:BestellingDataService, private contactDataService:ContactDataService, private bestellingGuiService:BestellingGuiService) {
            this.$scope.data = bestellingGuiService.getBestellingGui().data;
        }

        notDirty(){
            return angular.equals(this.$scope.data.bestellingToEdit, this.$scope.data.selectedbestelling);
        }

        saveDetails(){
            this.copyBestellingDetailsInto(this.$scope.data.bestellingToEdit, this.$scope.data.selectedbestelling);
            this.bestellingDataService.saveBestelling();
            this.bestellingGuiService.closePage(SCREEN_BESTELLING_EDIT_DETAIL);
        }

        copyBestellingDetailsInto(bestellingSrc:BestellingData, bestellingDst:BestellingData):void {
            bestellingDst.bestellingNummer = bestellingSrc.bestellingNummer;
            bestellingDst.gekoppeldFactuurNummer = bestellingSrc.gekoppeldFactuurNummer;
            bestellingDst.bestellingDate = bestellingSrc.bestellingDate;
            bestellingDst.klant = this.contactDataService.cloneContact(bestellingSrc.klant);
        }

        cancelEditDetails(){
            this.$scope.data.bestellingToEdit = this.bestellingDataService.cloneBestelling(this.$scope.data.selectedbestelling);
            this.bestellingGuiService.closePage(SCREEN_BESTELLING_EDIT_DETAIL);
        }

    }
}


angular.module('mswFrontendApp').controller('BestellingEditDetailsController', Application.Controllers.BestellingEditDetailsController);


