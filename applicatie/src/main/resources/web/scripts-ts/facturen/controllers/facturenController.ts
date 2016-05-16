'use strict';

module Application.Controllers {

    import FactuurGuiService = Application.Services.FactuurGuiService;
    import FactuurGui = Application.Services.FactuurGui;

    interface MyScope extends ng.IScope {
        factuurGui:FactuurGui;
    }

    export class FacturenController {

        constructor(private $scope:MyScope, private factuurGuiService:FactuurGuiService) {
            this.$scope.factuurGui = factuurGuiService.getFactuurGui();
        }
    }
}

angular.module('mswFrontendApp').controller('FacturenCtrl', Application.Controllers.FacturenController);


