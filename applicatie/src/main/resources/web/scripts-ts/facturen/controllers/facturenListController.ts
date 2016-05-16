'use strict';

module Application.Controllers {

    import FactuurData = Application.Model.FactuurData;
    import FactuurRegelData = Application.Model.FactuurRegelData;
    import Administratie = Application.Model.Administratie;
    import FactuurGuiService = Application.Services.FactuurGuiService;
    import SCREEN_FACTUUR_EDIT = Application.SCREEN_FACTUUR_EDIT;
    import MyDataservice = Application.Services.MyDataservice;

    interface MyScope extends ng.IScope {
        administratie : Administratie;

    }

    export class FacturenListController {

        constructor(private $scope:MyScope, private $rootScope, private dataService:MyDataservice, private factuurDataService:FactuurDataService, private factuurGuiService:FactuurGuiService) {
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
            this.factuurDataService.setExistingFactuurAsSelected(uuid);
            this.factuurGuiService.showPage(SCREEN_FACTUUR_EDIT);
        }


        newFactuur() {
            this.factuurDataService.createAndSelectNewFactuur();
            this.factuurGuiService.showPage(SCREEN_FACTUUR_EDIT);
        }

    }

}

angular.module('mswFrontendApp').controller('FacturenListController', Application.Controllers.FacturenListController);


