'use strict';

module Application.Controllers {

    import BestellingData = Application.Model.BestellingData;
    import ContactData = Application.Model.ContactData;
    import BestellingDataService = Application.Services.BestellingDataService;
    import BestellingGuiService = Application.Services.BestellingGuiService;
    import ContactDataService = Application.Services.ContactDataService;
    import BestellingGui = Application.Services.BestellingGui;
    import SCREEN_BESTELLING_EDIT_DETAIL = Application.SCREEN_BESTELLING_EDIT_DETAIL;
    import BestellingGuiData = Application.Services.BestellingGuiData;

    interface MyScope extends ng.IScope {
        data:BestellingGuiData;
    }

    export class BestellingEditContactController {

        constructor(private $scope:MyScope, private $rootScope, private bestellingDataService:BestellingDataService, private contactDataService:ContactDataService, private bestellingGuiService:BestellingGuiService) {
            this.$scope.data = bestellingGuiService.getBestellingGui().data;
        }

        notDirty(){
            return angular.equals(this.$scope.data.bestellingToEdit, this.$scope.data.selectedbestelling);
        }

        saveContactDetails(){
            this.copyContactDetailsInto(this.$scope.data.bestellingToEdit, this.$scope.data.selectedbestelling);
            this.bestellingDataService.saveBestelling();
            this.bestellingGuiService.closePage(SCREEN_BESTELLING_EDIT_DETAIL);
            if (this.$scope.data.addToAdresboek){
                this.bestellingDataService.copyContactFromSelectedBestellingToAdresboek();
            }
        }

        copyContactDetailsInto(bestellingSrc:BestellingData, bestellingDst:BestellingData):void {
            bestellingDst.klant = new ContactData;
            bestellingDst.klant.naam = bestellingSrc.klant.naam;
            bestellingDst.klant.adres = bestellingSrc.klant.adres;
            bestellingDst.klant.klantNummer = bestellingSrc.klant.klantNummer;
            bestellingDst.klant.land = bestellingSrc.klant.land;
            bestellingDst.klant.postcode = bestellingSrc.klant.postcode;
            bestellingDst.klant.woonplaats = bestellingSrc.klant.woonplaats;
        }

        cancelEditDetails(){
            this.$scope.data.bestellingToEdit = this.bestellingDataService.cloneBestelling(this.$scope.data.selectedbestelling);
            this.bestellingGuiService.closePage(SCREEN_BESTELLING_EDIT_DETAIL);
        }
    }
}


angular.module('mswFrontendApp').controller('BestellingEditContactController', Application.Controllers.BestellingEditContactController);


