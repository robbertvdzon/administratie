'use strict';

module Application.Services {

    import FactuurData = Application.Model.FactuurData;

    export class FactuurGuiService {

        constructor(private $rootScope,private $http, private dataService:Application.Services.MyDataservice) {
        }

        public editFactuur(factuur:FactuurData) {
        };



    }
}


angular.module('mswFrontendApp').service('factuurDataService', Application.Services.FactuurDataService);
