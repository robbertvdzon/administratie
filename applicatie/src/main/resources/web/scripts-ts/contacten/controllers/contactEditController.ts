'use strict';

module Application.Controllers {

    import ContactData = Application.Model.ContactData;
    import ContactDataService = Application.Services.ContactDataService;
    import ContactGuiService = Application.Services.ContactGuiService;
    import ContactGuiData = Application.Services.ContactGuiData;

    interface MyScope extends ng.IScope {
        data:ContactGuiData;
    }

    export class ContactEditController {

        constructor(private $scope:MyScope, private contactDataService:ContactDataService, private contactGuiService:ContactGuiService) {
            this.$scope.data = contactGuiService.getContactGui().data;
        }

        save() {
            this.contactDataService.saveContact().then((response) => {
                this.contactGuiService.closePage(SCREEN_CONTACT_EDIT);
            }).catch((response) => {
                alert("Opslaan mislukt");
            })
        }

        add() {
            this.contactDataService.addContact().then((response) => {
                this.contactGuiService.closePage(SCREEN_CONTACT_EDIT);
            }).catch((response) => {
                alert("Toevoegen mislukt");
            })
        }

        delete() {
            this.contactDataService.deleteContact().then((response) => {
                this.contactGuiService.closePage(SCREEN_CONTACT_EDIT);
            }).catch((response) => {
                alert("Delete mislukt");
            })
        }

        cancel() {
            this.contactGuiService.closePage(SCREEN_CONTACT_EDIT);
        }

    }
}


angular.module('mswFrontendApp').controller('ContactEditController', Application.Controllers.ContactEditController);


