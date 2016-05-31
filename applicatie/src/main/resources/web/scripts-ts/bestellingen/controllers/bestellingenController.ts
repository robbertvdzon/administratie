'use strict';

module Application.Controllers {

    import BestellingGuiService = Application.Services.BestellingGuiService;
    import BestellingGui = Application.Services.BestellingGui;
    import SCREEN_BESTELLING_LIJST = Application.SCREEN_BESTELLING_LIJST;

    interface MyScope extends ng.IScope {
        bestellingGui:BestellingGui;
    }

    export class BestellingenController {

        constructor(private $scope:MyScope, private bestellingGuiService:BestellingGuiService) {
            this.$scope.bestellingGui = bestellingGuiService.getBestellingGui();
            bestellingGuiService.showPage(SCREEN_BESTELLING_LIJST);
        }
    }
}

angular.module('mswFrontendApp').controller('BestellingenCtrl', Application.Controllers.BestellingenController);


