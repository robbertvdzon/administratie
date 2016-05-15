'use strict';

module Application.Controllers {

    import FactuurData = Application.Model.FactuurData;
    import FactuurRegelData = Application.Model.FactuurRegelData;
    import Administratie = Application.Model.Administratie;
    import FactuurDataService = Application.Services.FactuurDataService;


    interface MyScope extends ng.IScope {
        administratie : Administratie;

    }

    export class SearchContactController {
        $scope:MyScope;
        $rootScope:ng.IScope;
        dataService:Application.Services.MyDataservice;

        constructor($scope, $rootScope, dataService, private factuurDataService:FactuurDataService) {
            this.$scope = $scope;
            this.$rootScope = $rootScope;
            this.dataService = dataService;
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

        loadData() {
            if (this.dataService.getData() != undefined) {
                this.$scope.administratie = this.dataService.getData();
            }
        }


        selectContact(uuid: String) {
            this.$rootScope.$broadcast('update-contact', this.factuurDataService.getContactByUuid(uuid));
            this.$rootScope.$broadcast('factuur-close-page', SCREEN_FACTUUR_CONTACT);
            //this.$rootScope.$broadcast('close-search-contact-screen');
        }


        cancel() {
            this.$rootScope.$broadcast('factuur-close-page', SCREEN_FACTUUR_CONTACT);
            //this.$rootScope.$broadcast('close-search-contact-screen');
        }



    }

}


angular.module('mswFrontendApp').controller('SearchContactController', Application.Controllers.SearchContactController);


