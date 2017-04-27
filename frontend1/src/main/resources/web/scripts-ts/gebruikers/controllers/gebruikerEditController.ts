'use strict';

module Application.Controllers {

    import GebruikerData = Application.Model.GebruikerData;
    import GebruikerDataService = Application.Services.GebruikerDataService;
    import GebruikerGuiService = Application.Services.GebruikerGuiService;
    import GebruikerGuiData = Application.Services.GebruikerGuiData;

    interface MyScope extends ng.IScope {
        data:GebruikerGuiData;
    }

    export class GebruikerEditController {

        constructor(private $scope:MyScope, private gebruikerDataService:GebruikerDataService, private gebruikerGuiService:GebruikerGuiService) {
            this.$scope.data = gebruikerGuiService.getGebruikerGui().data;
        }

        save() {
            this.gebruikerDataService.saveGebruiker().then((response) => {
                this.gebruikerGuiService.closePage(SCREEN_GEBRUIKER_EDIT);
            }).catch((response) => {
                alert("Opslaan mislukt");
            })
        }

        add() {
            this.gebruikerDataService.addGebruiker().then((response) => {
                this.gebruikerGuiService.closePage(SCREEN_GEBRUIKER_EDIT);
            }).catch((response) => {
                alert("Toevoegen mislukt");
            })
        }

        delete() {
            this.gebruikerDataService.deleteGebruiker().then((response) => {
                this.gebruikerGuiService.closePage(SCREEN_GEBRUIKER_EDIT);
            }).catch((response) => {
                alert("Delete mislukt");
            })
        }

        cancel() {
            this.gebruikerGuiService.closePage(SCREEN_GEBRUIKER_EDIT);
        }

        setPassword(){
            this.gebruikerGuiService.showPage(SCREEN_GEBRUIKER_SET_PASSWD);
        }

    }
}


angular.module('mswFrontendApp').controller('GebruikerEditController', Application.Controllers.GebruikerEditController);


