'use strict';

module Application.Controllers {

    import AdministratieGegevens = Application.Model.AdministratieGegevens;
    import AdministratieDataService = Application.Services.AdministratieDataService;
    import AdministratieGuiService = Application.Services.AdministratieGuiService;
    import AdministratieGuiData = Application.Services.AdministratieGuiData;

    interface MyScope extends ng.IScope {
        data:AdministratieGuiData;
    }

    export class AdministratieEditController {

        constructor(private $scope:MyScope, private administratieDataService:AdministratieDataService, private administratieGuiService:AdministratieGuiService) {
            this.$scope.data = administratieGuiService.getAdministratieGui().data;
            this.startEdit();
        }

        startEdit(){
            this.$scope.data.administratieGegevensToEdit = this.administratieDataService.cloneAdministratieGegevens(this.$scope.data.administratieGegevens);
        }

        notDirty(){
            return angular.equals(this.$scope.data.administratieGegevensToEdit, this.$scope.data.administratieGegevens);
        }

        save() {
            this.administratieDataService.saveAdministratieGegevens().then((response) => {
                //this.gebruikerGuiService.closePage(SCREEN_GEBRUIKER_EDIT);
            }).catch((response) => {
                alert("Opslaan mislukt");
            })
        }

        cancel() {
            this.startEdit();
        }

    }
}


angular.module('mswFrontendApp').controller('AdministratieEditController', Application.Controllers.AdministratieEditController);


