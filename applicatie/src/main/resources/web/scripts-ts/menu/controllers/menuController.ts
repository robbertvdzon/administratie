'use strict';

module Application.Controllers {

    export class MenuController {


        constructor(private $scope, private authService, private $mdSidenav, private $mdDialog, private $rootScope) {
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

        public mainFacturenButtonPressed(){
            this.$rootScope.$broadcast('facturen-main-button-pressed');
        }

        public mainBestellingenButtonPressed(){
            this.$rootScope.$broadcast('bestellingen-main-button-pressed');
        }

        public mainRekeningenButtonPressed(){
            this.$rootScope.$broadcast('rekeningen-main-button-pressed');
        }

        public mainDeclaratiesButtonPressed(){
            this.$rootScope.$broadcast('declaraties-main-button-pressed');
        }

        public mainAfschriftenButtonPressed(){
            this.$rootScope.$broadcast('afschriften-main-button-pressed');
        }

        public mainContactenButtonPressed(){
            this.$rootScope.$broadcast('contacten-main-button-pressed');
        }

        public mainOverzichtenButtonPressed(){
            this.$rootScope.$broadcast('overzichten-main-button-pressed');
        }

        public mainGebruikersButtonPressed(){
            this.$rootScope.$broadcast('gebruikers-main-button-pressed');
        }

    }
}

angular.module('mswFrontendApp').controller('menuCtrl', Application.Controllers.MenuController);

