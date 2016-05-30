'use strict';

module Application.Controllers {

    import DeclaratieGuiService = Application.Services.DeclaratieGuiService;
    import SCREEN_DECLARATIE_LIJST = Application.SCREEN_DECLARATIE_LIJST;
    import DeclaratieGui = Application.Services.DeclaratieGui;

    interface MyScope extends ng.IScope {
        declaratieGui:DeclaratieGui;
    }

    export class DeclaratiesController {

        constructor(private $scope:MyScope, private declaratieGuiService:DeclaratieGuiService) {
            this.$scope.declaratieGui = declaratieGuiService.getDeclaratieGui();
            declaratieGuiService.showPage(SCREEN_DECLARATIE_LIJST);
        }
    }
}

angular.module('mswFrontendApp').controller('DeclaratiesCtrl', Application.Controllers.DeclaratiesController);


