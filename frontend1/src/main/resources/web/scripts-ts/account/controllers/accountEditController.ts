'use strict';

module Application.Controllers {

    import GebruikerData = Application.Model.GebruikerData;
    import AccountDataService = Application.Services.AccountDataService;
    import AccountGuiService = Application.Services.AccountGuiService;
    import AccountGuiData = Application.Services.AccountGuiData;

    interface MyScope extends ng.IScope {
        data:AccountGuiData;
    }

    export class AccountEditController {

        constructor(private $scope:MyScope, private accountDataService:AccountDataService, private accountGuiService:AccountGuiService) {
            this.$scope.data = accountGuiService.getGebruikerGui().data;
            this.startEdit();
        }

        startEdit(){
            this.$scope.data.accountToEdit = this.accountDataService.cloneGebruiker(this.$scope.data.account);
        }

        notDirty(){
            return angular.equals(this.$scope.data.accountToEdit, this.$scope.data.account);
        }


        save() {
            this.accountDataService.saveGebruiker().then((response) => {
                this.accountGuiService.closePage(SCREEN_ACCOUNT_EDIT);
            }).catch((response) => {
                alert("Opslaan mislukt");
            })
        }

        cancel() {
            this.startEdit();
        }

        setPassword(){
            this.accountGuiService.showPage(SCREEN_ACCOUNT_SET_PASSWD);
        }

    }
}


angular.module('mswFrontendApp').controller('AccountEditController', Application.Controllers.AccountEditController);


