'use strict';

module Application.Controllers {

    interface MyScope extends ng.IScope{
        version : String;
        buildtime : String;
    }

    export class MainController {

        constructor(private $scope, private authService, private $http) {
            this.$scope.version="wait";
            this.$scope.buildtime="wait";
            authService.checkLogin();
            this.loadVersion();
            this.loadBuildtime();
        }

        loadVersion() {
            return this.$http.get('/rest/version').success((data:String)=> {
                this.setVersion(data);
            }).error(()=> {
                this.setVersion("unkown version");
            });
        };

        setVersion(version:String) {
            this.$scope.version = version;
        }

        loadBuildtime() {
            return this.$http.get('/rest/buildtime').success((data:String)=> {
                this.setBuildtime(data);
            }).error(()=> {
                this.setBuildtime("unkown buildtime");
            });
        };

        setBuildtime(buildtime:String) {
            this.$scope.buildtime = buildtime;
        }

    }


}

angular.module('mswFrontendApp').controller('MainCtrl', Application.Controllers.MainController);
