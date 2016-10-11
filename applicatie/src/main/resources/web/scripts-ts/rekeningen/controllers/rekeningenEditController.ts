'use strict';

module Application.Controllers {

    import RekeningData = Application.Model.RekeningData;
    import RekeningDataService = Application.Services.RekeningDataService;
    import RekeningGuiService = Application.Services.RekeningGuiService;
    import RekeningGuiData = Application.Services.RekeningGuiData;
    import BoekingData = Application.Model.BoekingData;

    interface MyScope extends ng.IScope {
        data:RekeningGuiData;
    }

    export class RekeningEditController {

        constructor(private $scope:MyScope, private rekeningDataService:RekeningDataService, private rekeningGuiService:RekeningGuiService) {
            this.$scope.data = rekeningGuiService.getRekeningGui().data;
        }

        showAddButton(){
            return this.$scope.data.addMode;
        }

        showSaveButton(){
            return !this.$scope.data.addMode && !angular.equals(this.$scope.data.rekeningToEdit, this.$scope.data.selectedrekening);
        }

        showDeleteButton(){
            return !this.$scope.data.addMode;
        }



        save() {
            this.rekeningDataService.copyInto(this.$scope.data.rekeningToEdit, this.$scope.data.selectedrekening);
            this.rekeningDataService.saveRekening().then((response) => {
                this.rekeningGuiService.closePage(SCREEN_REKENING_EDIT);
            }).catch((response) => {
                alert("Opslaan mislukt");
            })
        }

        add() {
            this.rekeningDataService.addRekening().then((response) => {
                this.rekeningGuiService.closePage(SCREEN_REKENING_EDIT);
            }).catch((response) => {
                alert("Toevoegen mislukt");
            })
        }

        delete() {
            this.rekeningDataService.deleteRekening().then((response) => {
                this.rekeningGuiService.closePage(SCREEN_REKENING_EDIT);
            }).catch((response) => {
                alert("Delete mislukt");
            })
        }

        cancel() {
            this.$scope.data.rekeningToEdit = this.rekeningDataService.cloneRekening(this.$scope.data.selectedrekening);
            this.rekeningGuiService.closePage(SCREEN_REKENING_EDIT);
        }

        disconnectAfschrift(id){
            var boekingen:BoekingData[] = this.$scope.data.rekeningToEdit.boekingen;
            for (var i = boekingen.length - 1; i >= 0; i--) {
                if (boekingen[i].afschriftNummer == id) {
                    boekingen.splice(i, 1);
                }
            }
        }

    }
}


angular.module('mswFrontendApp').controller('RekeningEditController', Application.Controllers.RekeningEditController);


