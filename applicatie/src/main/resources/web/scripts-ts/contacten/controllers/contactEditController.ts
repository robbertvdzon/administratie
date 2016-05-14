'use strict';

module Application.Controllers {

    import ContactData = Application.Model.ContactData;
    import ContactDataService = Application.Services.ContactDataService;

    interface MyScope extends ng.IScope {
        selectedcontact : ContactData;
        addMode : boolean;
    }

    export class ContactEditController {

        constructor(private $scope, private $rootScope, private contactDataService:ContactDataService, private $filter) {
            this.initialize();
        }

        initialize() {
            var loadContactToEditEvent = this.$rootScope.$on('load-contact-to-edit', (event, uuid: String)=> {
                this.loadExistingContact(uuid);
            });
            var loadContactToAddEvent = this.$rootScope.$on('load-contact-to-add', ()=> {
                this.loadNewFactuur();
            });

            this.$scope.$on("$destroy", function () {
                loadContactToEditEvent();
                loadContactToAddEvent();
            });

        }

        loadExistingContact(uuid) {
            var contact:ContactData = this.contactDataService.getContactByUuid(uuid);
            if (contact != null){
                this.$scope.selectedcontact = this.contactDataService.cloneContact(contact);
                this.$scope.addMode = false;
                this.$rootScope.$broadcast('show-contact-screen');
            }

        }

        loadNewFactuur() {
            this.$scope.selectedcontact = new ContactData();
            this.$scope.selectedcontact.name = "gebruiker";
            this.$scope.addMode = true;
            this.$rootScope.$broadcast('show-contact-screen');
        }

        save() {
            this.contactDataService.saveContact(this.$scope.selectedcontact).then((response) => {
                this.$rootScope.$broadcast('close-edit-contact');
            }).catch((response) => {
                alert("Opslaan mislukt");
            })
        }

        add() {
            this.contactDataService.addContact(this.$scope.selectedcontact).then((response) => {
                this.$rootScope.$broadcast('close-edit-contact');
            }).catch((response) => {
                alert("Toeveogen mislukt");
            })
        }

        delete() {
            this.contactDataService.deleteContact(this.$scope.selectedcontact).then((response) => {
                this.$rootScope.$broadcast('close-edit-contact');
            }).catch((response) => {
                alert("Delete mislukt");
            })
        }

        cancel() {
            this.$rootScope.$broadcast('close-edit-contact');
        }


    }
}


angular.module('mswFrontendApp').controller('ContactEditController', Application.Controllers.ContactEditController);


