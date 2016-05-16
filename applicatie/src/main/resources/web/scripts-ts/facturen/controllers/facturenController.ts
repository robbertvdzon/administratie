'use strict';

module Application.Controllers {

    export var SCREEN_FACTUUR_LIJST = "SCREEN_FACTUUR_LIJST";
    export var SCREEN_FACTUUR_EDIT = "SCREEN_FACTUUR_EDIT";
    export var SCREEN_FACTUUR_REGEL = "SCREEN_FACTUUR_REGEL";
    export var SCREEN_FACTUUR_CONTACT = "SCREEN_FACTUUR_CONTACT";
    export var SCREEN_FACTUUR_EDIT_DETAIL = "SCREEN_FACTUUR_EDIT_DETAIL";
    export var SCREEN_FACTUUR_EDIT_CONTACT = "SCREEN_FACTUUR_EDIT_CONTACT";

    interface MyScope extends ng.IScope {
        factuurTabDisabled:boolean;
        factuurDetailTabDisabled:boolean;
        factuurRegelTabDisabled:boolean;
        searchContactTabDisabled:boolean;
        factuurEditContactTabDisabled:boolean;
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

            var showPageEvent  = this.$rootScope.$on('factuur-show-page', (event, page)=> {
                this.showPage(page);
            });

            var closePageEvent  = this.$rootScope.$on('factuur-close-page', (event, page)=> {
                this.closePage(page);
            });

            this.$scope.$on("$destroy", function() {
                showPageEvent();
                closePageEvent();
            });

        }

        showPage(page:String) {
            this.$scope.factuurTabDisabled=true;
            this.$scope.factuurRegelTabDisabled=true;
            this.$scope.searchContactTabDisabled=true;
            this.$scope.factuurDetailTabDisabled=true;
            this.$scope.factuurEditContactTabDisabled=true;

            if (page == SCREEN_FACTUUR_LIJST) {
                this.$scope.selectedIndex=0;
            }
            if (page == SCREEN_FACTUUR_EDIT) {
                this.$scope.selectedIndex=1;
                this.$scope.factuurTabDisabled=false;
            }
            if (page == SCREEN_FACTUUR_REGEL) {
                this.$scope.selectedIndex=2;
                this.$scope.factuurTabDisabled=false;
                this.$scope.factuurRegelTabDisabled=false;
            }
            if (page == SCREEN_FACTUUR_CONTACT) {
                this.$scope.selectedIndex=3;
                this.$scope.factuurTabDisabled=false;
                this.$scope.searchContactTabDisabled=false;
            }
            if (page == SCREEN_FACTUUR_EDIT_DETAIL) {
                this.$scope.selectedIndex=4;
                this.$scope.factuurTabDisabled=false;
                this.$scope.factuurDetailTabDisabled=false;
            }
            if (page == SCREEN_FACTUUR_EDIT_CONTACT) {
                this.$scope.selectedIndex=5;
                this.$scope.factuurTabDisabled=false;
                this.$scope.factuurEditContactTabDisabled=false;
            }

        }

        closePage(page:String) {
            if (page == SCREEN_FACTUUR_LIJST) {
            }
            if (page == SCREEN_FACTUUR_EDIT) {
                this.showPage(SCREEN_FACTUUR_LIJST);
            }
            if (page == SCREEN_FACTUUR_REGEL) {
                this.showPage(SCREEN_FACTUUR_EDIT);
            }
            if (page == SCREEN_FACTUUR_CONTACT) {
                this.showPage(SCREEN_FACTUUR_EDIT);
            }
            if (page == SCREEN_FACTUUR_EDIT_DETAIL) {
                this.showPage(SCREEN_FACTUUR_EDIT);
            }
            if (page == SCREEN_FACTUUR_EDIT_CONTACT) {
                this.showPage(SCREEN_FACTUUR_EDIT);
            }
        }

    }

}


angular.module('mswFrontendApp').controller('FacturenCtrl', Application.Controllers.FacturenController);


