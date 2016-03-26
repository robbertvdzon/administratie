'use strict';

module Application.Controllers {

    export class MenuController {

        $scope:ng.IScope;
        authService:Application.Services.AuthService;
        $http: ng.IHttpService;
        $mdSidenav:any;
        $mdDialog:any;

        constructor($scope, authService, $http, $mdSidenav, $mdDialog) {
            this.$scope = $scope;
            this.authService=authService;
            this.$http = $http;
            this.$mdSidenav = $mdSidenav;
            this.$mdDialog = $mdDialog;
        }


        isAuth() {
            return this.authService.isAuthenticated();
        }

        openLoginPopup(ev) {
            this.$mdDialog.show({
                controller: 'LoginCtrl',
                templateUrl: 'views/popup/logintemplate.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose:true
            });
        }

        logOff () {
            return this.authService.logOff();
        }

        toggleMenu() {
            this.$mdSidenav('left').toggle();
        }

    }
}

angular.module('mswFrontendApp').controller('menuCtrl', Application.Controllers.MenuController);

