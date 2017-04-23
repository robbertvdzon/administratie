'use strict';

module Application.Controllers {

    import DeclaratieData = Application.Model.DeclaratieData;
    import DeclaratieDataService = Application.Services.DeclaratieDataService;
    import DeclaratieGuiService = Application.Services.DeclaratieGuiService;
    import DeclaratieGuiData = Application.Services.DeclaratieGuiData;

    interface MyScope extends ng.IScope {
        data:DeclaratieGuiData;
    }

    export class DeclaratieEditController {

        constructor(private $scope:MyScope, private declaratieDataService:DeclaratieDataService, private declaratieGuiService:DeclaratieGuiService) {
            this.$scope.data = declaratieGuiService.getDeclaratieGui().data;
        }

        showAddButton(){
            return this.$scope.data.addMode;
        }

        showSaveButton(){
            return !this.$scope.data.addMode && !angular.equals(this.$scope.data.declaratieToEdit, this.$scope.data.selecteddeclaratie);
        }

        showDeleteButton(){
            return !this.$scope.data.addMode;
        }



        save() {
            this.declaratieDataService.copyInto(this.$scope.data.declaratieToEdit, this.$scope.data.selecteddeclaratie);
            this.declaratieDataService.saveDeclaratie().then((response) => {
                this.declaratieGuiService.closePage(SCREEN_DECLARATIE_EDIT);
            }).catch((response) => {
                alert("Opslaan mislukt");
            })
        }

        add() {
            this.declaratieDataService.copyInto(this.$scope.data.declaratieToEdit, this.$scope.data.selecteddeclaratie);
            this.declaratieDataService.addDeclaratie().then((response) => {
                this.declaratieGuiService.closePage(SCREEN_DECLARATIE_EDIT);
            }).catch((response) => {
                alert("Toevoegen mislukt");
            })
        }

        delete() {
            this.declaratieDataService.deleteDeclaratie().then((response) => {
                this.declaratieGuiService.closePage(SCREEN_DECLARATIE_EDIT);
            }).catch((response) => {
                alert("Delete mislukt");
            })
        }

        cancel() {
            this.$scope.data.declaratieToEdit = this.declaratieDataService.cloneDeclaratie(this.$scope.data.selecteddeclaratie);
            this.declaratieGuiService.closePage(SCREEN_DECLARATIE_EDIT);
        }

    }
}


angular.module('mswFrontendApp').controller('DeclaratieEditController', Application.Controllers.DeclaratieEditController);


