'use strict';

module Application.Controllers {


    interface MyScope extends ng.IScope {
        factuurTabDisabled:boolean;
        factuurRegelTabDisabled:boolean;
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




            this.$scope.$on("$destroy", function() {
                unregisterCloseEditEvent();
                showFactuurScreenEvent();
                closeFactuurScreenEvent();
                showFactuurRegelScreenEvent();
                closeFactuurRegelScreenEvent();
            });

        }


        showListPage() {
            this.$scope.selectedIndex=0;
            this.$scope.factuurTabDisabled=true;
            this.$scope.factuurRegelTabDisabled=true;
        }
        showFactuurPage() {
            this.$scope.factuurTabDisabled=false;
            this.$scope.selectedIndex=1;
            this.$scope.factuurRegelTabDisabled=true;
        }
        showFactuurRegelPage() {
            this.$scope.factuurRegelTabDisabled=false;
            this.$scope.selectedIndex=2;
        }

    }

}


angular.module('mswFrontendApp').controller('FacturenCtrl', Application.Controllers.FacturenController);


