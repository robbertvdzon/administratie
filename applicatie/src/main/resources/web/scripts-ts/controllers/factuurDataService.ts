'use strict';

module Application.Services {

    import FactuurData = Application.Model.FactuurData;
    import Gebruiker = Application.Model.Gebruiker;

    export class FactuurDataService {


        constructor(private $rootScope,private $http, private dataService:Application.Services.MyDataservice) {
        }

        public getFactuurByUuid(uuid):FactuurData {
            var gebruiker:Gebruiker  = this.dataService.getData();
            for (var i = 0; i < gebruiker.facturen.length; i++) {
                var factuur:FactuurData = gebruiker.facturen[i];
                if (factuur.uuid === uuid) {
                    return factuur;
                }
            }
            return null;
        }

        public cloneFactuur(factuur:FactuurData):FactuurData {
            var factuurClone = new FactuurData();
            factuurClone.uuid = factuur.uuid;
            factuurClone.factuurNummer = factuur.factuurNummer;
            factuurClone.factuurRegels = factuur.factuurRegels;
            factuurClone.betaald = factuur.betaald;
            factuurClone.factuurDate = factuur.factuurDate;
            return factuurClone;
        }


        public saveFactuur(factuur:FactuurData): ng.IPromise<any> {
            return this.$http({
                url: "/rest/factuur/",
                method: "POST",
                data: factuur,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => {
                this.dataService.reload();
            });
        };

        public deleteFactuur(factuur:FactuurData): ng.IPromise<any> {
            return this.$http({
                url: "/rest/factuur/" + factuur.factuurNummer,
                method: "DELETE"
            }).then((response) => {
                this.dataService.reload();
            });
        };

        public addFactuur(factuur:FactuurData): ng.IPromise<any> {
            return this.$http({
                url: "/rest/factuur/",
                method: "PUT",
                data: factuur,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => {
                this.dataService.reload();
            });
        };




    }
}


angular.module('mswFrontendApp').service('factuurDataService', Application.Services.FactuurDataService);
