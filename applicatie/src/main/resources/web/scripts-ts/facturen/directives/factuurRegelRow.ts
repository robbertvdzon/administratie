module Application.Directives {
    'use strict';

    import FactuurRegelData = Application.Model.FactuurRegelData;

    interface MyScope extends ng.IScope {
        factuurregel : FactuurRegelData;
    }

    class FactuurRegelRow implements ng.IDirective {

        static instance() : ng.IDirective {
            return new FactuurRegelRow;
        }

        restrict = 'E';
        templateUrl = '../facturen/directives/factuurregelrow.html';
        link(scope : MyScope, elements : ng.IAugmentedJQuery, attrs : ng.IAttributes) {
        }
    }

angular.module('mswFrontendApp').directive('factuurRegelRow', FactuurRegelRow.instance);

}
