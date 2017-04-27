'use strict';

module Application.Controllers {

    import BestellingData = Application.Model.BestellingData;
    import BestellingRegelData = Application.Model.BestellingRegelData;
    import Administratie = Application.Model.Administratie;
    import BestellingGuiService = Application.Services.BestellingGuiService;
    import BestellingGuiData = Application.Services.BestellingGuiData;
    import BestellingDataService = Application.Services.BestellingDataService;
    import SCREEN_BESTELLING_EDIT = Application.SCREEN_BESTELLING_EDIT;
    import MyDataservice = Application.Services.MyDataservice;

    interface MyScope extends ng.IScope {
        data : BestellingGuiData;
    }

    export class BestellingenListController {

        constructor(private $scope:MyScope, private $rootScope, private dataService:MyDataservice, private bestellingDataService:BestellingDataService, private bestellingGuiService:BestellingGuiService) {
            this.$scope.data = this.bestellingGuiService.getBestellingGui().data;
        }

        edit(uuid:String) {
            this.bestellingDataService.setBestellingAsSelected(uuid);
            this.bestellingGuiService.showPage(SCREEN_BESTELLING_EDIT);
        }


        newBestelling() {
            this.bestellingDataService.createAndSelectNewBestelling();
            this.bestellingGuiService.showPage(SCREEN_BESTELLING_EDIT);
        }

    }

}

angular.module('mswFrontendApp').controller('BestellingenListController', Application.Controllers.BestellingenListController);


