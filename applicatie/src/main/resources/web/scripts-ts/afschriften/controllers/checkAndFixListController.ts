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

    export class CheckAndFixListController {

        constructor(private $scope:MyScope, private $rootScope, private dataService:MyDataservice, private afschriftDataService:AfschriftDataService, private afschriftGuiService:AfschriftGuiService) {
            this.$scope.data = this.afschriftGuiService.getAfschriftGui().data;
        }

        public startFix(){
            this.afschriftDataService.startFix().then((response) => {
                this.dataService.reload();
                this.afschriftGuiService.showPage(SCREEN_AFSCHRIFT_LIJST);
            });
        }

        getActie(actie:String){
            if (actie=="NONE") return "Geen actie";
            if (actie=="REMOVE_REF_FROM_FACTUUR") return "Repareer de factuur";
            if (actie=="REMOVE_REF_FROM_REKENING") return "Repareer de rekening";
            if (actie=="REMOVE_REF_FROM_AFSCHRIFT") return "Repareer het afschrift";
            return "Geen actie";
        }


    }


}


angular.module('mswFrontendApp').controller('CheckAndFixListController', Application.Controllers.CheckAndFixListController);


