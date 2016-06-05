'use strict';

module Application.Services {

    import RekeningData = Application.Model.RekeningData;
    import Administratie = Application.Model.Administratie;
    import RekeningGuiService = Application.Services.RekeningGuiService;

    export class RekeningDataService {
        constructor(private $rootScope,private $http, private dataService:Application.Services.MyDataservice, private rekeningGuiService:RekeningGuiService, private $filter) {
        }

        private setSelectedRekening(rekening:RekeningData) {
            this.rekeningGuiService.getRekeningGui().data.selectedrekening = rekening;
            this.resetRekeningToEdit();
        }

        public resetRekeningToEdit() {
            this.rekeningGuiService.getRekeningGui().data.rekeningToEdit = this.cloneRekening(this.getSelectedRekening());
        }



        private getSelectedRekening():RekeningData{
            return this.rekeningGuiService.getRekeningGui().data.selectedrekening;
        }

        public setRekeningAsSelected(uuid) {
            var rekening:RekeningData = this.getRekeningByUuid(uuid);
            if (rekening != null) {
                rekening = this.cloneRekening(rekening);
            }
            this.rekeningGuiService.getRekeningGui().data.addMode = false;
            this.setSelectedRekening(rekening);
        }

        public createAndSelectNewRekening() {
            var rekening:RekeningData = new RekeningData();
            rekening.rekeningNummer = this.findNextRekeningnummer();
            rekening.rekeningDate = this.$filter('date')(new Date(), 'dd-MM-yyyy');
            rekening.omschrijving = "Rekening omschrijving";
            rekening.uuid = this.createUuid();
            this.rekeningGuiService.getRekeningGui().data.addMode = true;
            this.setSelectedRekening(rekening);
        }

        public getRekeningByUuid(uuid):RekeningData {
            var rekeningen:RekeningData[] = this.dataService.getData().administratie.rekeningen;
            for (var i = 0; i < rekeningen.length; i++) {
                var rekening:RekeningData = rekeningen[i];
                if (rekening.uuid === uuid) {
                    return rekening;
                }
            }
            return null;
        }

        public cloneRekening(rekening:RekeningData):RekeningData {
            if (rekening==null) return null;
            var rekeningClone = new RekeningData();
            rekeningClone.naam = rekening.naam;
            rekeningClone.uuid = rekening.uuid;
            rekeningClone.rekeningNummer = rekening.rekeningNummer;
            rekeningClone.omschrijving = rekening.omschrijving;
            rekeningClone.rekeningDate = rekening.rekeningDate;
            rekeningClone.bedragExBtw = rekening.bedragExBtw;
            rekeningClone.bedragIncBtw = rekening.bedragIncBtw;
            rekeningClone.gekoppeldAfschrift = rekening.gekoppeldAfschrift;
            rekeningClone.btw = rekening.btw;
            return rekeningClone;
        }

        public copyInto(rekeningFrom:RekeningData, rekeningTo:RekeningData) {
            rekeningTo.naam = rekeningFrom.naam;
            rekeningTo.uuid = rekeningFrom.uuid;
            rekeningTo.rekeningNummer = rekeningFrom.rekeningNummer;
            rekeningTo.omschrijving = rekeningFrom.omschrijving;
            rekeningTo.rekeningDate = rekeningFrom.rekeningDate;
            rekeningTo.bedragExBtw = rekeningFrom.bedragExBtw;
            rekeningTo.bedragIncBtw = rekeningFrom.bedragIncBtw;
            rekeningTo.btw = rekeningFrom.btw;
            rekeningTo.gekoppeldAfschrift = rekeningFrom.gekoppeldAfschrift;
        }

        public saveRekening(): ng.IPromise<any> {
            return this.$http({
                url: "/rest/rekening/",
                method: "POST",
                data: this.getSelectedRekening(),
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => {
                this.dataService.reload();
            });
        };


        public deleteRekening(): ng.IPromise<any> {
            return this.$http({
                url: "/rest/rekening/" + this.getSelectedRekening().uuid,
                method: "DELETE"
            }).then((response) => {
                this.dataService.reload();
            });
        };

        public addNewRekening(rekening:RekeningData): ng.IPromise<any> {
            return this.$http({
                url: "/rest/rekening/",
                method: "PUT",
                data: rekening,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => {
                this.dataService.reload();
            });
        };

        public addRekening(): ng.IPromise<any> {
            return this.addNewRekening(this.getSelectedRekening());
        };

        public createUuid():String {
            var d = new Date().getTime();
            var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                var r = (d + Math.random() * 16) % 16 | 0;
                d = Math.floor(d / 16);
                return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
            });
            return uuid;
        };

        public findNextRekeningnummer():string {
            var administratie:Administratie = this.dataService.getData().administratie;
            var hoogste:number = 0;
            for (var i = 0; i < administratie.rekeningen.length; i++) {
                var rekeningNr:number = parseInt(String(administratie.rekeningen[i].rekeningNummer), 10);
                if (rekeningNr > hoogste) {
                    hoogste = rekeningNr;
                }
            }
            var nieuwNummer = hoogste + 1;
            return "" + nieuwNummer;
        }
    }
}


angular.module('mswFrontendApp').service('rekeningDataService', Application.Services.RekeningDataService);
