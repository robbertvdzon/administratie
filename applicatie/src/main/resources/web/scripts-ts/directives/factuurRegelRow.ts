module Application.Directives {
    import FactuurRegelData = Application.Controllers.FactuurRegelData;
    'use strict';

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
