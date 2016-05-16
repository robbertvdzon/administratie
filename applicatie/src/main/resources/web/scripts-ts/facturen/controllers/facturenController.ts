'use strict';

module Application.Controllers {

    import FactuurGuiService = Application.Services.FactuurGuiService;
    import FactuurGui = Application.Services.FactuurGui;

    //export var SCREEN_FACTUUR_LIJST = "SCREEN_FACTUUR_LIJST";
    //export var SCREEN_FACTUUR_EDIT = "SCREEN_FACTUUR_EDIT";
    //export var SCREEN_FACTUUR_REGEL = "SCREEN_FACTUUR_REGEL";
    //export var SCREEN_FACTUUR_CONTACT = "SCREEN_FACTUUR_CONTACT";
    //export var SCREEN_FACTUUR_EDIT_DETAIL = "SCREEN_FACTUUR_EDIT_DETAIL";
    //export var SCREEN_FACTUUR_EDIT_CONTACT = "SCREEN_FACTUUR_EDIT_CONTACT";

    interface MyScope extends ng.IScope {
        factuurGui:FactuurGui;
    }

    export class FacturenController {
        $scope:MyScope;
        $rootScope:ng.IScope;

        constructor($scope, $rootScope, factuurGuiService:FactuurGuiService) {
            this.$scope = $scope;
            this.$rootScope = $rootScope;
            this.$scope.factuurGui = factuurGuiService.getFactuurGui();
            this.initialize();
        }

        initialize() {

        }
        //
        //showPage(page:String) {
        //    this.$scope.factuurGui.factuurTabDisabled=true;
        //    this.$scope.factuurGui.factuurRegelTabDisabled=true;
        //    this.$scope.factuurGui.searchContactTabDisabled=true;
        //    this.$scope.factuurGui.factuurDetailTabDisabled=true;
        //    this.$scope.factuurGui.factuurEditContactTabDisabled=true;
        //
        //    if (page == SCREEN_FACTUUR_LIJST) {
        //        this.$scope.factuurGui.selectedIndex=0;
        //    }
        //    if (page == SCREEN_FACTUUR_EDIT) {
        //        this.$scope.factuurGui.selectedIndex=1;
        //        this.$scope.factuurGui.factuurTabDisabled=false;
        //    }
        //    if (page == SCREEN_FACTUUR_REGEL) {
        //        this.$scope.factuurGui.selectedIndex=2;
        //        this.$scope.factuurGui.factuurTabDisabled=false;
        //        this.$scope.factuurGui.factuurRegelTabDisabled=false;
        //    }
        //    if (page == SCREEN_FACTUUR_CONTACT) {
        //        this.$scope.factuurGui.selectedIndex=3;
        //        this.$scope.factuurGui.factuurTabDisabled=false;
        //        this.$scope.factuurGui.searchContactTabDisabled=false;
        //    }
        //    if (page == SCREEN_FACTUUR_EDIT_DETAIL) {
        //        this.$scope.factuurGui.selectedIndex=4;
        //        this.$scope.factuurGui.factuurTabDisabled=false;
        //        this.$scope.factuurGui.factuurDetailTabDisabled=false;
        //    }
        //    if (page == SCREEN_FACTUUR_EDIT_CONTACT) {
        //        this.$scope.factuurGui.selectedIndex=5;
        //        this.$scope.factuurGui.factuurTabDisabled=false;
        //        this.$scope.factuurGui.factuurEditContactTabDisabled=false;
        //    }
        //
        //}
        //
        //closePage(page:String) {
        //    if (page == SCREEN_FACTUUR_LIJST) {
        //    }
        //    if (page == SCREEN_FACTUUR_EDIT) {
        //        this.showPage(SCREEN_FACTUUR_LIJST);
        //    }
        //    if (page == SCREEN_FACTUUR_REGEL) {
        //        this.showPage(SCREEN_FACTUUR_EDIT);
        //    }
        //    if (page == SCREEN_FACTUUR_CONTACT) {
        //        this.showPage(SCREEN_FACTUUR_EDIT);
        //    }
        //    if (page == SCREEN_FACTUUR_EDIT_DETAIL) {
        //        this.showPage(SCREEN_FACTUUR_EDIT);
        //    }
        //    if (page == SCREEN_FACTUUR_EDIT_CONTACT) {
        //        this.showPage(SCREEN_FACTUUR_EDIT);
        //    }
        //}

    }

}


angular.module('mswFrontendApp').controller('FacturenCtrl', Application.Controllers.FacturenController);


