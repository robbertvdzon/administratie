'use strict';

module Application.Controllers {


    interface MyScope extends ng.IScope {
        factuurTabDisabled:boolean;
        factuurRegelTabDisabled:boolean;
        searchContactTabDisabled:boolean;
        selectedIndex : number;

    }

    export class FacturenController {
        $scope:MyScope;
        $rootScope:ng.IScope;

        constructor($scope, $rootScope) {
            this.$scope = $scope;
            this.$rootScope = $rootScope;

            this.$scope.factuurTabDisabled=true;
            this.$scope.factuurRegelTabDisabled=true;
            this.$scope.selectedIndex = 0;
            this.initialize();
        }

        initialize() {

            var unregisterCloseEditEvent = this.$rootScope.$on('close-edit-factuur', ()=> {
                this.showListPage();
            });

            var showFactuurScreenEvent = this.$rootScope.$on('show-factuur-screen', ()=> {
                this.showFactuurPage();
            });

            var closeFactuurScreenEvent = this.$rootScope.$on('close-factuur-screen', ()=> {
                this.showListPage();
            });

            var showFactuurRegelScreenEvent = this.$rootScope.$on('show-factuurregel-screen', ()=> {
                this.showFactuurRegelPage();
            });

            var closeFactuurRegelScreenEvent = this.$rootScope.$on('close-factuurregel-screen', ()=> {
                this.showFactuurPage();
            });
            var showSearchContactScreenEvent = this.$rootScope.$on('show-search-contact-screen', ()=> {
                this.showSearchContactPage();
            });

            var closeSearchContactScreenEvent  = this.$rootScope.$on('close-search-contact-screen', ()=> {
                this.showFactuurPage();
            });

            this.$scope.$on("$destroy", function() {
                unregisterCloseEditEvent();
                showFactuurScreenEvent();
                closeFactuurScreenEvent();
                showFactuurRegelScreenEvent();
                closeFactuurRegelScreenEvent();
                showSearchContactScreenEvent();
                closeSearchContactScreenEvent();
            });

        }


        showListPage() {
            this.$scope.selectedIndex=0;
            this.$scope.factuurTabDisabled=true;
            this.$scope.factuurRegelTabDisabled=true;
            this.$scope.searchContactTabDisabled=true;
        }
        showFactuurPage() {
            this.$scope.selectedIndex=1;
            this.$scope.factuurTabDisabled=false;
            this.$scope.factuurRegelTabDisabled=true;
            this.$scope.searchContactTabDisabled=true;
        }
        showFactuurRegelPage() {
            this.$scope.selectedIndex=2;
            this.$scope.factuurRegelTabDisabled=false;
            this.$scope.searchContactTabDisabled=true;
        }
        showSearchContactPage() {
            this.$scope.selectedIndex=3;
            this.$scope.factuurRegelTabDisabled=true;
            this.$scope.searchContactTabDisabled=false;
        }

    }

}


angular.module('mswFrontendApp').controller('FacturenCtrl', Application.Controllers.FacturenController);


