'use strict';

module Application.Services {

    import ContactData = Application.Model.ContactData;
    import Administratie = Application.Model.Administratie;

    export class ContactDataService {


        constructor(private $rootScope,private $http, private dataService:Application.Services.MyDataservice) {
        }

        public getContactByUuid(uuid):ContactData {
            var administratie:Administratie = this.dataService.getData();
            for (var i = 0; i < administratie.adresboek.length; i++) {
                var contact:ContactData = administratie.adresboek[i];
                if (contact.uuid === uuid) {
                    return contact;
                }
            }
            return null;
        }

        public cloneContact(contact:ContactData):ContactData {
            var contactClone = new ContactData();
            contactClone.uuid = contact.uuid;
            contactClone.name = contact.name;
            return contactClone;
        }


        public saveContact(contact:ContactData): ng.IPromise<any> {
            return this.$http({
                url: "/rest/contact/",
                method: "POST",
                data: contact,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => {
                this.dataService.reload();
            });
        };

        public deleteContact(contact:ContactData): ng.IPromise<any> {
            return this.$http({
                url: "/rest/contact/" + contact.uuid,
                method: "DELETE"
            }).then((response) => {
                this.dataService.reload();
            });
        };

        public addContact(contact:ContactData): ng.IPromise<any> {
            return this.$http({
                url: "/rest/contact/",
                method: "PUT",
                data: contact,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => {
                this.dataService.reload();
            });
        };

    }
}


angular.module('mswFrontendApp').service('contactDataService', Application.Services.ContactDataService);
