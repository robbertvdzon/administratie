'use strict';

module Application.Controllers {

    import FactuurData = Application.Model.FactuurData;
    import FactuurDataService = Application.Services.FactuurDataService;
    import FactuurGuiService = Application.Services.FactuurGuiService;
    import ContactDataService = Application.Services.ContactDataService;
    import FactuurGui = Application.Services.FactuurGui;
    import SCREEN_FACTUUR_EDIT_DETAIL = Application.SCREEN_FACTUUR_EDIT_DETAIL;
    import FactuurGuiData = Application.Services.FactuurGuiData;

    interface MyScope extends ng.IScope {
        data:FactuurGuiData;
    }

    export class FactuurEditContactController {

        constructor(private $scope:MyScope, private $rootScope, private factuurDataService:FactuurDataService, private contactDataService:ContactDataService, private factuurGuiService:FactuurGuiService) {
            this.$scope.data = factuurGuiService.getFactuurGui().data;
        }

        saveContactDetails(){
            this.copyContactDetailsInto(this.$scope.data.factuurToEdit, this.$scope.data.selectedfactuur);
            this.factuurGuiService.closePage(SCREEN_FACTUUR_EDIT_DETAIL);
            if (this.$scope.data.addToAdresboek){
                this.factuurDataService.copyContactFromSelectedFactuurToAdresboek();
            }
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


angular.module('mswFrontendApp').controller('FactuurEditContactController', Application.Controllers.FactuurEditContactController);


