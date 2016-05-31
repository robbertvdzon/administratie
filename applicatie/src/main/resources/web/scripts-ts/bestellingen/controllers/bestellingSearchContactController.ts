'use strict';

module Application.Controllers {

    import BestellingData = Application.Model.BestellingData;
    import BestellingRegelData = Application.Model.BestellingRegelData;
    import Administratie = Application.Model.Administratie;
    import BestellingDataService = Application.Services.BestellingDataService;
    import BestellingGuiService = Application.Services.BestellingGuiService;
    import BestellingGuiData = Application.Services.BestellingGuiData;
    import MyDataservice = Application.Services.MyDataservice;


    interface MyScope extends ng.IScope {
        data : BestellingGuiData;
    }

    export class BestellingSearchContactController {

        constructor(private $scope:MyScope, private $rootScope, private dataService:MyDataservice , private bestellingDataService:BestellingDataService, private bestellingGuiService:BestellingGuiService) {
            this.$scope.data = this.bestellingGuiService.getBestellingGui().data;
        }

        selectContact(uuid: String) {
            this.bestellingDataService.updateContact(this.bestellingDataService.getContactByUuid(uuid));
            this.bestellingDataService.saveBestelling();
            this.bestellingGuiService.closePage(SCREEN_BESTELLING_CONTACT);
        }

        cancel() {
            this.bestellingGuiService.closePage(SCREEN_BESTELLING_CONTACT);
        }
    }
}


angular.module('mswFrontendApp').controller('BestellingSearchContactController', Application.Controllers.BestellingSearchContactController);


