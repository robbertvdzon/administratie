module Application.Directives {

    import CheckAndFixRegel = Application.Model.CheckAndFixRegel;
    'use strict';

    interface IProgressbarScope extends ng.IScope {
        regel : CheckAndFixRegel;
    }

    class CheckandfixRow implements ng.IDirective {

        static instance() : ng.IDirective {
            return new CheckandfixRow;
        }

        restrict = 'E';
        templateUrl = '../afschriften/directives/checkandfixrow.html';
        link(scope : IProgressbarScope, elements : ng.IAugmentedJQuery, attrs : ng.IAttributes) {
        }
    }

angular.module('mswFrontendApp').directive('checkandfixRow', CheckandfixRow.instance);

}
