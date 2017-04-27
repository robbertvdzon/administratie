module Application.Directives {
    import DeclaratieData = Application.Model.DeclaratieData;

    'use strict';

    interface IProgressbarScope extends ng.IScope {
        declaratie : DeclaratieData;
    }

    class DeclaratieRow implements ng.IDirective {

        static instance() : ng.IDirective {
            return new DeclaratieRow;
        }

        restrict = 'E';
        templateUrl = '../declaraties/directives/declaratierow.html';
        link(scope : IProgressbarScope, elements : ng.IAugmentedJQuery, attrs : ng.IAttributes) {
        }
    }

angular.module('mswFrontendApp').directive('declaratieRow', DeclaratieRow.instance);

}
