'use strict';

module Application.Controllers {

    import FactuurData = Application.Model.FactuurData;
    import Gebruiker = Application.Model.Gebruiker;
    import FactuurRegelData = Application.Model.FactuurRegelData;

    interface MyScope extends ng.IScope {
        gebruiker : Gebruiker;

    }

    export class FacturenListController {
        $scope:MyScope;
        $rootScope:ng.IScope;
        dataService:Application.Services.MyDataservice;

        constructor($scope, $rootScope, dataService) {
            this.$scope = $scope;
            this.$rootScope = $rootScope;
            this.dataService = dataService;
            this.initialize();
        }

        initialize() {
            var unregisterDataUpdatedEvent = this.$rootScope.$on('data-updated', ()=> {
                this.loadData();
            });


            this.$scope.$on("$destroy", function() {
                unregisterDataUpdatedEvent();
            });

            this.loadData();
        }

        loadData() {
            if (this.dataService.getData() != undefined) {
                this.$scope.gebruiker = this.dataService.getData();
            }
        }


        edit(uuid: String) {
            this.$rootScope.$broadcast('load-factuur-to-edit', uuid);
        }


        newFactuur() {
            this.$rootScope.$broadcast('load-factuur-to-add');
        }



    }

}


angular.module('mswFrontendApp').controller('FacturenListController', Application.Controllers.FacturenListController);


