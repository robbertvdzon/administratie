'use strict';

module Application.Controllers {

    interface MyScope extends ng.IScope{
        name : any;
        username : any;
        passwd : any;
        loginfailed : any;
        registerUserMode : boolean;
    }

    export class MyController {

        constructor(private $scope, private authService, private $http, private $location, private $mdDialog, private $cookies) {
            $scope.name = "";
            $scope.passwd = $cookies.get("password");
            $scope.username = $cookies.get("username");
            $scope.loginfailed = false;
            $scope.registerUserMode = false;
        }

        private startRegistreer () {
            this.$scope.registerUserMode = true;
            this.$scope.name = "";
            this.$scope.passwd = "";
            this.$scope.username = "";
        }

        private saveCookies(){
            this.$cookies.put('password', this.$scope.passwd);
            this.$cookies.put('username', this.$scope.username);
        }

        private registreer () {
            this.$http({
                method: 'POST',
                url: '/register',
                data: "name=" + encodeURIComponent(this.$scope.name) + "&username=" + encodeURIComponent(this.$scope.username) + "&password=" + encodeURIComponent(this.$scope.passwd),
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).success((data)=> {
                this.authService.checkLogin().success(()=>{
                    this.$location.path('facturen');
                    this.$scope.loginfailed = false;
                    this.$mdDialog.hide('succes');
                    this.saveCookies();

                }).error(()=>{
                });

            }).error(()=> {
                alert("Deze gebruiker bestaat al");
                this.$scope.loginfailed = true;
            });
        }

        private cancelRegistreer () {
            this.$scope.registerUserMode = false;
            this.$scope.passwd = this.$cookies.get("password");
            this.$scope.username = this.$cookies.get("username");

        }

        private login () {
            this.$http({
                method: 'POST',
                url: '/log-in',
                data: "username=" + encodeURIComponent(this.$scope.username) + "&password=" + encodeURIComponent(this.$scope.passwd),
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).success((data)=> {
                this.authService.checkLogin().success(()=>{
                    this.$location.path('facturen');
                    this.$scope.loginfailed = false;
                    this.$mdDialog.hide('succes');
                    this.saveCookies();
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
