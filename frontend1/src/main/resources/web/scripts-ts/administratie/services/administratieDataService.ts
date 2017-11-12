'use strict';

module Application.Services {

    import GebruikerData = Application.Model.GebruikerData;
    import Administratie = Application.Model.Administratie;
    import AdministratieGuiService = Application.Services.AdministratieGuiService;
    import AdministratieGegevens = Application.Model.AdministratieGegevens;

    export class AdministratieDataService {
        constructor(private $rootScope,private $http, private dataService:Application.Services.MyDataservice, private administratieGuiService:AdministratieGuiService) {
        }

        public setAdministratieGegevens(administratieGegevens:AdministratieGegevens) {
            this.administratieGuiService.getAdministratieGui().data.administratieGegevens = administratieGegevens;
        }

        private getAdministratieGegevens():AdministratieGegevens{
            return this.administratieGuiService.getAdministratieGui().data.administratieGegevens;
        }

        public cloneAdministratieGegevens(administratieGegevens:AdministratieGegevens):AdministratieGegevens {
            if (administratieGegevens==null) return null;
            var administratieGegevensClone = new AdministratieGegevens();
            administratieGegevensClone.uuid = administratieGegevens.uuid;
            administratieGegevensClone.name = administratieGegevens.name;
            administratieGegevensClone.rekeningNummer = administratieGegevens.rekeningNummer;
            administratieGegevensClone.rekeningNaam = administratieGegevens.rekeningNaam;
            administratieGegevensClone.btwNummer = administratieGegevens.btwNummer;
            administratieGegevensClone.handelsRegister = administratieGegevens.handelsRegister;
            administratieGegevensClone.adres = administratieGegevens.adres;
            administratieGegevensClone.postcode = administratieGegevens.postcode;
            administratieGegevensClone.woonplaats = administratieGegevens.woonplaats;
            administratieGegevensClone.logoUrl = administratieGegevens.logoUrl;
            return administratieGegevensClone;
        }

        public saveAdministratieGegevens(): ng.IPromise<any> {
            return this.$http({
                url: "/rest/administratie",
                method: "POST",
                data: this.administratieGuiService.getAdministratieGui().data.administratieGegevensToEdit,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => {
                this.dataService.reload();
            });
        };

    }
}


angular.module('mswFrontendApp').service('administratieDataService', Application.Services.AdministratieDataService);
