'use strict';

module Application.Controllers {

    export class FactuurRegelData {
        uuid:String;
        omschrijving:String;
        aantal:number;
        stuksPrijs:number;
        btwPercentage:number;
    }

    export class FactuurData {
        factuurNummer:String;
        factuurRegels:FactuurRegelData[];
        uuid:String;
        betaald:boolean;
        factuurDate:String;
    }

    export class Gebruiker {
        name:String;
        username:String;
        password:String;
        uuid:String;
        facturen:FactuurData[];

    }

    interface MyScope extends ng.IScope {
        gebruiker : Gebruiker;
        selectedfactuur : FactuurData;
        newfactuur : FactuurData;
        selectedfactuurregel : FactuurRegelData;
        name: String;
        factuurTabDisabled:boolean;
        factuurRegelTabDisabled:boolean;
        selectedIndex : number;
        addRegelMode:boolean;

    }

    export class FacturenController {
        $scope:MyScope;
        $rootScope:ng.IScope;
        $http:ng.IHttpService;
        dataService:Application.Services.MyDataservice;
        $location:ng.ILocationService;
        $mdSidenav:any;

        constructor($scope, $rootScope, $http, dataService, $location, $mdSidenav) {
            this.$scope = $scope;
            this.$rootScope = $rootScope;
            this.$http = $http;
            this.dataService = dataService;
            this.$location = $location;
            this.$mdSidenav = $mdSidenav;

            this.$scope.gebruiker = new Gebruiker();
            this.$scope.selectedfactuur = new FactuurData();
            this.$scope.newfactuur = new FactuurData();

            this.$scope.factuurTabDisabled=true;
            this.$scope.factuurRegelTabDisabled=true;
            this.$scope.selectedIndex = 0;
            this.$scope.addRegelMode = false;

            this.initialize();
        }

        initialize() {
            var unregisterDataUpdatedEvent = this.$rootScope.$on('data-updated', ()=> {
                this.loadData();
            });

            this.$scope.$on("$destroy", function() {
                unregisterDataUpdatedEvent();
            });

            this.loadData();
            alert("test9");
        }

        cancelAddRegel(){
            this.showFactuurPage();
        }

        startAddRegel(ev) {
            this.$scope.selectedfactuurregel = new FactuurRegelData();
            this.$scope.selectedfactuurregel.omschrijving = "Werkzaamheden";
            this.$scope.selectedfactuurregel.aantal = 1;
            this.$scope.selectedfactuurregel.btwPercentage = 21;
            this.$scope.selectedfactuurregel.stuksPrijs = 72.5;
            this.$scope.selectedfactuurregel.uuid = "";
            this.$scope.factuurRegelTabDisabled=false;
            this.$scope.addRegelMode = true;
            this.showFactuurRegelPage();
        }

        loadData() {
            if (this.dataService.getData() != undefined) {
                this.$scope.name = this.dataService.getData().name;
                this.$scope.gebruiker = this.dataService.getData();
            }
        }

        saveAddRegel(){
            var regel = this.$scope.selectedfactuurregel;
            this.$scope.selectedfactuur.factuurRegels.push(regel);
            this.showFactuurPage();
        }

        saveEditRegel(){
            for (var i = 0; i < this.$scope.selectedfactuur.factuurRegels.length; i++) {
                var factuurRegel = this.$scope.selectedfactuur.factuurRegels[i];
                if (factuurRegel.uuid === this.$scope.selectedfactuurregel.uuid) {
                    factuurRegel.aantal = this.$scope.selectedfactuurregel.aantal;
                    factuurRegel.omschrijving = this.$scope.selectedfactuurregel.omschrijving;
                    factuurRegel.btwPercentage = this.$scope.selectedfactuurregel.btwPercentage;
                    factuurRegel.stuksPrijs = this.$scope.selectedfactuurregel.stuksPrijs;
                    factuurRegel.uuid = this.$scope.selectedfactuurregel.uuid;
                }
            }
            this.showFactuurPage();
        }


