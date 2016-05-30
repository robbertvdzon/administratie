'use strict';

module Application.Controllers {

    import DeclaratieData = Application.Model.DeclaratieData;
    import Administratie = Application.Model.Administratie;
    import DeclaratieGuiData = Application.Services.DeclaratieGuiData;
    import DeclaratieGuiService = Application.Services.DeclaratieGuiService;
    import MyDataservice = Application.Services.MyDataservice;
    import DeclaratieDataService = Application.Services.DeclaratieDataService;
    import SCREEN_DECLARATIE_EDIT = Application.SCREEN_DECLARATIE_EDIT;

    interface MyScope extends ng.IScope {
        data : DeclaratieGuiData;
    }

    export class DeclaratiesListController {

        constructor(private $scope:MyScope, private $rootScope, private dataService:MyDataservice, private declaratieDataService:DeclaratieDataService, private declaratieGuiService:DeclaratieGuiService) {
            this.$scope.data = this.declaratieGuiService.getDeclaratieGui().data;
        }

        edit(uuid:String) {
            this.declaratieDataService.setDeclaratieAsSelected(uuid);
            this.declaratieGuiService.showPage(SCREEN_DECLARATIE_EDIT);
        }


        newDeclaratie() {
            this.declaratieDataService.createAndSelectNewDeclaratie();
            this.declaratieGuiService.showPage(SCREEN_DECLARATIE_EDIT);
        }
    }

}


angular.module('mswFrontendApp').controller('DeclaratiesListController', Application.Controllers.DeclaratiesListController);


