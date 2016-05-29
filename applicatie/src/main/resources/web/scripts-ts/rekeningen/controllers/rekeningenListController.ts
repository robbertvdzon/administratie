'use strict';

module Application.Controllers {

    import RekeningData = Application.Model.RekeningData;
    import Administratie = Application.Model.Administratie;
    import RekeningGuiData = Application.Services.RekeningGuiData;
    import RekeningGuiService = Application.Services.RekeningGuiService;
    import MyDataservice = Application.Services.MyDataservice;
    import RekeningDataService = Application.Services.RekeningDataService;
    import SCREEN_REKENING_EDIT = Application.SCREEN_REKENING_EDIT;

    interface MyScope extends ng.IScope {
        data : RekeningGuiData;
    }

    export class RekeningenListController {

        constructor(private $scope:MyScope, private $rootScope, private dataService:MyDataservice, private rekeningDataService:RekeningDataService, private rekeningGuiService:RekeningGuiService) {
            this.$scope.data = this.rekeningGuiService.getRekeningGui().data;
        }

        edit(uuid:String) {
            this.rekeningDataService.setRekeningAsSelected(uuid);
            this.rekeningGuiService.showPage(SCREEN_REKENING_EDIT);
        }


        newRekening() {
            this.rekeningDataService.createAndSelectNewRekening();
            this.rekeningGuiService.showPage(SCREEN_REKENING_EDIT);
        }
    }

}


angular.module('mswFrontendApp').controller('RekeningenListController', Application.Controllers.RekeningenListController);


