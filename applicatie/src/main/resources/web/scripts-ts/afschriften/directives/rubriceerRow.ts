module Application.Directives {

    import RubriceerRegel = Application.Model.RubriceerRegel;
    'use strict';

    interface IProgressbarScope extends ng.IScope {
        regel : RubriceerRegel;
    }

    class RubriceerRow implements ng.IDirective {

        static instance() : ng.IDirective {
            return new RubriceerRow;
        }

        restrict = 'E';
        templateUrl = '../afschriften/directives/rubriceerrow.html';
        link(scope : IProgressbarScope, elements : ng.IAugmentedJQuery, attrs : ng.IAttributes) {
        }
    }

angular.module('mswFrontendApp').directive('rubriceerRow', RubriceerRow.instance);

}
