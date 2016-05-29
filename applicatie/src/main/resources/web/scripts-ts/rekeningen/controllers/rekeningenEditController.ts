'use strict';

module Application.Controllers {

    import RekeningData = Application.Model.RekeningData;
    import RekeningDataService = Application.Services.RekeningDataService;
    import RekeningGuiService = Application.Services.RekeningGuiService;
    import RekeningGuiData = Application.Services.RekeningGuiData;

    interface MyScope extends ng.IScope {
        data:RekeningGuiData;
    }

    export class RekeningEditController {

        constructor(private $scope:MyScope, private rekeningDataService:RekeningDataService, private rekeningGuiService:RekeningGuiService) {
            this.$scope.data = rekeningGuiService.getRekeningGui().data;
        }

        save() {
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
            this.rekeningGuiService.closePage(SCREEN_REKENING_EDIT);
        }

    }
}


angular.module('mswFrontendApp').controller('RekeningEditController', Application.Controllers.RekeningEditController);


