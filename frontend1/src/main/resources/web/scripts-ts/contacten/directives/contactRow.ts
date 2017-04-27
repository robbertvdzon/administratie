module Application.Directives {
    import ContactData = Application.Model.ContactData;

    'use strict';

    interface IProgressbarScope extends ng.IScope {
        contact : ContactData;
    }

    class ContactRow implements ng.IDirective {

        static instance() : ng.IDirective {
            return new ContactRow;
        }

        restrict = 'E';
        templateUrl = '../contacten/directives/contactrow.html';
        link(scope : IProgressbarScope, elements : ng.IAugmentedJQuery, attrs : ng.IAttributes) {
        }
    }

angular.module('mswFrontendApp').directive('contactRow', ContactRow.instance);

}
