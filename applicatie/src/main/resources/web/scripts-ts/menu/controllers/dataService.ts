'use strict';

module Application.Services {

    //import Administratie = Application.Model.Administratie;
    //interface UserData {
    //    userData: Administratie;
    //}
    export class MyDataservice {
        userData:any;
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

        public setData (data: any) {
            this.userData = data;
            alert("send broadcast "+data.facturen.length);
            this.$rootScope.$broadcast('data-updated', data);
        };


        public getData = function (): any {
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
