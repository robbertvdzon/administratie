module Application.Directives {
    'use strict';

    import BestellingRegelData = Application.Model.BestellingRegelData;

    interface MyScope extends ng.IScope {
        bestellingregel : BestellingRegelData;
    }

    class BestellingRegelRow implements ng.IDirective {

        static instance() : ng.IDirective {
            return new BestellingRegelRow;
        }

        restrict = 'E';
        templateUrl = '../bestellingen/directives/bestellingregelrow.html';
        link(scope : MyScope, elements : ng.IAugmentedJQuery, attrs : ng.IAttributes) {
        }
    }

angular.module('mswFrontendApp').directive('bestellingRegelRow', BestellingRegelRow.instance);

}
