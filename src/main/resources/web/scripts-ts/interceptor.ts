"use strict";

module Application.Controllers {

    //Method name should be exactly "response" - http://docs.angularjs.org/api/ng/service/$http
    export interface IInterceptor {
        request: Function;
        requestError: Function;
        response: Function;
        responseError: Function;
    }

    export class AuthenticationInterceptor implements IInterceptor {
        public static $inject = ["$injector", "$q"];

        public static Factory($injector: ng.auto.IInjectorService, $q: ng.IQService) {
            return new AuthenticationInterceptor($injector, $q);
        }

        constructor(private $injector: ng.auto.IInjectorService, private $q: ng.IQService) {
            //console.log("initializing AuthenticationInterceptor");
        }

        public request = (requestSuccess): ng.IPromise<any> => {
            //console.log("intercepting request:::"+JSON.stringify(requestSuccess));
            return requestSuccess;
        }
        public requestError = (requestFailure): ng.IPromise<any> => {
            //console.log("requestError reported");
            return requestFailure;
        }
        public response = (responseSuccess): ng.IPromise<any> => {
            //console.log("success response reported with status: " + responseSuccess.status);
            //console.log("response: ", responseSuccess);
            return responseSuccess;
        }
        public responseError = (responseFailure): ng.IPromise<any> => {
            //console.log("response Error reported");
            if (responseFailure.status === 401) {
                //console.log("401 error");
            }
            return this.$q.reject(responseFailure);
        }
    }
}

angular.module('mswFrontendApp').config(['$httpProvider', function ($httpProvider: ng.IHttpProvider) {
    $httpProvider.interceptors.push(Application.Controllers.AuthenticationInterceptor.Factory);
}])
