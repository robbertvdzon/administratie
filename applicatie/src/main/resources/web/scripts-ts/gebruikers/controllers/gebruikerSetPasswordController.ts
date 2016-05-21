'use strict';

module Application.Controllers {

    import GebruikerData = Application.Model.GebruikerData;
    import GebruikerDataService = Application.Services.GebruikerDataService;
    import GebruikerGuiService = Application.Services.GebruikerGuiService;
    import GebruikerGuiData = Application.Services.GebruikerGuiData;

    interface MyScope extends ng.IScope {
        data:GebruikerGuiData;
        newPassword: String;
    }

    export class GebruikerSetPasswordController {

        constructor(private $scope:MyScope, private gebruikerDataService:GebruikerDataService, private gebruikerGuiService:GebruikerGuiService) {
            this.$scope.data = gebruikerGuiService.getGebruikerGui().data;
        }

        setPassword() {
            this.gebruikerDataService.setPassword(this.$scope.newPassword).then((response) => {
                this.$scope.newPassword = "";
                this.gebruikerGuiService.closePage(SCREEN_GEBRUIKER_SET_PASSWD);
            }).catch((response) => {
                alert("Mislukt");
            })
        }

        cancel() {
            this.$scope.newPassword = "";
            this.gebruikerGuiService.closePage(SCREEN_GEBRUIKER_SET_PASSWD);
        }
    }
}


angular.module('mswFrontendApp').controller('GebruikerSetPasswordController', Application.Controllers.GebruikerSetPasswordController);


