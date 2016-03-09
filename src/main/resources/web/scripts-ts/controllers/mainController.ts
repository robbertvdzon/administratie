'use strict';

module Application.Controllers {

    interface MyScope extends ng.IScope{
        version : String;
    }

    export class MainController {
        $http:ng.IHttpService;
        $scope:MyScope;

        constructor($scope, authService, $http) {
            this.$scope = $scope;
            this.$http=$http;
            this.$scope.version="wait";
            authService.checkLogin();
            this.loadVersion();
        }

        loadVersion() {
            return this.$http.get('/rest/version').success((data)=> {
                this.setVersion(data);
            }).error(()=> {
                this.setVersion("unkown version");
            });
        };

        setVersion(version:String) {
            this.$scope.version = version;
        }
    }
}

angular.module('mswFrontendApp').controller('MainCtrl', Application.Controllers.MainController);
