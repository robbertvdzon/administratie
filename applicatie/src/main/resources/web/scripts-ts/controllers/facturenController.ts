'use strict';

module Application.Controllers {

    export class FactuurRegelData {
        omschrijving:String;
        aantal:number;
        stuksPrijs:number;
        btwPercentage:number;
    }

    export class FactuurData {
        omschrijving:String;
        factuurNummer:String;
        factuurRegels:FactuurRegelData[];
        factuurRegelsText:String;
        uuid:String;
        editMode:boolean;
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
        name: String;
        showEdit : boolean;
        showNew : boolean;
        showList : boolean;
        partial : boolean;
    }

    export class FacturenController {

        $scope:MyScope;
        $rootScope:ng.IScope;
        $http:ng.IHttpService;
        dataService:Application.Services.MyDataservice;
        $location:ng.ILocationService;
        $mdSidenav:any;
        //$mdSidenav:angular.material.MDSidenavObject;

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
            this.$scope.showEdit = false;
            this.$scope.showNew = false;
            this.$scope.showList = false;
            this.$scope.partial = false;

            this.initialize();
        }

        initialize() {
            this.$rootScope.$on('data-updated', ()=> {
                this.loadData();
            });
            this.loadData();
            this.showPartial('showList');
        }

        showPartial(partial) {
            console.log("showpart");
            this.$scope.showEdit = false;
            this.$scope.showNew = false;
            this.$scope.showList = false;
            this.$scope[partial] = true;
        }


        loadData() {
            if (this.dataService.getData() != undefined) {
                this.$scope.name = this.dataService.getData().name;
                this.$scope.gebruiker = this.dataService.getData();
                //this.$scope.gewichten = this.dataService.getData().gewichten;
            }
        }

        editRegel() {
            this.$scope.selectedfactuur.editMode = true;
        }

        saveRegels() {
            this.$scope.selectedfactuur.editMode = false;
            var regels = this.$scope.selectedfactuur.factuurRegelsText.split('\n');
            this.$scope.selectedfactuur.factuurRegels = [];
            regels.forEach(regel => {
                var values = regel.split('#');
                if (values.length >= 4) {
                    var aantal:number = Number(values[0]);
                    var stuksprijs:number = Number(values[1]);
                    var btwPerc:number = Number(values[2]);
                    var omschrijving:String = values[3];
                    var factuurRegel = new FactuurRegelData();
                    factuurRegel.aantal = aantal;
                    factuurRegel.stuksPrijs = stuksprijs;
                    factuurRegel.btwPercentage = btwPerc;
                    factuurRegel.omschrijving = omschrijving;
                    this.$scope.selectedfactuur.factuurRegels.push(factuurRegel);
                }
            });


        }


        edit(uuid) {
            for (var i = 0; i < this.$scope.gebruiker.facturen.length; i++) {
                var factuur = this.$scope.gebruiker.facturen[i];
                if (factuur.uuid === uuid) {
                    this.$scope.selectedfactuur = new FactuurData();
                    this.$scope.selectedfactuur.uuid = factuur.uuid;
                    this.$scope.selectedfactuur.omschrijving = factuur.omschrijving;
                    this.$scope.selectedfactuur.factuurNummer = factuur.factuurNummer;
                    this.$scope.selectedfactuur.factuurRegels = factuur.factuurRegels;
                    this.$scope.selectedfactuur.factuurRegelsText = "";
                    this.$scope.selectedfactuur.editMode = true;
                    if (factuur.factuurRegels != null) {
                        for (var j = 0; j < factuur.factuurRegels.length; j++) {
                            this.$scope.selectedfactuur.factuurRegelsText += factuur.factuurRegels[j].aantal;
                            this.$scope.selectedfactuur.factuurRegelsText += " # ";
                            this.$scope.selectedfactuur.factuurRegelsText += factuur.factuurRegels[j].stuksPrijs;
                            this.$scope.selectedfactuur.factuurRegelsText += " # ";
                            this.$scope.selectedfactuur.factuurRegelsText += factuur.factuurRegels[j].btwPercentage;
                            this.$scope.selectedfactuur.factuurRegelsText += " # ";
                            this.$scope.selectedfactuur.factuurRegelsText += factuur.factuurRegels[j].omschrijving;
                            this.$scope.selectedfactuur.factuurRegelsText += "\n";
                            this.$scope.selectedfactuur.editMode = false;
                        }
                    }
                }
            }
            this.$mdSidenav('editScherm').toggle();
        }

        save() {
            this.$http({
                url: "/rest/factuur/",
                method: "POST",
                data: this.$scope.selectedfactuur,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).success((response) => {
                this.dataService.reload();
            });
            this.closeEditScherm();
            //this.showPartial('showList');
            this.$scope.selectedfactuur.editMode = false;
        }

        delete() {
            this.$http({
                url: "/rest/factuur/" + this.$scope.selectedfactuur.factuurNummer,
                method: "DELETE"
            }).success(
                (response)=> {
                    this.dataService.reload();
                    this.showPartial('showList');
                });
        }

        newFactuur() {
            this.showPartial('showNew');
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
            this.showPartial('showList');
        }

        cancel() {
            this.closeEditScherm();
            //this.showPartial('showList');
        }

        closeEditScherm() {
            this.$mdSidenav('editScherm').close();
        }
    }

}


angular.module('mswFrontendApp').controller('FacturenCtrl', Application.Controllers.FacturenController);


