'use strict';

module Application.Controllers {

    import AfschriftData = Application.Model.AfschriftData;
    import AfschriftDataService = Application.Services.AfschriftDataService;
    import AfschriftGuiService = Application.Services.AfschriftGuiService;
    import AfschriftGuiData = Application.Services.AfschriftGuiData;
    import BoekingData = Application.Model.BoekingData;

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

        showAddBoeking(){
            this.$scope.data.addManualBoeking = new BoekingData();
            this.afschriftGuiService.showPage(SCREEN_AFSCHRIFT_ADD_MANUAL_BOEKING);
        }

        addManualBoeking(){
            this.$scope.data.addManualBoeking.omschrijving = "nieuwe boeking";
            this.$scope.data.addManualBoeking.uuid = new Date().toLocaleTimeString();
            this.$scope.data.afschriftToEdit.boekingen.push(this.afschriftDataService.cloneBoeking(this.$scope.data.addManualBoeking));
            this.afschriftGuiService.closePage(SCREEN_AFSCHRIFT_ADD_MANUAL_BOEKING);
        }

        cancelAddManualBoeking(){
            this.afschriftGuiService.closePage(SCREEN_AFSCHRIFT_ADD_MANUAL_BOEKING);
        }

        removeBoeking(uuid){
            var boekingen:BoekingData[] = this.$scope.data.afschriftToEdit.boekingen;
            for (var i = boekingen.length - 1; i >= 0; i--) {
                if (boekingen[i].uuid == uuid) {
                    boekingen.splice(i, 1);
                }
            }
        }

        echoBoeking(boeking:BoekingData){
            var result:String = boeking.omschrijving+' ';
            if (!angular.isUndefined(boeking.factuurNummer)){
                result = result + '(factuurnr: '+boeking.factuurNummer+')';
            }
            if (!angular.isUndefined(boeking.rekeningNummer)){
                result = result + '(rekeningnr: '+boeking.rekeningNummer+')';
            }
            return result
        }
    }
}


angular.module('mswFrontendApp').controller('AfschriftEditController', Application.Controllers.AfschriftEditController);


