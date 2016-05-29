'use strict';

module Application.Controllers {

    import FactuurData = Application.Model.FactuurData;
    import FactuurDataService = Application.Services.FactuurDataService;
    import FactuurGuiService = Application.Services.FactuurGuiService;
    import ContactDataService = Application.Services.ContactDataService;
    import SCREEN_FACTUUR_EDIT_DETAIL = Application.SCREEN_FACTUUR_EDIT_DETAIL;
    import FactuurGuiData = Application.Services.FactuurGuiData;

    interface MyScope extends ng.IScope {
        data:FactuurGuiData;
    }

    export class FactuurEditDetailsController {

        constructor(private $scope:MyScope, private factuurDataService:FactuurDataService, private contactDataService:ContactDataService, private factuurGuiService:FactuurGuiService) {
            this.$scope.data = factuurGuiService.getFactuurGui().data;
        }

        notDirty(){
            return angular.equals(this.$scope.data.factuurToEdit, this.$scope.data.selectedfactuur);
        }

        saveDetails(){
            this.copyFactuurDetailsInto(this.$scope.data.factuurToEdit, this.$scope.data.selectedfactuur);
            this.factuurDataService.saveFactuur();
            this.factuurGuiService.closePage(SCREEN_FACTUUR_EDIT_DETAIL);
        }

        copyFactuurDetailsInto(factuurSrc:FactuurData, factuurDst:FactuurData):void {
            factuurDst.factuurNummer = factuurSrc.factuurNummer;
            factuurDst.betaald = factuurSrc.betaald;
            factuurDst.factuurDate = factuurSrc.factuurDate;
            factuurDst.klant = this.contactDataService.cloneContact(factuurSrc.klant);
        }

        cancelEditDetails(){
            this.$scope.data.factuurToEdit = this.factuurDataService.cloneFactuur(this.$scope.data.selectedfactuur);
            this.factuurGuiService.closePage(SCREEN_FACTUUR_EDIT_DETAIL);
        }

    }
}


angular.module('mswFrontendApp').controller('FactuurEditDetailsController', Application.Controllers.FactuurEditDetailsController);


