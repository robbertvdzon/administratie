'use strict';

module Application.Controllers {

    import FactuurData = Application.Model.FactuurData;
    import FactuurRegelData = Application.Model.FactuurRegelData;
    import FactuurDataService = Application.Services.FactuurDataService;
    import FactuurGuiService = Application.Services.FactuurGuiService;

    interface MyScope extends ng.IScope {
        selectedfactuurregel : FactuurRegelData;
        addRegelMode:boolean;

    }

    export class FactuurRegelEditController {


        constructor(private $scope, private $rootScope, private factuurDataService:FactuurDataService, private factuurGuiService:FactuurGuiService) {
            this.initialize();
        }

        initialize() {
            var loadFactuurToEditEvent = this.$rootScope.$on('load-factuurregel-to-edit', (event, selectedfactuurregel : FactuurRegelData)=> {
                this.loadExistingFactuurRegel(selectedfactuurregel);
            });
            var loadFactuurToAddEvent = this.$rootScope.$on('load-factuurregel-to-add', ()=> {
                this.loadNewFactuurRegel();
            });

            this.$scope.$on("$destroy", function () {
                loadFactuurToEditEvent();
                loadFactuurToAddEvent();
            });

        }

        loadExistingFactuurRegel(selectedfactuurregel : FactuurRegelData) {
            this.$scope.selectedfactuurregel = selectedfactuurregel;
            this.$scope.addRegelMode = false;
            this.factuurGuiService.showPage(SCREEN_FACTUUR_REGEL);
         }

        loadNewFactuurRegel() {
            this.$scope.selectedfactuurregel = new FactuurRegelData();
            this.$scope.selectedfactuurregel.omschrijving = "Werkzaamheden";
            this.$scope.selectedfactuurregel.aantal = 1;
            this.$scope.selectedfactuurregel.btwPercentage = 21;
            this.$scope.selectedfactuurregel.stuksPrijs = 72.5;
            this.$scope.selectedfactuurregel.uuid = this.factuurDataService.createUuid();
            this.$scope.addRegelMode = true;
            this.factuurGuiService.showPage(SCREEN_FACTUUR_REGEL);
        }


        cancelAddRegel(){
            this.factuurGuiService.closePage(SCREEN_FACTUUR_REGEL);
        }

        saveAddRegel(){
            this.factuurDataService.addFactuurRegel(this.$scope.selectedfactuurregel);
            this.factuurGuiService.closePage(SCREEN_FACTUUR_REGEL);
        }

        saveEditRegel(){
            this.factuurDataService.updateFactuurRegel(this.$scope.selectedfactuurregel);
            this.factuurGuiService.closePage(SCREEN_FACTUUR_REGEL);
        }


        deleteRegel() {
            this.factuurDataService.deleteFactuurRegel(this.$scope.selectedfactuurregel);
            this.factuurGuiService.closePage(SCREEN_FACTUUR_REGEL);
        }
    }
}


angular.module('mswFrontendApp').controller('FactuurRegelEditController', Application.Controllers.FactuurRegelEditController);


