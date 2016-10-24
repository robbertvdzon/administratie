'use strict';

module Application.Services {

    import ContactData = Application.Model.ContactData;
    import Administratie = Application.Model.Administratie;
    import ContactGuiService = Application.Services.ContactGuiService;

    export class ContactDataService {
        constructor(private $rootScope,private $http, private dataService:Application.Services.MyDataservice, private contactGuiService:ContactGuiService) {
        }

        public setSelectedContact(contact:ContactData) {
            this.contactGuiService.getContactGui().data.selectedcontact = contact;
            this.contactGuiService.getContactGui().data.addMode = contact.uuid == null;
        }

        private getSelectedContact():ContactData{
            return this.contactGuiService.getContactGui().data.selectedcontact;
        }

        public setContactAsSelected(uuid) {
            var contact:ContactData = this.getContactByUuid(uuid);
            if (contact != null) {
                contact = this.cloneContact(contact);
            }
            this.setSelectedContact(contact);
        }

        public createAndSelectNewContact() {
            var contact:ContactData = new ContactData();
            contact.klantNummer = this.findNextKlantnummer();
            contact.naam = "Klant";
            contact.land = "Nederland";
            contact.uuid = this.createUuid();
            this.setSelectedContact(contact);
        }

        public getContactByUuid(uuid):ContactData {
            var administratie:Administratie = this.dataService.getData().administratie;
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
            contactClone.tenNameVan = contact.tenNameVan;
            contactClone.adres = contact.adres;
            return contactClone;
        }

        public saveContact(): ng.IPromise<any> {
            return this.$http({
                url: "/rest/contact/",
                method: "POST",
                data: this.getSelectedContact(),
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => {
                this.dataService.reload();
            });
        };


        public deleteContact(): ng.IPromise<any> {
            return this.$http({
                url: "/rest/contact/" + this.getSelectedContact().uuid,
                method: "DELETE"
            }).then((response) => {
                this.dataService.reload();
            });
        };

        public addNewContact(contact:ContactData): ng.IPromise<any> {
            var contactClone:ContactData = this.cloneContact(contact);
            contactClone.uuid = this.createUuid();

            return this.$http({
                url: "/rest/contact/",
                method: "PUT",
                data: contactClone,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => {
                this.dataService.reload();
            });
        };

        public addContact(): ng.IPromise<any> {
            return this.addNewContact(this.getSelectedContact());
        };

        public findNextKlantnummer():string {
            var administratie:Administratie = this.dataService.getData().administratie;
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

        public createUuid():String {
            var d = new Date().getTime();
            var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                var r = (d + Math.random() * 16) % 16 | 0;
                d = Math.floor(d / 16);
                return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
            });
            return uuid;
        };

    }


}


angular.module('mswFrontendApp').service('contactDataService', Application.Services.ContactDataService);
