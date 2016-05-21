'use strict';

module Application.Controllers {

    export class MenuController {


        constructor(private $scope, private authService, private $mdSidenav, private $mdDialog) {
        }


        isAdmin() {
            return this.authService.isAdmin();
        }

        isAuth() {
            return this.authService.isAuthenticated();
        }

        openLoginPopup(ev) {
            this.$mdDialog.show({
                controller: 'LoginCtrl',
                templateUrl: 'menu/views/popup/logintemplate.html',
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

