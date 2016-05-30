'use strict';

module Application.Controllers {

    import AfschriftData = Application.Model.AfschriftData;
    import AfschriftDataService = Application.Services.AfschriftDataService;
    import AfschriftGuiService = Application.Services.AfschriftGuiService;
    import AfschriftGuiData = Application.Services.AfschriftGuiData;

    interface MyScope extends ng.IScope {
        data:AfschriftGuiData;
    }

    export class AfschriftEditController {

        constructor(private $scope:MyScope, private afschriftDataService:AfschriftDataService, private afschriftGuiService:AfschriftGuiService) {
            this.$scope.data = afschriftGuiService.getAfschriftGui().data;
        }

        showAddButton(){
            return this.$scope.data.addMode;
        }

        showSaveButton(){
            return !this.$scope.data.addMode && !angular.equals(this.$scope.data.afschriftToEdit, this.$scope.data.selectedafschrift);
        }

        showDeleteButton(){
            return !this.$scope.data.addMode;
        }



        save() {
            this.afschriftDataService.copyInto(this.$scope.data.afschriftToEdit, this.$scope.data.selectedafschrift);
            this.afschriftDataService.saveAfschrift().then((response) => {
                this.afschriftGuiService.closePage(SCREEN_AFSCHRIFT_EDIT);
            }).catch((response) => {
                alert("Opslaan mislukt");
            })
        }

        add() {
            this.afschriftDataService.addAfschrift().then((response) => {
                this.afschriftGuiService.closePage(SCREEN_AFSCHRIFT_EDIT);
            }).catch((response) => {
                alert("Toevoegen mislukt");
            })
        }

        delete() {
            this.afschriftDataService.deleteAfschrift().then((response) => {
                this.afschriftGuiService.closePage(SCREEN_AFSCHRIFT_EDIT);
            }).catch((response) => {
                alert("Delete mislukt");
            })
        }

        cancel() {
            this.$scope.data.afschriftToEdit = this.afschriftDataService.cloneAfschrift(this.$scope.data.selectedafschrift);
            this.afschriftGuiService.closePage(SCREEN_AFSCHRIFT_EDIT);
        }

    }
}


angular.module('mswFrontendApp').controller('AfschriftEditController', Application.Controllers.AfschriftEditController);


