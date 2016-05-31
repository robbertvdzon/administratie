'use strict';
/// <reference path="../../lib/typings/main.d.ts" />
/**
 * @ngdoc overview
 * @name mswFrontendApp
 * @description
 * # mswFrontendApp
 *
 * Main module of the application.
 */
angular
    .module('mswFrontendApp', [
        'ngAnimate',
        'ngCookies',
        'ngResource',
        'ngRoute',
        'ngSanitize',
        'ngTouch',
        'ngMaterial'
    ])
    .config(function ($mdThemingProvider, $mdIconProvider, $routeProvider) {
        $mdIconProvider
            .defaultIconSet("./svg/avatars.svg", 128)
            .icon("menu", "./svg/menu.svg", 24)
            .icon("share", "./svg/share.svg", 24)
            .icon("google_plus", "./svg/google_plus.svg", 512)
            .icon("hangouts", "./svg/hangouts.svg", 512)
            .icon("twitter", "./svg/twitter.svg", 512)
            .icon("phone", "./svg/phone.svg", 512);
        $mdThemingProvider.theme('default')
            .primaryPalette('teal')
            .accentPalette('grey');
        $routeProvider
            .when('/', {
                templateUrl: 'menu/views/main.html',
                controller: 'MainCtrl'
            })
            .when('/login', {
                templateUrl: 'login/views/main.html',
                controller: 'LoginCtrl'
            })
            .when('/facturen', {
                templateUrl: 'facturen/views/main.html',
                controller: 'FacturenCtrl',
                controllerAs: 'facturencontroller'
            })
            .when('/contacten', {
                templateUrl: 'contacten/views/main.html',
                controller: 'ContactenCtrl',
                controllerAs: 'contactencontroller'
            })
            .when('/gebruikers', {
                templateUrl: 'gebruikers/views/main.html',
                controller: 'GebruikersCtrl',
                controllerAs: 'gebruikerscontroller'
            })
            .when('/administratie', {
                templateUrl: 'administratie/views/main.html',
                controller: 'AdministratieEditController',
                controllerAs: 'administratieEditController'
            })
            .when('/account', {
                templateUrl: 'account/views/main.html',
                controller: 'AccountCtrl',
                controllerAs: 'accountcontroller'
            })
            .when('/rekeningen', {
                templateUrl: 'rekeningen/views/main.html',
                controller: 'RekeningenCtrl',
                controllerAs: 'rekeningencontroller'
            })
            .when('/declaraties', {
                templateUrl: 'declaraties/views/main.html',
                controller: 'DeclaratiesCtrl',
                controllerAs: 'declaratiescontroller'
            })
            .when('/afschriften', {
                templateUrl: 'afschriften/views/main.html',
                controller: 'AfschriftenCtrl',
                controllerAs: 'afschriftencontroller'
            })
            .when('/bestellingen', {
                templateUrl: 'bestellingen/views/main.html',
                controller: 'BestellingenCtrl',
                controllerAs: 'bestellingencontroller'
            })
            .otherwise({
                redirectTo: '/'
            });
    });
