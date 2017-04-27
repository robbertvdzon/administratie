'use strict';

module Application.Controllers {

    import BestellingDataService = Application.Services.BestellingDataService;
    import BestellingGuiService = Application.Services.BestellingGuiService;
    import BestellingGuiData = Application.Services.BestellingGuiData;

    interface MyScope extends ng.IScope {
        data:BestellingGuiData;
    }

    export class BestellingRegelEditController {
        constructor(private $scope:MyScope, private bestellingDataService:BestellingDataService, private bestellingGuiService:BestellingGuiService) {
            this.$scope.data = this.bestellingGuiService.getBestellingGui().data;
        }

        showAddButton(){
            return this.$scope.data.addRegelMode;
        }

        showSaveButton(){
            return !this.$scope.data.addRegelMode && !angular.equals(this.$scope.data.bestellingregelToEdit, this.$scope.data.selectedbestellingregel);
        }

        showDeleteButton(){
            return !this.$scope.data.addRegelMode;
        }

        cancelAddRegel() {
            this.bestellingGuiService.closePage(SCREEN_BESTELLING_REGEL);
        }

        saveAddRegel() {
            this.bestellingDataService.addBestellingRegel(this.$scope.data.bestellingregelToEdit);
            this.bestellingGuiService.closePage(SCREEN_BESTELLING_REGEL);
        }

        saveEditRegel() {
            this.bestellingDataService.updateBestellingRegel(this.$scope.data.bestellingregelToEdit);
            this.bestellingGuiService.closePage(SCREEN_BESTELLING_REGEL);
        }

        deleteRegel() {
            this.bestellingDataService.deleteBestellingRegel(this.$scope.data.bestellingregelToEdit);
            this.bestellingGuiService.closePage(SCREEN_BESTELLING_REGEL);
        }
    }
}


angular.module('mswFrontendApp').controller('BestellingRegelEditController', Application.Controllers.BestellingRegelEditController);


