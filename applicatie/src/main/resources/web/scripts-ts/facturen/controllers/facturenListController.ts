'use strict';

module Application.Controllers {

    import FactuurData = Application.Model.FactuurData;
    import FactuurRegelData = Application.Model.FactuurRegelData;
    import Administratie = Application.Model.Administratie;
    import FactuurGuiService = Application.Services.FactuurGuiService;
    import SCREEN_FACTUUR_EDIT = Application.SCREEN_FACTUUR_EDIT;

    interface MyScope extends ng.IScope {
        administratie : Administratie;

    }

    export class FacturenListController {
        $scope:MyScope;
        $rootScope:ng.IScope;
        dataService:Application.Services.MyDataservice;

        constructor($scope, $rootScope, dataService, private factuurGuiService:FactuurGuiService) {
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
            this.factuurGuiService.showPage(SCREEN_FACTUUR_EDIT);
        }


        newFactuur() {
            this.$rootScope.$broadcast('create_and_select_new_factuur');
            this.factuurGuiService.showPage(SCREEN_FACTUUR_EDIT);
        }



    }

}


angular.module('mswFrontendApp').controller('FacturenListController', Application.Controllers.FacturenListController);


