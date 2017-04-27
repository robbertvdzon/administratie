'use strict';

module Application.Controllers {

    import ContactGuiService = Application.Services.ContactGuiService;
    import SCREEN_CONTACT_LIJST = Application.SCREEN_CONTACT_LIJST;
    import ContactGui = Application.Services.ContactGui;

    interface MyScope extends ng.IScope {
        contactGui:ContactGui;
    }

    export class ContactenController {

        constructor(private $scope:MyScope, private contactGuiService:ContactGuiService) {
            this.$scope.contactGui = contactGuiService.getContactGui();
            contactGuiService.showPage(SCREEN_CONTACT_LIJST);
        }
    }
}

angular.module('mswFrontendApp').controller('ContactenCtrl', Application.Controllers.ContactenController);


