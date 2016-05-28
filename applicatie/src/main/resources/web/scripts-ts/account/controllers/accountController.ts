'use strict';

module Application.Controllers {

    import AccountGuiService = Application.Services.AccountGuiService;
    import SCREEN_ACCOUNT_EDIT = Application.SCREEN_ACCOUNT_EDIT;
    import AccountGui = Application.Services.AccountGui;

    interface MyScope extends ng.IScope {
        accountGui:AccountGui;
    }

    export class AccountController {

        constructor(private $scope:MyScope, private accountGuiService:AccountGuiService) {
            this.$scope.accountGui = accountGuiService.getGebruikerGui();
            accountGuiService.showPage(SCREEN_ACCOUNT_EDIT);
        }
    }
}

angular.module('mswFrontendApp').controller('AccountCtrl', Application.Controllers.AccountController);


