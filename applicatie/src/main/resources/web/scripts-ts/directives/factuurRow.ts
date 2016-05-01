module Application.Directives {
    import FactuurData = Application.Controllers.FactuurData;
    'use strict';

    interface IProgressbarScope extends ng.IScope {
        factuur : FactuurData;
    }

    class FactuurRow implements ng.IDirective {

        static instance() : ng.IDirective {
            return new FactuurRow;
        }

        restrict = 'E';
        templateUrl = 'directives/factuurrow.html';
        link(scope : IProgressbarScope, elements : ng.IAugmentedJQuery, attrs : ng.IAttributes) {
        }
    }

angular.module('mswFrontendApp').directive('factuurRow', FactuurRow.instance);

}
