'use strict';

module Application.Controllers {

    import GebruikerData = Application.Model.GebruikerData;
    import Administratie = Application.Model.Administratie;
    import GebruikerGuiData = Application.Services.GebruikerGuiData;
    import GebruikerGuiService = Application.Services.GebruikerGuiService;
    import MyDataservice = Application.Services.MyDataservice;
    import GebruikerDataService = Application.Services.GebruikerDataService;
    import SCREEN_GEBRUIKER_EDIT = Application.SCREEN_GEBRUIKER_EDIT;

    interface MyScope extends ng.IScope {
        data : GebruikerGuiData;
    }

    export class GebruikersListController {

        constructor(private $scope:MyScope, private $rootScope, private dataService:MyDataservice, private gebruikerDataService:GebruikerDataService, private gebruikerGuiService:GebruikerGuiService) {
            this.$scope.data = this.gebruikerGuiService.getGebruikerGui().data;
        }

        edit(uuid:String) {
            this.gebruikerDataService.setGebruikerAsSelected(uuid);
            this.gebruikerGuiService.showPage(SCREEN_GEBRUIKER_EDIT);
        }


        newGebruiker() {
            this.gebruikerDataService.createAndSelectNewGebruiker();
            this.gebruikerGuiService.showPage(SCREEN_GEBRUIKER_EDIT);
        }
    }

}


angular.module('mswFrontendApp').controller('GebruikersListController', Application.Controllers.GebruikersListController);


