'use strict';

module Application.Services {

    import GuiData = Application.Model.GuiData;
    export class MyDataservice {
        userData:any;
        gebruikers:any;
        $rootScope:ng.IScope;
        $http: ng.IHttpService;

        constructor($rootScope,$http) {
            this.$rootScope = $rootScope;
            this.$http = $http;
            this.initialize();
        }

        private initialize() {
            this.userData = undefined;
            this.gebruikers = undefined;
        }

        public setData (data: any) {
            this.userData = data;
            this.$rootScope.$broadcast('data-updated', data);
        };

        public getData = function (): GuiData {
            return this.userData;
        };

        public reload() {
            return this.$http.get('/load').success((data)=> {
                this.setData(data);
            }).error(()=> {
                this.setData(undefined);
            });
        };

    }
}


angular.module('mswFrontendApp').service('dataService', Application.Services.MyDataservice);
