'use strict';

module Application.Services {

    import GebruikerData = Application.Model.GebruikerData;
    import Administratie = Application.Model.Administratie;
    import AccountGuiService = Application.Services.AccountGuiService;

    export class AccountDataService {
        constructor(private $rootScope,private $http, private dataService:Application.Services.MyDataservice, private accountGuiService:AccountGuiService) {
        }

        private getAccount():GebruikerData{
            return this.accountGuiService.getGebruikerGui().data.account;
        }

        private getAccountToEdit():GebruikerData{
            return this.accountGuiService.getGebruikerGui().data.accountToEdit;
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
                url: "/rest/gebruiker/updatePassword/"+this.getAccount().uuid+"/"+password,
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
                data: this.getAccountToEdit(),
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => {
                this.dataService.reload();
            });
        };
    }
}


angular.module('mswFrontendApp').service('accountDataService', Application.Services.AccountDataService);
