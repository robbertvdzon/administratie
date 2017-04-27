module Application.Directives {
    import FactuurData = Application.Model.FactuurData;

    'use strict';

    interface MyScope extends ng.IScope {
        factuur : FactuurData;
    }

    class FactuurRow implements ng.IDirective {

        static instance() : ng.IDirective {
            return new FactuurRow;
        }

        restrict = 'E';
        templateUrl = '../facturen/directives/factuurrow.html';
        link(scope : MyScope, elements : ng.IAugmentedJQuery, attrs : ng.IAttributes) {
        }
    }

angular.module('mswFrontendApp').directive('factuurRow', FactuurRow.instance);

}
