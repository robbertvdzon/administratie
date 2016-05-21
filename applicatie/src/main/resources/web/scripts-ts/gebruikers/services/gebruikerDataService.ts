'use strict';

module Application.Services {

    import GebruikerData = Application.Model.GebruikerData;
    import Administratie = Application.Model.Administratie;
    import GebruikerGuiService = Application.Services.GebruikerGuiService;

    export class GebruikerDataService {
        constructor(private $rootScope,private $http, private dataService:Application.Services.MyDataservice, private gebruikerGuiService:GebruikerGuiService) {
        }

        public setSelectedGebruiker(gebruiker:GebruikerData) {
            this.gebruikerGuiService.getGebruikerGui().data.selectedgebruiker = gebruiker;
            this.gebruikerGuiService.getGebruikerGui().data.addMode = gebruiker.uuid == null;
        }

        private getSelectedGebruiker():GebruikerData{
            return this.gebruikerGuiService.getGebruikerGui().data.selectedgebruiker;
        }

        public setGebruikerAsSelected(uuid) {
            var gebruiker:GebruikerData = this.getGebruikerByUuid(uuid);
            if (gebruiker != null) {
                gebruiker = this.cloneGebruiker(gebruiker);
            }
            this.setSelectedGebruiker(gebruiker);
        }

        public createAndSelectNewGebruiker() {
            var gebruiker:GebruikerData = new GebruikerData();
            gebruiker.name = "Naam";
            gebruiker.username = "Username";
            gebruiker.uuid = this.createUuid();
            this.setSelectedGebruiker(gebruiker);
        }

        public getGebruikerByUuid(uuid):GebruikerData {
            var gebruikers:GebruikerData[] = this.dataService.getData().gebruikers;
            for (var i = 0; i < gebruikers.length; i++) {
                var gebruiker:GebruikerData = gebruikers[i];
                if (gebruiker.uuid === uuid) {
                    return gebruiker;
                }
            }
            return null;
        }

        public cloneGebruiker(gebruiker:GebruikerData):GebruikerData {
            if (gebruiker==null) return null;
            var gebruikerClone = new GebruikerData();
            gebruikerClone.uuid = gebruiker.uuid;
            gebruikerClone.name = gebruiker.name;
            gebruikerClone.username = gebruiker.username;
            gebruikerClone.admin = gebruiker.admin;
            return gebruikerClone;
        }

        public setPassword(password:String): ng.IPromise<any> {
            return this.$http({
                url: "/rest/gebruiker/updatePassword/"+this.getSelectedGebruiker().uuid+"/"+password,
                method: "POST",
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => {
                this.dataService.reload();
            });
        };


        public saveGebruiker(): ng.IPromise<any> {
            return this.$http({
                url: "/rest/gebruiker/",
                method: "POST",
                data: this.getSelectedGebruiker(),
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => {
                this.dataService.reload();
            });
        };


        public deleteGebruiker(): ng.IPromise<any> {
            return this.$http({
                url: "/rest/gebruiker/" + this.getSelectedGebruiker().uuid,
                method: "DELETE"
            }).then((response) => {
                this.dataService.reload();
            });
        };

        public addNewGebruiker(gebruiker:GebruikerData): ng.IPromise<any> {
            return this.$http({
                url: "/rest/gebruiker/",
                method: "PUT",
                data: gebruiker,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => {
                this.dataService.reload();
            });
        };

        public addGebruiker(): ng.IPromise<any> {
            return this.addNewGebruiker(this.getSelectedGebruiker());
        };

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


angular.module('mswFrontendApp').service('gebruikerDataService', Application.Services.GebruikerDataService);
