'use strict';

module Application.Controllers {

    interface MyScope extends ng.IScope{
        name : any;
        passwd : any;
        loginfailed : any;
    }

    export class MyController {
        $scope:MyScope;
        authService:Application.Services.AuthService;
        $http: ng.IHttpService;
        $location: ng.ILocationService;
        $mdDialog:any;

        constructor($scope, authService, $http, $location, $mdDialog) {
            this.$scope = $scope;
            this.authService = authService;
            this.$http = $http;
            this.$location = $location;
            this.$mdDialog = $mdDialog;

            $scope.name = "q";
            $scope.passwd = "q";
            $scope.loginfailed = false;
        }

        private hide() {
            this.$mdDialog.hide();
        }

        private login () {
            this.$http({
                method: 'POST',
                url: '/login',
                data: "username=" + encodeURIComponent(this.$scope.name) + "&password=" + encodeURIComponent(this.$scope.passwd),
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).success((data)=> {
                this.authService.checkLogin().success(()=>{
                    this.$location.path('teams');
                    this.$scope.loginfailed = false;
                    this.$mdDialog.hide('succes');
                }).error(()=>{
                    this.$scope.loginfailed = true;
                });

            }).error(()=> {
                this.$scope.loginfailed = true;
            });
        }
    }
}

angular.module('mswFrontendApp').controller('LoginCtrl', Application.Controllers.MyController);
