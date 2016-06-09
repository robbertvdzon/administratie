'use strict';

module Application.Controllers {

    import AdministratieGegevens = Application.Model.AdministratieGegevens;
    import AdministratieDataService = Application.Services.AdministratieDataService;
    import AdministratieGuiService = Application.Services.AdministratieGuiService;
    import AdministratieGuiData = Application.Services.AdministratieGuiData;

    interface MyScope extends ng.IScope {
        jaar:String;
    }

    export class OverzichtController {

        constructor(private $scope:MyScope, private $http) {
        $scope.jaar= "2016";
        }


        public dowload(){
            alert("download "+this.$scope.jaar);
        }


    }
}


angular.module('mswFrontendApp').controller('OverzichtController', Application.Controllers.OverzichtController);


