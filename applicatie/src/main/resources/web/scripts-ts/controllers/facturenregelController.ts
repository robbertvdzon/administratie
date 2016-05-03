'use strict';

module Application.Controllers {

    export class FactuurRegelData {
        omschrijving:String;
        aantal:number;
        stuksPrijs:number;
        btwPercentage:number;
    }


    interface MyScope extends ng.IScope {
        selectedfactuurregel : FactuurRegelData;
        okButtonText:String;
    }

    export class FacturenregelController {

        $scope:MyScope;
        $rootScope:ng.IScope;
        $http:ng.IHttpService;
        dataService:Application.Services.MyDataservice;
        $location:ng.ILocationService;
        $mdSidenav:any;
        $mdDialog:any;

        constructor($scope, $rootScope, $http, dataService, $location, $mdSidenav, $mdDialog, message) {
            this.$scope = $scope;
            this.$rootScope = $rootScope;
            this.$http = $http;
            this.dataService = dataService;
            this.$location = $location;
            this.$mdSidenav = $mdSidenav;
            this.$mdDialog = $mdDialog;
            this.$scope.okButtonText = message;

            this.initialize();
        }

        initialize() {
            this.$scope.selectedfactuurregel = new FactuurRegelData();
            this.$scope.selectedfactuurregel.omschrijving = "werkje2";
            this.$scope.selectedfactuurregel.aantal = 4;
            this.$scope.selectedfactuurregel.stuksPrijs = 72;
            this.$scope.selectedfactuurregel.btwPercentage = 21;
        }

        addFactuurRegel(){
            this.$rootScope.$broadcast("addRegelFromDialog", this.$scope.selectedfactuurregel);

        }

        cancelAddRegel(){
            this.$rootScope.$broadcast("closeAddRegel");
        }

    }

}


angular.module('mswFrontendApp').controller('FacturenregelCtrl', Application.Controllers.FacturenregelController);


