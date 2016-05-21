'use strict';

module Application.Controllers {

    interface MyScope extends ng.IScope{
        name : any;
        passwd : any;
        loginfailed : any;
    }

    export class MyController {

        constructor(private $scope, private authService, private $http, private $location, private $mdDialog) {
            $scope.name = "admin";
            $scope.passwd = "admin";
            $scope.loginfailed = false;
        }

        private hide() {
            this.$mdDialog.hide();
        }

        private login () {
            this.$http({
                method: 'POST',
                url: '/log-in',
                data: "username=" + encodeURIComponent(this.$scope.name) + "&password=" + encodeURIComponent(this.$scope.passwd),
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).success((data)=> {
                this.authService.checkLogin().success(()=>{
                    this.$location.path('facturen');
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
