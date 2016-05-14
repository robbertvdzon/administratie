'use strict';

module Application.Controllers {


    interface MyScope extends ng.IScope {
        contactTabDisabled:boolean;
        selectedIndex : number;
    }

    export class ContactenController {
        $scope:MyScope;
        $rootScope:ng.IScope;

        constructor($scope, $rootScope) {
            this.$scope = $scope;
            this.$rootScope = $rootScope;

            this.$scope.contactTabDisabled=true;
            this.$scope.selectedIndex = 0;
            this.initialize();
        }

        initialize() {

            var unregisterCloseEditEvent = this.$rootScope.$on('close-edit-contact', ()=> {
                this.showListPage();
            });

            var showFactuurScreenEvent = this.$rootScope.$on('show-contact-screen', ()=> {
                this.showContactPage();
            });

            var closeFactuurScreenEvent = this.$rootScope.$on('close-contact-screen', ()=> {
                this.showListPage();
            });



            this.$scope.$on("$destroy", function() {
                unregisterCloseEditEvent();
                showFactuurScreenEvent();
                closeFactuurScreenEvent();
            });

        }


        showListPage() {
            this.$scope.selectedIndex=0;
            this.$scope.contactTabDisabled=true;
        }
        showContactPage() {
            this.$scope.contactTabDisabled=false;
            this.$scope.selectedIndex=1;
        }
    }

}


angular.module('mswFrontendApp').controller('ContactenCtrl', Application.Controllers.ContactenController);


