'use strict';

module Application.Controllers {

    import FactuurGuiService = Application.Services.FactuurGuiService;
    import FactuurGui = Application.Services.FactuurGui;
    import SCREEN_FACTUUR_LIJST = Application.SCREEN_FACTUUR_LIJST;

    interface MyScope extends ng.IScope {
        factuurGui:FactuurGui;
    }

    export class FacturenController {

        constructor(private $scope:MyScope, private factuurGuiService:FactuurGuiService) {
            this.$scope.factuurGui = factuurGuiService.getFactuurGui();
            factuurGuiService.showPage(SCREEN_FACTUUR_LIJST);
        }
    }
}

angular.module('mswFrontendApp').controller('FacturenCtrl', Application.Controllers.FacturenController);


