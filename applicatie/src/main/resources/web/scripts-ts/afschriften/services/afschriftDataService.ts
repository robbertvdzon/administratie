'use strict';

module Application.Services {

    import AfschriftData = Application.Model.AfschriftData;
    import Administratie = Application.Model.Administratie;
    import AfschriftGuiService = Application.Services.AfschriftGuiService;

    export class AfschriftDataService {
        constructor(private $rootScope, private $http, private dataService:Application.Services.MyDataservice, private afschriftGuiService:AfschriftGuiService, private $filter) {
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

        public createAndSelectNewAfschrift() {
            var afschrift:AfschriftData = new AfschriftData();
            afschrift.boekdatum = this.$filter('date')(new Date(), 'dd-MM-yyyy');
            afschrift.uuid = this.createUuid();
            this.afschriftGuiService.getAfschriftGui().data.addMode = true;
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
            afschriftClone.rekeningnaam = afschrift.rekeningnaam;
            afschriftClone.relatienaam = afschrift.relatienaam;
            afschriftClone.boekdatum = afschrift.boekdatum;
            afschriftClone.bedrag = afschrift.bedrag;
            return afschriftClone;
        }

        public copyInto(afschriftFrom:AfschriftData, afschriftTo:AfschriftData) {
            afschriftTo.uuid = afschriftFrom.uuid;
            afschriftTo.rekening = afschriftFrom.rekening;
            afschriftTo.rekeningnaam = afschriftFrom.rekeningnaam;
            afschriftTo.relatienaam = afschriftFrom.relatienaam;
            afschriftTo.boekdatum = afschriftFrom.boekdatum;
            afschriftTo.bedrag = afschriftFrom.bedrag;
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

    }
}


angular.module('mswFrontendApp').service('afschriftDataService', Application.Services.AfschriftDataService);
