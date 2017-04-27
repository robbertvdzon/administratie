'use strict';

module Application.Controllers {

    import AdministratieGegevens = Application.Model.AdministratieGegevens;
    import AdministratieDataService = Application.Services.AdministratieDataService;
    import AdministratieGuiService = Application.Services.AdministratieGuiService;
    import AdministratieGuiData = Application.Services.AdministratieGuiData;

    interface MyScope extends ng.IScope {
        beginDate:String;
        endDate:String;
    }

    export class OverzichtController {

        constructor(private $scope:MyScope, private $http, private $filter, private $window) {
            $scope.beginDate= "01-01-"+this.$filter('date')(new Date(), 'yyyy');
            $scope.endDate= "31-12-"+this.$filter('date')(new Date(), 'yyyy');
        }


        public dowload(){
            this.$window.open('/rest/overzicht/pdf/'+this.$scope.beginDate+"/"+this.$scope.endDate, '_blank');
        }


    }
}


angular.module('mswFrontendApp').controller('OverzichtController', Application.Controllers.OverzichtController);


