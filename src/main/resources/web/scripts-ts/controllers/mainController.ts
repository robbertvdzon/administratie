'use strict';

module Application.Controllers {

    export class MainController {

        constructor(authService) {
            authService.checkLogin();
        }
    }
}

angular.module('mswFrontendApp').controller('MainCtrl', Application.Controllers.MainController);
