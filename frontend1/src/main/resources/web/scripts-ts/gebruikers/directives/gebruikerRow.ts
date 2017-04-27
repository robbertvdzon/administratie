module Application.Directives {
    import GebruikerData = Application.Model.GebruikerData;

    'use strict';

    interface IProgressbarScope extends ng.IScope {
        gebruiker : GebruikerData;
    }

    class GebruikerRow implements ng.IDirective {

        static instance() : ng.IDirective {
            return new GebruikerRow;
        }

        restrict = 'E';
        templateUrl = '../gebruikers/directives/gebruikerrow.html';
        link(scope : IProgressbarScope, elements : ng.IAugmentedJQuery, attrs : ng.IAttributes) {
        }
    }

angular.module('mswFrontendApp').directive('gebruikerRow', GebruikerRow.instance);

}
