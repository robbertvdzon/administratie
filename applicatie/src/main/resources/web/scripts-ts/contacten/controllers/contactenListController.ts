'use strict';

module Application.Controllers {

    import ContactData = Application.Model.ContactData;
    import Administratie = Application.Model.Administratie;

    interface MyScope extends ng.IScope {
        administratie : Administratie;
    }

    export class ContactenListController {
        $scope:MyScope;
        $rootScope:ng.IScope;
        dataService:Application.Services.MyDataservice;

        constructor($scope, $rootScope, dataService) {
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


        edit(uuid: String) {
            this.$rootScope.$broadcast('load-contact-to-edit', uuid);
        }


        newFactuur() {
            this.$rootScope.$broadcast('load-contact-to-add');
        }



    }

}


angular.module('mswFrontendApp').controller('ContactenListController', Application.Controllers.ContactenListController);


