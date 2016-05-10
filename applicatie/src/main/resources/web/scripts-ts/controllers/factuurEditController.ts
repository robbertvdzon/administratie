'use strict';

module Application.Controllers {

    import FactuurData = Application.Model.FactuurData;

    interface MyScope extends ng.IScope {
        selectedfactuur : FactuurData;
        addMode : boolean;
    }

    export class FactuurEditController {
        $scope:MyScope;
        $rootScope:ng.IScope;
        factuurDataService:Application.Services.FactuurDataService;


        constructor($scope, $rootScope, factuurDataService) {
            this.$scope = $scope;
            this.$rootScope = $rootScope;
            this.factuurDataService = factuurDataService;
            this.initialize();
        }

        initialize() {
            var loadFactuurToEditEvent = this.$rootScope.$on('load-factuur-to-edit', (event, uuid: String)=> {
                this.loadExistingFactuur(uuid);
            });
            var loadFactuurToAddEvent = this.$rootScope.$on('load-factuur-to-add', ()=> {
                this.loadNewFactuur();
            });

            this.$scope.$on("$destroy", function () {
                loadFactuurToEditEvent();
                loadFactuurToAddEvent();
            });

        }

        loadExistingFactuur(uuid) {
            var factuur:FactuurData = this.factuurDataService.getFactuurByUuid(uuid);
            if (factuur != null){
                this.$scope.selectedfactuur = this.factuurDataService.cloneFactuur(factuur);
                this.$scope.addMode = false;
                this.$rootScope.$broadcast('show-factuur-screen');
            }

        }

        loadNewFactuur() {
            this.$scope.selectedfactuur = new FactuurData();
            this.$scope.addMode = true;
            this.$rootScope.$broadcast('show-factuur-screen');
        }

        save() {
            this.factuurDataService.saveFactuur(this.$scope.selectedfactuur).then((response) => {
                this.$rootScope.$broadcast('close-edit-factuur');
            }).catch((response) => {
                alert("Opslaan mislukt");
            })
        }

        add() {
            this.factuurDataService.addFactuur(this.$scope.selectedfactuur).then((response) => {
                this.$rootScope.$broadcast('close-edit-factuur');
            }).catch((response) => {
                alert("Toeveogen mislukt");
            })
        }

        delete() {
            this.factuurDataService.deleteFactuur(this.$scope.selectedfactuur).then((response) => {
                this.$rootScope.$broadcast('close-edit-factuur');
            }).catch((response) => {
                alert("Delete mislukt");
            })
        }

        cancel() {
            this.$rootScope.$broadcast('close-edit-factuur');
        }

        addRegel(ev) {
            this.$rootScope.$broadcast('load-factuurregel-to-add', this.$scope.selectedfactuur);
        }

        editRegel(uuid) {
            this.$rootScope.$broadcast('load-factuurregel-to-edit', this.$scope.selectedfactuur, uuid);
        }
    }
}


angular.module('mswFrontendApp').controller('FactuurEditController', Application.Controllers.FactuurEditController);


