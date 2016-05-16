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
            if (contact==null) return null;
            var contactClone = new ContactData();
            contactClone.uuid = contact.uuid;
            contactClone.naam = contact.naam;
            contactClone.klantNummer = contact.klantNummer;
            contactClone.land = contact.land;
            contactClone.postcode = contact.postcode;
            contactClone.woonplaats = contact.woonplaats;
            contactClone.adres = contact.adres;
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

        public findNextKlantnummer():string {
            var administratie:Administratie = this.dataService.getData();
            var hoogste:number = 0;
            for (var i = 0; i < administratie.adresboek.length; i++) {
                var klantNr:number = parseInt(String(administratie.adresboek[i].klantNummer),10);
                if (klantNr>hoogste){
                    hoogste = klantNr;
                }
            }
            var nieuwNummer = hoogste+1;
            return ""+nieuwNummer;
        }

    }
}


angular.module('mswFrontendApp').service('contactDataService', Application.Services.ContactDataService);
