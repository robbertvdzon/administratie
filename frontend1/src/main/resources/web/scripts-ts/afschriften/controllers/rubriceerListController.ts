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
    import RubriceerRegel = Application.Model.RubriceerRegel;

    interface MyScope extends ng.IScope {
        data : AfschriftGuiData;
    }

    export class RubriceerListController {

        constructor(private $scope:MyScope, private $rootScope, private dataService:MyDataservice, private afschriftDataService:AfschriftDataService, private afschriftGuiService:AfschriftGuiService) {
            this.$scope.data = this.afschriftGuiService.getAfschriftGui().data;
        }

        public setRubriceerRegelToNone(regel:RubriceerRegel){
            regel.rubriceerAction = "NONE";
        }

        public setRubriceerRegelToPriveBoeking(regel:RubriceerRegel){
            regel.rubriceerAction = "PRIVE";
        }

        public setRubriceerRegelToMaakRekening(regel:RubriceerRegel){
            regel.rubriceerAction = "CREATE_REKENING";
        }

        public setRubriceerRegelToBetalngZonderFacuur(regel:RubriceerRegel){
            regel.rubriceerAction = "BETALING_ZONDER_FACTUUR";
        }

        public setRubriceerRegelToInkomstenZonderFactuur(regel:RubriceerRegel){
            regel.rubriceerAction = "INKOMSTEN_ZONDER_FACTUUR";
        }


        public startRubriceer(){
            this.afschriftDataService.startRubriceer().then((response) => {
                this.dataService.reload();
                this.afschriftGuiService.showPage(SCREEN_AFSCHRIFT_LIJST);
            });
        }

        public isBetaling(regel:RubriceerRegel){
            return Number(regel.afschrift.bedrag)<0;
        }

    }


}


angular.module('mswFrontendApp').controller('RubriceerListController', Application.Controllers.RubriceerListController);


