'use strict';

module Application.Controllers {

    import AfschriftData = Application.Model.AfschriftData;
    import Administratie = Application.Model.Administratie;
    import AfschriftGuiData = Application.Services.AfschriftGuiData;
    import AfschriftGuiService = Application.Services.AfschriftGuiService;
    import MyDataservice = Application.Services.MyDataservice;
    import AfschriftDataService = Application.Services.AfschriftDataService;
    import SCREEN_AFSCHRIFT_EDIT = Application.SCREEN_AFSCHRIFT_EDIT;
    import RubriceerRegels = Application.Model.RubriceerRegels;

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

        getCheckAndFixRegels() {
            this.afschriftDataService.getCheckAndFixRegels().success((data)=> {
                this.afschriftDataService.setCheckAndFixRegelLijst(data);
                this.afschriftGuiService.showPage(SCREEN_AFSCHRIFT_CHECK_AND_FIX_REGELS);
            }).error(()=> {
                alert("failed");
            });
        }


        getRubriceerRegels() {
            this.afschriftDataService.getRubriceerRegels().success((data)=> {
                this.afschriftDataService.setRubriceerRegelLijst(data);
                this.afschriftGuiService.showPage(SCREEN_AFSCHRIFT_RUBRICEER_REGELS);
            }).error(()=> {
                alert("failed");
            });
        }

        uploadFile(files){
            this.afschriftDataService.uploadFileToUrl(files.files[0],"/rest/afschrift/uploadabn");
        }

        getStatus(boekingsType:String){
            if (boekingsType=="NONE") return "Niet verwerkt!";
            if (boekingsType=="PRIVE") return "Prive";
            if (boekingsType=="FACTUUR") return "Geboekt";
            if (boekingsType=="REKENING") return "Geboekt";
            return "ok";
        }

    }


}


angular.module('mswFrontendApp').controller('AfschriftenListController', Application.Controllers.AfschriftenListController);


