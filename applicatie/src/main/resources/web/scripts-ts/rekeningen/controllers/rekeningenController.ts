'use strict';

module Application.Controllers {

    import RekeningGuiService = Application.Services.RekeningGuiService;
    import SCREEN_REKENING_LIJST = Application.SCREEN_REKENING_LIJST;
    import RekeningGui = Application.Services.RekeningGui;

    interface MyScope extends ng.IScope {
        rekeningGui:RekeningGui;
    }

    export class RekeningenController {

        constructor(private $scope:MyScope, private rekeningGuiService:RekeningGuiService) {
            this.$scope.rekeningGui = rekeningGuiService.getRekeningGui();
            rekeningGuiService.showPage(SCREEN_REKENING_LIJST);
        }
    }
}

angular.module('mswFrontendApp').controller('RekeningenCtrl', Application.Controllers.RekeningenController);


