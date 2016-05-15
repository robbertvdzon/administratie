'use strict';

module Application.Controllers {

    import FactuurData = Application.Model.FactuurData;
    import ContactData = Application.Model.ContactData;
    import FactuurRegelData = Application.Model.FactuurRegelData;
    import FactuurDataService = Application.Services.FactuurDataService;
    import ContactDataService = Application.Services.ContactDataService;

    interface MyScope extends ng.IScope {
        selectedfactuur : FactuurData;
        addMode : boolean;
    }

    export class FactuurEditController {

        constructor(private $scope, private $rootScope, private factuurDataService:FactuurDataService, private $filter, private contactDataService:ContactDataService) {
            this.initialize();
        }

        initialize() {
            var loadFactuurToEditEvent = this.$rootScope.$on('load-factuur-to-edit', (event, uuid: String)=> {
                this.loadExistingFactuur(uuid);
            });
            var loadFactuurToAddEvent = this.$rootScope.$on('load-factuur-to-add', ()=> {
                this.loadNewFactuur();
            });
            var addFactuurRegelEvent = this.$rootScope.$on('add-factuurregel-screen', (event, factuurregel : FactuurRegelData)=> {
                this.addFactuurRegel(factuurregel);
            });
            var updateFactuurRegelEvent = this.$rootScope.$on('update-factuurregel-screen', (event, factuurregel : FactuurRegelData)=> {
                this.updateFactuurRegel(factuurregel);
            });
            var deleteFactuurRegelEvent = this.$rootScope.$on('delete-factuurregel-screen', (event, factuurregel : FactuurRegelData)=> {
                this.deleteFactuurRegel(factuurregel);
            });
            var addFactuurRegelEvent = this.$rootScope.$on('update-contact', (event, contact : ContactData)=> {
                this.updateContact(contact);
            });

            this.$scope.$on("$destroy", function () {
                loadFactuurToEditEvent();
                loadFactuurToAddEvent();
                addFactuurRegelEvent();
                updateFactuurRegelEvent();
            });

        }

        loadExistingFactuur(uuid) {
            var factuur:FactuurData = this.factuurDataService.getFactuurByUuid(uuid);
            if (factuur != null){
                this.$scope.selectedfactuur = this.factuurDataService.cloneFactuur(factuur);
                this.$scope.addMode = false;
                this.$rootScope.$broadcast('factuur-show-page', SCREEN_FACTUUR_EDIT);
                //this.$rootScope.$broadcast('show-factuur-screen');
            }

        }

        loadNewFactuur() {
            this.$scope.selectedfactuur = new FactuurData();
            this.$scope.selectedfactuur.factuurNummer = this.factuurDataService.findNextFactuurnummer();
            this.$scope.selectedfactuur.factuurDate = this.$filter('date')(new Date(),'dd-MM-yyyy');
            this.$scope.addMode = true;
            //this.$rootScope.$broadcast('show-factuur-screen');
            this.$rootScope.$broadcast('factuur-show-page', SCREEN_FACTUUR_EDIT);
        }

        save() {
            this.factuurDataService.saveFactuur(this.$scope.selectedfactuur).then((response) => {
                //this.$rootScope.$broadcast('close-edit-factuur');
                this.$rootScope.$broadcast('factuur-close-page', SCREEN_FACTUUR_EDIT);
            }).catch((response) => {
                alert("Opslaan mislukt");
            })
        }

        add() {
            this.factuurDataService.addFactuur(this.$scope.selectedfactuur).then((response) => {
                //this.$rootScope.$broadcast('close-edit-factuur');
                this.$rootScope.$broadcast('factuur-close-page', SCREEN_FACTUUR_EDIT);
            }).catch((response) => {
                alert("Toevoegen mislukt");
            })
        }

        delete() {
            this.factuurDataService.deleteFactuur(this.$scope.selectedfactuur).then((response) => {
                //this.$rootScope.$broadcast('close-edit-factuur');
                this.$rootScope.$broadcast('factuur-close-page', SCREEN_FACTUUR_EDIT);
            }).catch((response) => {
                alert("Delete mislukt");
            })
        }

        cancel() {
            //this.$rootScope.$broadcast('close-edit-factuur');
            this.$rootScope.$broadcast('factuur-close-page', SCREEN_FACTUUR_EDIT);
        }

        addRegel(ev) {
            this.$rootScope.$broadcast('load-factuurregel-to-add');
        }

        editRegel(uuid) {
            var selectedfactuurregel:FactuurRegelData = this.factuurDataService.cloneFactuurRegel(this.factuurDataService.getRegelByUuid(this.$scope.selectedfactuur, uuid));
            this.$rootScope.$broadcast('load-factuurregel-to-edit', selectedfactuurregel, uuid);
        }

        addFactuurRegel(factuurregel:FactuurRegelData){
            this.$scope.selectedfactuur.factuurRegels.push(factuurregel);
        }

        updateFactuurRegel(factuurregel:FactuurRegelData){
            this.factuurDataService.updateFactuurRegel(this.$scope.selectedfactuur, factuurregel);
        }

        deleteFactuurRegel(factuurregel:FactuurRegelData) {
            this.factuurDataService.deleteFactuurRegel(this.$scope.selectedfactuur, factuurregel);
        }

        searchContact(){
            this.$rootScope.$broadcast('factuur-show-page', SCREEN_FACTUUR_CONTACT);
            //this.$rootScope.$broadcast('show-search-contact-screen');
        }

        private updateContact(contact:ContactData):void {
            var contactClone = this.contactDataService.cloneContact(contact);
            // TODO: genereer uuid?
            contactClone.uuid = "";
            this.$scope.selectedfactuur.klant = contactClone;
        }
    }
}


angular.module('mswFrontendApp').controller('FactuurEditController', Application.Controllers.FactuurEditController);


