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

        public isAdmin() {
            if (this.isAuthenticated()){
                return this.authData.userData.huidigeGebruiker.admin;
            }
            return false;
        };

        public logOff() {
            this.authData.userData = undefined;
            this.$http.get('/logout').success((data)=> {
                this.$location.path('/login');
            });
        };

        public checkLogin() {
            return this.$http.get('/load').success((data) => {
                this.dataService.setData(data);
                this.authData.userData = data;
                //this.dataService.reloadGebruikers();
            }).error(()=> {
                this.dataService.userData = undefined;
                this.dataService.setData(undefined);
                this.$location.path('/login');
            });
        };
    }
}

angular.module('mswFrontendApp').service('authService', Application.Services.AuthService);
