module Application.Directives {
    import AfschriftData = Application.Model.AfschriftData;

    'use strict';

    interface IProgressbarScope extends ng.IScope {
        afschrift : AfschriftData;
    }

    class AfschriftRow implements ng.IDirective {

        static instance() : ng.IDirective {
            return new AfschriftRow;
        }

        restrict = 'E';
        templateUrl = '../afschriften/directives/afschriftrow.html';
        link(scope : IProgressbarScope, elements : ng.IAugmentedJQuery, attrs : ng.IAttributes) {
        }
    }

angular.module('mswFrontendApp').directive('afschriftRow', AfschriftRow.instance);

}