        deleteRegel(uuid) {
            var selectedNumber = -1;
            for (var i = 0; i < this.$scope.selectedfactuur.factuurRegels.length; i++) {
                var factuurRegel = this.$scope.selectedfactuur.factuurRegels[i];
                if (factuurRegel.uuid === uuid) {
                    selectedNumber = i;
                }
            }
            if (selectedNumber >= 0) {
                this.$scope.selectedfactuur.factuurRegels.splice(selectedNumber, 1);
            }
            this.$scope.factuurRegelTabDisabled=false;
            this.showFactuurPage();
        }

        editRegel(uuid) {
            for (var i = 0; i < this.$scope.selectedfactuur.factuurRegels.length; i++) {
                var factuurRegel = this.$scope.selectedfactuur.factuurRegels[i];
                if (factuurRegel.uuid === uuid) {
                    this.$scope.selectedfactuurregel = new FactuurRegelData();
                    this.$scope.selectedfactuurregel.aantal = factuurRegel.aantal;
                    this.$scope.selectedfactuurregel.omschrijving = factuurRegel.omschrijving;
                    this.$scope.selectedfactuurregel.btwPercentage = factuurRegel.btwPercentage;
                    this.$scope.selectedfactuurregel.stuksPrijs = factuurRegel.stuksPrijs;
                    this.$scope.selectedfactuurregel.uuid = factuurRegel.uuid;
                }
            }
            this.$scope.factuurRegelTabDisabled=false;
            this.$scope.addRegelMode = false;
            this.showFactuurRegelPage();
        }

        showListPage() {
            this.$scope.selectedIndex=0;
            this.$scope.factuurTabDisabled=true;
            this.$scope.factuurRegelTabDisabled=true;
        }
        showFactuurPage() {
            this.$scope.selectedIndex=1;
            this.$scope.factuurRegelTabDisabled=true;
        }
        showFactuurRegelPage() {
            this.$scope.selectedIndex=2;
        }

        edit(uuid) {
            for (var i = 0; i < this.$scope.gebruiker.facturen.length; i++) {
                var factuur = this.$scope.gebruiker.facturen[i];
                if (factuur.uuid === uuid) {
                    this.$scope.selectedfactuur = new FactuurData();
                    this.$scope.selectedfactuur.uuid = factuur.uuid;
                    this.$scope.selectedfactuur.factuurNummer = factuur.factuurNummer;
                    this.$scope.selectedfactuur.factuurRegels = factuur.factuurRegels;
                    this.$scope.selectedfactuur.betaald = factuur.betaald;
                    this.$scope.selectedfactuur.factuurDate = factuur.factuurDate;
                }
            }
            this.$scope.factuurTabDisabled=false;
            this.showFactuurPage();
        }

        save() {
            this.$http({
                url: "/rest/factuur/",
                method: "POST",
                data: this.$scope.selectedfactuur,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).success((response) => {
                this.dataService.reload();
                this.closeEditScherm();
            });
        }

        delete() {
            this.$http({
                url: "/rest/factuur/" + this.$scope.selectedfactuur.factuurNummer,
                method: "DELETE"
            }).success(
                (response)=> {
                    this.dataService.reload();
                    this.showListPage();
                });
        }

        newFactuur() {
            this.showFactuurPage();
        }


        add() {
            this.$http({
                url: "/rest/factuur/",
                method: "PUT",
                data: this.$scope.newfactuur,
            }).success(
                (response) => {
                    this.dataService.reload();
                });
            this.showListPage();
        }

        cancel() {
            this.dataService.reload();
            this.showListPage();
        }

        closeEditScherm() {
            this.showListPage();
        }

    }

}


angular.module('mswFrontendApp').controller('FacturenCtrl', Application.Controllers.FacturenController);


