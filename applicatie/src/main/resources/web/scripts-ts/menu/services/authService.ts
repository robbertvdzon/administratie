'use strict';

module Application.Services {

    class AuthData {
        userData: any;
    }
    export class AuthService {

        authData : AuthData;
        $http: ng.IHttpService;
        $location: any;
        dataService: Application.Services.MyDataservice;

        constructor($http, $location, dataService) {
            this.$http = $http;
            this.$location = $location;
            this.dataService = dataService;
            this.authData = new AuthData();
        }

        public isAuthenticated() {
            return !angular.isUndefined(this.authData.userData);
        };

        public logOff() {
            this.authData.userData = undefined;
            this.$http.get('/logout').success((data)=> {
                this.$location.path('/');
            });
        };

        public checkLogin() {
            return this.$http.get('/users/getcurrentadministratie').success((data) => {
                this.dataService.setData(data);
                this.authData.userData = data;
            }).error(()=> {
                this.dataService.userData = undefined;
                this.dataService.setData(undefined);
                this.$location.path('/');
            });
        };
    }
}

angular.module('mswFrontendApp').service('authService', Application.Services.AuthService);
