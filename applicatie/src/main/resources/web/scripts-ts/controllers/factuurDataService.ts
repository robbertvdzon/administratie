'use strict';

module Application.Services {

    import FactuurData = Application.Model.FactuurData;

    export class FactuurDataService {

        constructor(private $rootScope,private $http, private dataService:Application.Services.MyDataservice) {
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


    }
}


angular.module('mswFrontendApp').service('factuurDataService', Application.Services.FactuurDataService);
