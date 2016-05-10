'use strict';

module Application.Controllers {

    import FactuurData = Application.Model.FactuurData;
    import FactuurRegelData = Application.Model.FactuurRegelData;

    interface MyScope extends ng.IScope {
        selectedfactuurregel : FactuurRegelData;
        selectedfactuur : FactuurData;
        addRegelMode:boolean;

    }

    export class FactuurRegelEditController {
        $scope:MyScope;
        $rootScope:ng.IScope;


        constructor($scope, $rootScope) {
            this.$scope = $scope;
            this.$rootScope = $rootScope;
            this.initialize();
        }

        initialize() {
            var loadFactuurToEditEvent = this.$rootScope.$on('load-factuurregel-to-edit', (event, selectedfactuur:FactuurData, uuid: String)=> {
                this.loadExistingFactuur(selectedfactuur, uuid);
            });
            var loadFactuurToAddEvent = this.$rootScope.$on('load-factuurregel-to-add', (event, selectedfactuur:FactuurData)=> {
                this.loadNewFactuurRegel(selectedfactuur);
            });

            this.$scope.$on("$destroy", function () {
                loadFactuurToEditEvent();
                loadFactuurToAddEvent();
            });

        }

        loadExistingFactuur(selectedfactuur:FactuurData, uuid) {
            /*
             * DIT NAAR DATA SERVICE
             */
            this.$scope.selectedfactuur = selectedfactuur;
            for (var i = 0; i < selectedfactuur.factuurRegels.length; i++) {
                var factuurRegel = selectedfactuur.factuurRegels[i];
                if (factuurRegel.uuid === uuid) {
                    this.$scope.selectedfactuurregel = new FactuurRegelData();
                    this.$scope.selectedfactuurregel.aantal = factuurRegel.aantal;
                    this.$scope.selectedfactuurregel.omschrijving = factuurRegel.omschrijving;
                    this.$scope.selectedfactuurregel.btwPercentage = factuurRegel.btwPercentage;
                    this.$scope.selectedfactuurregel.stuksPrijs = factuurRegel.stuksPrijs;
                    this.$scope.selectedfactuurregel.uuid = factuurRegel.uuid;
                }
            }
            this.$scope.addRegelMode = false;
            this.$rootScope.$broadcast('show-factuurregel-screen');
         }

        loadNewFactuurRegel(selectedfactuur:FactuurData) {
            /*
             * DIT NAAR DATA SERVICE
             */
            this.$scope.selectedfactuur = selectedfactuur;
            this.$scope.selectedfactuurregel = new FactuurRegelData();
            this.$scope.selectedfactuurregel.omschrijving = "Werkzaamheden";
            this.$scope.selectedfactuurregel.aantal = 1;
            this.$scope.selectedfactuurregel.btwPercentage = 21;
            this.$scope.selectedfactuurregel.stuksPrijs = 72.5;
            this.$scope.selectedfactuurregel.uuid = "";
            this.$scope.addRegelMode = true;
            this.$rootScope.$broadcast('show-factuurregel-screen');
        }


        cancelAddRegel(){
            this.$rootScope.$broadcast('close-factuurregel-screen');
        }

        saveAddRegel(){
            this.$scope.selectedfactuur.factuurRegels.push(this.$scope.selectedfactuurregel);
            this.$rootScope.$broadcast('close-factuurregel-screen');
        }

        saveEditRegel(){
            this.$rootScope.$broadcast('close-factuurregel-screen');
            for (var i = 0; i < this.$scope.selectedfactuur.factuurRegels.length; i++) {
                var factuurRegel = this.$scope.selectedfactuur.factuurRegels[i];
                if (factuurRegel.uuid === this.$scope.selectedfactuurregel.uuid) {
                    factuurRegel.aantal = this.$scope.selectedfactuurregel.aantal;
                    factuurRegel.omschrijving = this.$scope.selectedfactuurregel.omschrijving;
                    factuurRegel.btwPercentage = this.$scope.selectedfactuurregel.btwPercentage;
                    factuurRegel.stuksPrijs = this.$scope.selectedfactuurregel.stuksPrijs;
                    factuurRegel.uuid = this.$scope.selectedfactuurregel.uuid;
                }
            }
        }


        deleteRegel() {
            var selectedNumber = -1;
            for (var i = 0; i < this.$scope.selectedfactuur.factuurRegels.length; i++) {
                var factuurRegel = this.$scope.selectedfactuur.factuurRegels[i];
                if (factuurRegel.uuid === this.$scope.selectedfactuurregel.uuid) {
                    selectedNumber = i;
                }
            }
            if (selectedNumber >= 0) {
                this.$scope.selectedfactuur.factuurRegels.splice(selectedNumber, 1);
            }
            this.$rootScope.$broadcast('close-factuurregel-screen');
        }
    }
}


angular.module('mswFrontendApp').controller('FactuurRegelEditController', Application.Controllers.FactuurRegelEditController);


