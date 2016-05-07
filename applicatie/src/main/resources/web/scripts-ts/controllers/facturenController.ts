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
        tab2Disabled:boolean;
        tab3Disabled:boolean;
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
        $mdDialog:any;

        constructor($scope, $rootScope, $http, dataService, $location, $mdSidenav, $mdDialog) {
            this.$scope = $scope;
            this.$rootScope = $rootScope;
            this.$http = $http;
            this.dataService = dataService;
            this.$location = $location;
            this.$mdSidenav = $mdSidenav;
            this.$mdDialog = $mdDialog;

            this.$scope.gebruiker = new Gebruiker();
            this.$scope.selectedfactuur = new FactuurData();
            this.$scope.newfactuur = new FactuurData();

            this.$scope.tab2Disabled=true;
            this.$scope.tab3Disabled=true;
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
        }

        cancelAddRegel(){
            this.page2();
        }

        startAddRegel(ev) {
            this.$scope.selectedfactuurregel = new FactuurRegelData();
            this.$scope.selectedfactuurregel.omschrijving = "werkje jan";
            this.$scope.selectedfactuurregel.aantal = 41;
            this.$scope.selectedfactuurregel.btwPercentage = 21;
            this.$scope.selectedfactuurregel.stuksPrijs = 72;
            this.$scope.selectedfactuurregel.uuid = "";
            this.$scope.tab3Disabled=false;
            this.$scope.addRegelMode = true;
            this.page3();
        }



        addRegelFromDialog(regel){
            this.$mdDialog.hide();
        }

        closeAddRegel(){
            this.$mdDialog.hide();
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
            //this.$http({
            //    url: "/rest/factuur/",
            //    method: "POST",
            //    data: this.$scope.selectedfactuur,
            //    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            //}).success((response) => {
            //    this.page2();
            //});
                this.page2();
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

            //this.$http({
            //    url: "/rest/factuur/",
            //    method: "POST",
            //    data: this.$scope.selectedfactuur,
            //    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            //}).success((response) => {
            //    this.page2();
            //});
            this.page2();
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
            this.$scope.tab3Disabled=false;
            this.page2();
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
            this.$scope.tab3Disabled=false;
            this.$scope.addRegelMode = false;
            this.page3();
        }

        page1() {
            this.$scope.selectedIndex=0;
            this.$scope.tab2Disabled=true;
            this.$scope.tab3Disabled=true;
        }
        page2() {
            this.$scope.selectedIndex=1;
            this.$scope.tab3Disabled=true;
        }
        page3() {
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
            this.$scope.tab2Disabled=false;
            this.page2();
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
                    this.page1();
                });
        }

        newFactuur() {
            this.page2();
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
            this.page1();
        }

        cancel() {
            this.dataService.reload();
            this.page1();
        }

        closeEditScherm() {
            this.page1();
        }


    }

}


angular.module('mswFrontendApp').controller('FacturenCtrl', Application.Controllers.FacturenController);


