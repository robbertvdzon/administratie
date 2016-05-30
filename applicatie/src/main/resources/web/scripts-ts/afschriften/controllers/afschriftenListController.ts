'use strict';

module Application.Controllers {

    import AfschriftData = Application.Model.AfschriftData;
    import Administratie = Application.Model.Administratie;
    import AfschriftGuiData = Application.Services.AfschriftGuiData;
    import AfschriftGuiService = Application.Services.AfschriftGuiService;
    import MyDataservice = Application.Services.MyDataservice;
    import AfschriftDataService = Application.Services.AfschriftDataService;
    import SCREEN_AFSCHRIFT_EDIT = Application.SCREEN_AFSCHRIFT_EDIT;

    interface MyScope extends ng.IScope {
        data : AfschriftGuiData;
    }

    export class AfschriftenListController {

        constructor(private $scope:MyScope, private $rootScope, private dataService:MyDataservice, private afschriftDataService:AfschriftDataService, private afschriftGuiService:AfschriftGuiService) {
            this.$scope.data = this.afschriftGuiService.getAfschriftGui().data;
        }

        edit(uuid:String) {
            this.afschriftDataService.setAfschriftAsSelected(uuid);
            this.afschriftGuiService.showPage(SCREEN_AFSCHRIFT_EDIT);
        }


        newAfschrift() {
            this.afschriftDataService.createAndSelectNewAfschrift();
            this.afschriftGuiService.showPage(SCREEN_AFSCHRIFT_EDIT);
        }
    }

}


angular.module('mswFrontendApp').controller('AfschriftenListController', Application.Controllers.AfschriftenListController);


