'use strict';

module Application.Controllers {

    import FactuurData = Application.Model.FactuurData;
    import FactuurRegelData = Application.Model.FactuurRegelData;
    import Administratie = Application.Model.Administratie;
    import FactuurDataService = Application.Services.FactuurDataService;
    import FactuurGuiService = Application.Services.FactuurGuiService;
    import FactuurGuiData = Application.Services.FactuurGuiData;
    import MyDataservice = Application.Services.MyDataservice;


    interface MyScope extends ng.IScope {
        data : FactuurGuiData;
    }

    export class SearchContactController {

        constructor(private $scope:MyScope, private $rootScope, private dataService:MyDataservice , private factuurDataService:FactuurDataService, private factuurGuiService:FactuurGuiService) {
            this.$scope.data = this.factuurGuiService.getFactuurGui().data;
        }

        selectContact(uuid: String) {
            this.factuurDataService.updateContact(this.factuurDataService.getContactByUuid(uuid));
            this.factuurGuiService.closePage(SCREEN_FACTUUR_CONTACT);
        }

        cancel() {
            this.factuurGuiService.closePage(SCREEN_FACTUUR_CONTACT);
        }
    }
}


angular.module('mswFrontendApp').controller('SearchContactController', Application.Controllers.SearchContactController);


