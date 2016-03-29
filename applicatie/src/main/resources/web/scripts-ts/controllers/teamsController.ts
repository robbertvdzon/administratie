'use strict';

module Application.Controllers {

    export class GewichtData {
        gewicht: String;
        uuid: String;
    }

    export class Gebruiker {
        name: String;
        username: String;
        password: String;
        uuid: String;
    }

    interface MyScope extends ng.IScope{
        gebruiker : Gebruiker;
        name: String;
        showEdit : boolean;
        showNew : boolean;
        showList : boolean;
        partial : boolean;
    }

    export class TeamController {

        $scope:MyScope;
        $rootScope:ng.IScope;
        $http:ng.IHttpService;
        dataService:Application.Services.MyDataservice;
        $location:ng.ILocationService;

        constructor($scope, $rootScope, $http, dataService, $location) {
            this.$scope=$scope;
            this.$rootScope=$rootScope;
            this.$http=$http;
            this.dataService=dataService;
            this.$location=$location;

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
            if (this.dataService.getData() != undefined){
                this.$scope.name= this.dataService.getData().name;
                this.$scope.gebruiker= this.dataService.getData();
                //this.$scope.gewichten = this.dataService.getData().gewichten;
            }
        }

        edit(uuid) {
            //for (var i = 0; i < this.$scope.gewichten.length; i++) {
            //    var gewicht = this.$scope.gewichten[i];
            //    if (gewicht.uuid===uuid) {
            //        this.$scope.selectedgewicht = new GewichtData();
            //        this.$scope.selectedgewicht.uuid = gewicht.uuid;
            //        this.$scope.selectedgewicht.gewicht = gewicht.gewicht;
            //    }
            //}
            this.showPartial('showEdit');
        }

        save() {

            this.$http({
                url: "/rest/gebruiker/",
                method: "POST",
                data: this.$scope.gebruiker,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).success((response) => {
                this.dataService.reload();
            });
            this.showPartial('showList');


                //this.$http({
                //    method: 'POST',
                //    url: "/rest/gebruiker/",
                //    data: "gebruiker=" + encodeURIComponent(this.$scope.gebruiker),
                //    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                //}).success((data)=> {
                //    this.authService.checkLogin().success(()=>{
                //        this.$location.path('teams');
                //        this.$scope.loginfailed = false;
                //        this.$mdDialog.hide('succes');
                //    }).error(()=>{
                //        this.$scope.loginfailed = true;
                //    });
                //
                //}).error(()=> {
                //    this.$scope.loginfailed = true;
                //});


            //this.$http({
            //    url: "/rest/gebruiker/",
            //    method: "POST",
            //    params: this.$scope.gebruiker
            //}).success((response) => {
            //    this.dataService.reload();
            //});
            //this.showPartial('showList');


        }

        delete() {
            //this.$http({
            //    url: "/rest/gebruiker/"+this.$scope.selectedgewicht.uuid,
            //    method: "DELETE"
            //}).success(
            //    (response)=> {
            //        this.dataService.reload();
            //        this.showPartial('showList');
            //    });
        }

        newTeam() {
            this.showPartial('showNew');
        }


        add() {
            var newGewicht = new GewichtData();
            //newGewicht.gewicht = this.$scope.newgewicht.gewicht;
            this.$http({
                url: "/rest/gebruiker/",
                method: "PUT",
                params: newGewicht
            }).success(
                (response) => {
                    this.dataService.reload();
                });
            this.showPartial('showList');
        }

        cancel() {
            this.showPartial('showList');
        }

    }
}


angular.module('mswFrontendApp').controller('TeamsCtrl', Application.Controllers.TeamController);


