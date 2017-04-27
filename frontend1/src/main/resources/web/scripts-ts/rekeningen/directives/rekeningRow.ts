module Application.Directives {
    import RekeningData = Application.Model.RekeningData;

    'use strict';

    interface IProgressbarScope extends ng.IScope {
        rekening : RekeningData;
    }

    class RekeningRow implements ng.IDirective {

        static instance() : ng.IDirective {
            return new RekeningRow;
        }

        restrict = 'E';
        templateUrl = '../rekeningen/directives/rekeningrow.html';
        link(scope : IProgressbarScope, elements : ng.IAugmentedJQuery, attrs : ng.IAttributes) {
        }
    }

angular.module('mswFrontendApp').directive('rekeningRow', RekeningRow.instance);

}
