'use strict';

module Application.Controllers {

    import ContactData = Application.Model.ContactData;
    import Administratie = Application.Model.Administratie;
    import ContactGuiData = Application.Services.ContactGuiData;
    import ContactGuiService = Application.Services.ContactGuiService;
    import MyDataservice = Application.Services.MyDataservice;
    import ContactDataService = Application.Services.ContactDataService;
    import SCREEN_CONTACT_EDIT = Application.SCREEN_CONTACT_EDIT;

    interface MyScope extends ng.IScope {
        data : ContactGuiData;
    }

    export class ContactenListController {

        constructor(private $scope:MyScope, private $rootScope, private dataService:MyDataservice, private contactDataService:ContactDataService, private contactGuiService:ContactGuiService) {
            this.$scope.data = this.contactGuiService.getContactGui().data;
        }

        edit(uuid:String) {
            this.contactDataService.setContactAsSelected(uuid);
            this.contactGuiService.showPage(SCREEN_CONTACT_EDIT);
        }


        newContact() {
            this.contactDataService.createAndSelectNewContact();
            this.contactGuiService.showPage(SCREEN_CONTACT_EDIT);
        }
    }

}


angular.module('mswFrontendApp').controller('ContactenListController', Application.Controllers.ContactenListController);


