'use strict';

module Application.Controllers {

    import AfschriftGuiService = Application.Services.AfschriftGuiService;
    import SCREEN_AFSCHRIFT_LIJST = Application.SCREEN_AFSCHRIFT_LIJST;
    import AfschriftGui = Application.Services.AfschriftGui;

    interface MyScope extends ng.IScope {
        afschriftGui:AfschriftGui;
    }

    export class AfschriftenController {

        constructor(private $scope:MyScope, private afschriftGuiService:AfschriftGuiService) {
            this.$scope.afschriftGui = afschriftGuiService.getAfschriftGui();
            afschriftGuiService.showPage(SCREEN_AFSCHRIFT_LIJST);
        }
    }
}

angular.module('mswFrontendApp').controller('AfschriftenCtrl', Application.Controllers.AfschriftenController);


