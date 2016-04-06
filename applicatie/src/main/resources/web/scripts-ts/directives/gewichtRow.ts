module Application.Directives {
    import FactuurData = Application.Controllers.FactuurData;
    'use strict';

    interface IProgressbarScope extends ng.IScope {
        factuur : FactuurData;
    }

    class GewichtRow implements ng.IDirective {

        static instance() : ng.IDirective {
            return new GewichtRow;
        }

        restrict = 'E';
        templateUrl = 'directives/gewichtrow.html';
        link(scope : IProgressbarScope, elements : ng.IAugmentedJQuery, attrs : ng.IAttributes) {
        }
    }

angular.module('mswFrontendApp').directive('gewichtRow', GewichtRow.instance);

}
