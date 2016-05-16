'use strict';

module Application.Controllers {

    import FactuurData = Application.Model.FactuurData;
    import FactuurRegelData = Application.Model.FactuurRegelData;
    import Administratie = Application.Model.Administratie;

    interface MyScope extends ng.IScope {
        administratie : Administratie;

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
                this.$scope.administratie = this.dataService.getData();
            }
        }


        edit(uuid: String) {
            this.$rootScope.$broadcast('set_existing_factuur_as_selected', uuid);
            this.$rootScope.$broadcast('factuur-show-page', SCREEN_FACTUUR_EDIT);
        }


        newFactuur() {
            this.$rootScope.$broadcast('create_and_select_new_factuur');
            this.$rootScope.$broadcast('factuur-show-page', SCREEN_FACTUUR_EDIT);
        }



    }

}


angular.module('mswFrontendApp').controller('FacturenListController', Application.Controllers.FacturenListController);


