'use strict';

module Application.Services {

    interface UserData {
        userData: any;
    }
    export class MyDataservice {
        userData:UserData;
        $rootScope:ng.IScope;
        $http: ng.IHttpService;

        constructor($rootScope,$http) {
            this.$rootScope = $rootScope;
            this.$http = $http;
            this.initialize();
        }

        private initialize() {
            this.userData = undefined;
            this.$rootScope.$on('new-data', (data)=> {
                this.setData(data);
            });
        }

        public setData (data) {
            this.userData = data;
            this.$rootScope.$broadcast('data-updated');
        };


        public getData = function () {
            return this.userData;
        };

        public reload() {
            return this.$http.get('/users/getcurrentadministratie').success((data)=> {
                this.setData(data);
            }).error(()=> {
                this.setData(undefined);
            });
        };
    }
}


angular.module('mswFrontendApp').service('dataService', Application.Services.MyDataservice);
