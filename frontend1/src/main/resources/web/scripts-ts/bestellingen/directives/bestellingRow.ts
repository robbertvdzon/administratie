module Application.Directives {
    import BestellingData = Application.Model.BestellingData;

    'use strict';

    interface MyScope extends ng.IScope {
        bestelling : BestellingData;
    }

    class BestellingRow implements ng.IDirective {

        static instance() : ng.IDirective {
            return new BestellingRow;
        }

        restrict = 'E';
        templateUrl = '../bestellingen/directives/bestellingrow.html';
        link(scope : MyScope, elements : ng.IAugmentedJQuery, attrs : ng.IAttributes) {
        }
    }

angular.module('mswFrontendApp').directive('bestellingRow', BestellingRow.instance);

}
