'use strict';

module Application.Controllers {

    import GebruikerData = Application.Model.GebruikerData;
    import AccountDataService = Application.Services.AccountDataService;
    import AccountGuiService = Application.Services.AccountGuiService;
    import AccountGuiData = Application.Services.AccountGuiData;

    interface MyScope extends ng.IScope {
        data:AccountGuiData;
        newPassword: String;
    }

    export class AccountSetPasswordController {

        constructor(private $scope:MyScope, private accountDataService:AccountDataService, private accountGuiService:AccountGuiService) {
            this.$scope.data = accountGuiService.getGebruikerGui().data;
        }

        setPassword() {
            this.accountDataService.setPassword(this.$scope.newPassword).then((response) => {
                this.$scope.newPassword = "";
                this.accountGuiService.closePage(SCREEN_ACCOUNT_SET_PASSWD);
            }).catch((response) => {
                alert("Mislukt");
            })
        }

        cancel() {
            this.$scope.newPassword = "";
            this.accountGuiService.closePage(SCREEN_ACCOUNT_SET_PASSWD);
        }
    }
}


angular.module('mswFrontendApp').controller('AccountSetPasswordController', Application.Controllers.AccountSetPasswordController);


