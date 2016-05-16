'use strict';

module Application.Controllers {

    import FactuurDataService = Application.Services.FactuurDataService;
    import FactuurGuiService = Application.Services.FactuurGuiService;
    import FactuurGuiData = Application.Services.FactuurGuiData;

    interface MyScope extends ng.IScope {
        data:FactuurGuiData;
    }

    export class FactuurRegelEditController {
        constructor(private $scope:MyScope, private factuurDataService:FactuurDataService, private factuurGuiService:FactuurGuiService) {
            this.$scope.data = this.factuurGuiService.getFactuurGui().data;
        }

        cancelAddRegel() {
            this.factuurGuiService.closePage(SCREEN_FACTUUR_REGEL);
        }

        saveAddRegel() {
            this.factuurDataService.addFactuurRegel(this.$scope.data.selectedfactuurregel);
            this.factuurGuiService.closePage(SCREEN_FACTUUR_REGEL);
        }

        saveEditRegel() {
            this.factuurDataService.updateFactuurRegel(this.$scope.data.selectedfactuurregel);
            this.factuurGuiService.closePage(SCREEN_FACTUUR_REGEL);
        }

        deleteRegel() {
            this.factuurDataService.deleteFactuurRegel(this.$scope.data.selectedfactuurregel);
            this.factuurGuiService.closePage(SCREEN_FACTUUR_REGEL);
        }
    }
}


angular.module('mswFrontendApp').controller('FactuurRegelEditController', Application.Controllers.FactuurRegelEditController);


