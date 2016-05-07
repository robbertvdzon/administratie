'use strict';

module Application.Controllers {

    export class FactuurRegelData {
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
        $mdDialog:any;
        unregisterAddRegelFromDialogEvent;

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
            this.$scope.showEdit = false;
            this.$scope.showNew = false;
            this.$scope.showList = false;
            this.$scope.partial = false;

            this.initialize();
        }

        initialize() {
            var unregisterDataUpdatedEvent = this.$rootScope.$on('data-updated', ()=> {
                this.loadData();
            });

            var unregisterCloseAddRegelEvent = this.$rootScope.$on('closeAddRegel', ()=> {
                this.closeAddRegel();
            });

            var unregisterAddRegelFromDialogEvent = this.$rootScope.$on('addRegelFromDialog', (event, regel)=> {
                this.addRegelFromDialog(regel);
            });

            this.$scope.$on("$destroy", function() {
                unregisterDataUpdatedEvent();
                unregisterCloseAddRegelEvent();
                unregisterAddRegelFromDialogEvent();
            });

            this.loadData();
            this.showPartial('showList');
        }

        cancelAddRegel(){
            this.page2();
        }

        newFactuurregel() {
            // get factuurID
            console.log("showpart");
            this.$scope.selectedfactuurregel = new FactuurRegelData();
            this.$scope.selectedfactuurregel.omschrijving = "werkje";
            this.$scope.selectedfactuurregel.aantal = 41;
            this.$scope.selectedfactuurregel.btwPercentage = 41;
            this.$scope.selectedfactuurregel.stuksPrijs = 41;
            this.$scope.tab3Disabled=false;
            this.page3();
            //this.openDialog();
        }


        openDialog() {
            this.$mdDialog.show({
                controller: 'FacturenregelCtrl',
                controllerAs : 'facturenregelController',
                templateUrl: 'views/popup/factuurregeltemplate.html',
                parent: angular.element(document.body),
                clickOutsideToClose:true,
                locals : {
                    message : "toevoegen"
                }

            });
        }

        addRegelFromDialog(regel){
            this.$mdDialog.hide();
        }

        closeAddRegel(){
            this.$mdDialog.hide();
        }


        showPartial(partial) {
            console.log("showpart");
            this.$scope.showEdit = false;
            this.$scope.showNew = false;
            //this.$scope.showList = false;
            this.$scope[partial] = true;
        }


        loadData() {
            if (this.dataService.getData() != undefined) {
                this.$scope.name = this.dataService.getData().name;
                this.$scope.gebruiker = this.dataService.getData();
            }
        }

        //editRegel() {
        //    this.$scope.selectedfactuur.editMode = true;
        //}
        //
        //saveRegels() {
        //    this.$scope.selectedfactuur.editMode = false;
        //    var regels = this.$scope.selectedfactuur.factuurRegelsText.split('\n');
        //    this.$scope.selectedfactuur.factuurRegels = [];
        //    regels.forEach(regel => {
        //        var values = regel.split('#');
        //        if (values.length >= 4) {
        //            var aantal:number = Number(values[0]);
        //            var stuksprijs:number = Number(values[1]);
        //            var btwPerc:number = Number(values[2]);
        //            var omschrijving:String = values[3];
        //            var factuurRegel = new FactuurRegelData();
        //            factuurRegel.aantal = aantal;
        //            factuurRegel.stuksPrijs = stuksprijs;
        //            factuurRegel.btwPercentage = btwPerc;
        //            factuurRegel.omschrijving = omschrijving;
        //            this.$scope.selectedfactuur.factuurRegels.push(factuurRegel);
        //        }
        //    });
        //
        //
        //}

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
            //this.showPartial('showEdit');
            //this.$mdSidenav('editScherm').toggle();
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
            //this.$scope.selectedfactuur.editMode = false;
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
            this.page1();
            //this.closeEditScherm();
            //this.showPartial('showList');
        }

        closeEditScherm() {
            this.$mdSidenav('editScherm').close();
        }

        startAddRegel(ev) {
            this.newFactuurregel();
        }

    }

}


angular.module('mswFrontendApp').controller('FacturenCtrl', Application.Controllers.FacturenController);


