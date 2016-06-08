'use strict';

module Application.Services {

    import AfschriftData = Application.Model.AfschriftData;
    import Administratie = Application.Model.Administratie;
    import AfschriftGuiService = Application.Services.AfschriftGuiService;
    import RubriceerRegels = Application.Model.RubriceerRegels;

    export class AfschriftDataService {
        constructor(private $rootScope, private $http, private dataService:Application.Services.MyDataservice, private afschriftGuiService:AfschriftGuiService, private $filter) {
        }

        public setRubriceerRegelLijst(rubriceerRegels:RubriceerRegels){
            this.afschriftGuiService.getAfschriftGui().data.rubriceerRegels = rubriceerRegels;
        }

        public getRubriceerRegelLijst():RubriceerRegels{
            return this.afschriftGuiService.getAfschriftGui().data.rubriceerRegels;
        }


        private setSelectedAfschrift(afschrift:AfschriftData) {
            this.afschriftGuiService.getAfschriftGui().data.selectedafschrift = afschrift;
            this.resetAfschriftToEdit();
        }

        public resetAfschriftToEdit() {
            this.afschriftGuiService.getAfschriftGui().data.afschriftToEdit = this.cloneAfschrift(this.getSelectedAfschrift());
        }


        private getSelectedAfschrift():AfschriftData {
            return this.afschriftGuiService.getAfschriftGui().data.selectedafschrift;
        }

        public setAfschriftAsSelected(uuid) {
            var afschrift:AfschriftData = this.getAfschriftByUuid(uuid);
            if (afschrift != null) {
                afschrift = this.cloneAfschrift(afschrift);
            }
            this.afschriftGuiService.getAfschriftGui().data.addMode = false;
            this.setSelectedAfschrift(afschrift);
        }

        public getAfschriftByUuid(uuid):AfschriftData {
            var afschriften:AfschriftData[] = this.dataService.getData().administratie.afschriften;
            for (var i = 0; i < afschriften.length; i++) {
                var afschrift:AfschriftData = afschriften[i];
                if (afschrift.uuid === uuid) {
                    return afschrift;
                }
            }
            return null;
        }

        public cloneAfschrift(afschrift:AfschriftData):AfschriftData {
            if (afschrift == null) return null;
            var afschriftClone = new AfschriftData();
            afschriftClone.uuid = afschrift.uuid;
            afschriftClone.rekening = afschrift.rekening;
            afschriftClone.omschrijving = afschrift.omschrijving;
            afschriftClone.relatienaam = afschrift.relatienaam;
            afschriftClone.boekdatum = afschrift.boekdatum;
            afschriftClone.bedrag = afschrift.bedrag;
            afschriftClone.boekingType = afschrift.boekingType;
            afschriftClone.factuurNummer = afschrift.factuurNummer;
            afschriftClone.rekeningNummer = afschrift.rekeningNummer;
            return afschriftClone;
        }

        public copyInto(afschriftFrom:AfschriftData, afschriftTo:AfschriftData) {
            afschriftTo.uuid = afschriftFrom.uuid;
            afschriftTo.rekening = afschriftFrom.rekening;
            afschriftTo.omschrijving = afschriftFrom.omschrijving;
            afschriftTo.relatienaam = afschriftFrom.relatienaam;
            afschriftTo.boekdatum = afschriftFrom.boekdatum;
            afschriftTo.bedrag = afschriftFrom.bedrag;
            afschriftTo.boekingType = afschriftFrom.boekingType;
            afschriftTo.factuurNummer = afschriftFrom.factuurNummer;
            afschriftTo.rekeningNummer = afschriftFrom.rekeningNummer;

        }

        public getRubriceerRegels() {
            return this.$http.get('/rest/rebriceerregels/');
        }

        public saveAfschrift():ng.IPromise<any> {
            return this.$http({
                url: "/rest/afschrift/",
                method: "POST",
                data: this.getSelectedAfschrift(),
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => {
                this.dataService.reload();
            });
        };


        public deleteAfschrift():ng.IPromise<any> {
            return this.$http({
                url: "/rest/afschrift/" + this.getSelectedAfschrift().uuid,
                method: "DELETE"
            }).then((response) => {
                this.dataService.reload();
            });
        };

        public addNewAfschrift(afschrift:AfschriftData):ng.IPromise<any> {
            return this.$http({
                url: "/rest/afschrift/",
                method: "PUT",
                data: afschrift,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => {
                this.dataService.reload();
            });
        };

        public addAfschrift():ng.IPromise<any> {
            return this.addNewAfschrift(this.getSelectedAfschrift());
        };


        public uploadFileToUrl(file, uploadUrl) {
            var fd = new FormData();
            fd.append('file', file);

            this.$http.post(uploadUrl, fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            }).then((response) => {
                    this.dataService.reload();
                })
                .error(function () {
                    alert("failed");
                });
        }

        public createUuid():String {
            var d = new Date().getTime();
            var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                var r = (d + Math.random() * 16) % 16 | 0;
                d = Math.floor(d / 16);
                return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
            });
            return uuid;
        };

        public startRubriceer() {
            return this.$http({
                url: "/rest/rubriceer/",
                method: "POST",
                data: this.getRubriceerRegelLijst(),
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            });

        }
    }
}


angular.module('mswFrontendApp').service('afschriftDataService', Application.Services.AfschriftDataService);
