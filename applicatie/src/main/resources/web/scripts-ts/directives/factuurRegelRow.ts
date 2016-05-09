module Application.Directives {
    'use strict';

    import FactuurRegelData = Application.Model.FactuurRegelData;

    interface IFactuurRegelRowScope extends ng.IScope {
        factuurregel : FactuurRegelData;
    }

    class FactuurRegelRow implements ng.IDirective {

        static instance() : ng.IDirective {
            return new FactuurRegelRow;
        }

        restrict = 'E';
        templateUrl = 'directives/factuurregelrow.html';
        link(scope : IFactuurRegelRowScope, elements : ng.IAugmentedJQuery, attrs : ng.IAttributes) {
        }
    }

angular.module('mswFrontendApp').directive('factuurRegelRow', FactuurRegelRow.instance);

}
