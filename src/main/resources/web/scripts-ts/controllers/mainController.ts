'use strict';

module Application.Controllers {

    interface MyScope extends ng.IScope{
        version : String;
        buildtime : String;
    }

    export class MainController {
        $http:ng.IHttpService;
        $scope:MyScope;

        constructor($scope, authService, $http) {
            this.$scope = $scope;
            this.$http=$http;
            this.$scope.version="wait";
            this.$scope.buildtime="wait";
            authService.checkLogin();
            this.loadVersion();
            this.loadBuildtime();
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

        loadBuildtime() {
            return this.$http.get('/rest/buildtime').success((data)=> {
                this.setBuildtime(data);
            }).error(()=> {
                this.setBuildtime("unkown buildtime");
            });
        };

        setBuildtime(version:String) {
            this.$scope.version = version;
        }

    }


}

angular.module('mswFrontendApp').controller('MainCtrl', Application.Controllers.MainController);
