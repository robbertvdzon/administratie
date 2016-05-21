'use strict';

module Application.Controllers {

    import GebruikerGuiService = Application.Services.GebruikerGuiService;
    import SCREEN_GEBRUIKER_LIJST = Application.SCREEN_GEBRUIKER_LIJST;
    import GebruikerGui = Application.Services.GebruikerGui;

    interface MyScope extends ng.IScope {
        gebruikerGui:GebruikerGui;
    }

    export class GebruikersController {

        constructor(private $scope:MyScope, private gebruikerGuiService:GebruikerGuiService) {
            this.$scope.gebruikerGui = gebruikerGuiService.getGebruikerGui();
            gebruikerGuiService.showPage(SCREEN_GEBRUIKER_LIJST);
        }
    }
}

angular.module('mswFrontendApp').controller('GebruikersCtrl', Application.Controllers.GebruikersController);


