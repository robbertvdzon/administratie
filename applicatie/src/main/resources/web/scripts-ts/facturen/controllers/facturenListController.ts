'use strict';

module Application.Controllers {

    import FactuurData = Application.Model.FactuurData;
    import FactuurRegelData = Application.Model.FactuurRegelData;
    import Administratie = Application.Model.Administratie;
    import FactuurGuiService = Application.Services.FactuurGuiService;
    import FactuurGuiData = Application.Services.FactuurGuiData;
    import FactuurDataService = Application.Services.FactuurDataService;
    import SCREEN_FACTUUR_EDIT = Application.SCREEN_FACTUUR_EDIT;
    import MyDataservice = Application.Services.MyDataservice;

    interface MyScope extends ng.IScope {
        data : FactuurGuiData;
    }

    export class FacturenListController {

        constructor(private $scope:MyScope, private $rootScope, private dataService:MyDataservice, private factuurDataService:FactuurDataService, private factuurGuiService:FactuurGuiService) {
            this.$scope.data = this.factuurGuiService.getFactuurGui().data;
        }

        edit(uuid:String) {
            this.factuurDataService.setFactuurAsSelected(uuid);
            this.factuurGuiService.showPage(SCREEN_FACTUUR_EDIT);
        }


        newFactuur() {
            this.factuurDataService.createAndSelectNewFactuur();
            this.factuurGuiService.showPage(SCREEN_FACTUUR_EDIT);
        }

    }

}

angular.module('mswFrontendApp').controller('FacturenListController', Application.Controllers.FacturenListController);


