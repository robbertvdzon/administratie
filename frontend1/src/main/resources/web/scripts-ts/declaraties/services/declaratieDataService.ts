'use strict';

module Application.Services {

    import DeclaratieData = Application.Model.DeclaratieData;
    import Administratie = Application.Model.Administratie;
    import DeclaratieGuiService = Application.Services.DeclaratieGuiService;

    export class DeclaratieDataService {
        constructor(private $rootScope,private $http, private dataService:Application.Services.MyDataservice, private declaratieGuiService:DeclaratieGuiService, private $filter) {
        }

        private setSelectedDeclaratie(declaratie:DeclaratieData) {
            this.declaratieGuiService.getDeclaratieGui().data.selecteddeclaratie = declaratie;
            this.resetDeclaratieToEdit();
        }

        public resetDeclaratieToEdit() {
            this.declaratieGuiService.getDeclaratieGui().data.declaratieToEdit = this.cloneDeclaratie(this.getSelectedDeclaratie());
        }



        private getSelectedDeclaratie():DeclaratieData{
            return this.declaratieGuiService.getDeclaratieGui().data.selecteddeclaratie;
        }

        public setDeclaratieAsSelected(uuid) {
            var declaratie:DeclaratieData = this.getDeclaratieByUuid(uuid);
            if (declaratie != null) {
                declaratie = this.cloneDeclaratie(declaratie);
            }
            this.declaratieGuiService.getDeclaratieGui().data.addMode = false;
            this.setSelectedDeclaratie(declaratie);
        }

        public createAndSelectNewDeclaratie() {
            var declaratie:DeclaratieData = new DeclaratieData();
            declaratie.declaratieNummer = this.findNextDeclaratienummer();
            declaratie.declaratieDate = this.$filter('date')(new Date(), 'dd-MM-yyyy');
            declaratie.omschrijving = "Declaratie omschrijving";
            declaratie.uuid = this.createUuid();
            this.declaratieGuiService.getDeclaratieGui().data.addMode = true;
            this.setSelectedDeclaratie(declaratie);
        }

        public getDeclaratieByUuid(uuid):DeclaratieData {
            var declaraties:DeclaratieData[] = this.dataService.getData().administratie.declaraties;
            for (var i = 0; i < declaraties.length; i++) {
                var declaratie:DeclaratieData = declaraties[i];
                if (declaratie.uuid === uuid) {
                    return declaratie;
                }
            }
            return null;
        }

        public cloneDeclaratie(declaratie:DeclaratieData):DeclaratieData {
            if (declaratie==null) return null;
            var declaratieClone = new DeclaratieData();
            declaratieClone.uuid = declaratie.uuid;
            declaratieClone.declaratieNummer = declaratie.declaratieNummer;
            declaratieClone.omschrijving = declaratie.omschrijving;
            declaratieClone.declaratieDate = declaratie.declaratieDate;
            declaratieClone.bedragExBtw = declaratie.bedragExBtw;
            declaratieClone.bedragIncBtw = declaratie.bedragIncBtw;
            declaratieClone.btw = declaratie.btw;
            return declaratieClone;
        }

        public copyInto(declaratieFrom:DeclaratieData, declaratieTo:DeclaratieData) {
            declaratieTo.uuid = declaratieFrom.uuid;
            declaratieTo.declaratieNummer = declaratieFrom.declaratieNummer;
            declaratieTo.omschrijving = declaratieFrom.omschrijving;
            declaratieTo.declaratieDate = declaratieFrom.declaratieDate;
            declaratieTo.bedragExBtw = declaratieFrom.bedragExBtw;
            declaratieTo.bedragIncBtw = declaratieFrom.bedragIncBtw;
            declaratieTo.btw = declaratieFrom.btw;
        }

        public saveDeclaratie(): ng.IPromise<any> {
            return this.$http({
                url: "/rest/declaratie/",
                method: "POST",
                data: this.getSelectedDeclaratie(),
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => {
                this.dataService.reload();
            });
        };


        public deleteDeclaratie(): ng.IPromise<any> {
            return this.$http({
                url: "/rest/declaratie/" + this.getSelectedDeclaratie().uuid,
                method: "DELETE"
            }).then((response) => {
                this.dataService.reload();
            });
        };

        public addNewDeclaratie(declaratie:DeclaratieData): ng.IPromise<any> {
            return this.$http({
                url: "/rest/declaratie/",
                method: "PUT",
                data: declaratie,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => {
                this.dataService.reload();
            });
        };

        public addDeclaratie(): ng.IPromise<any> {
            return this.addNewDeclaratie(this.getSelectedDeclaratie());
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

        public findNextDeclaratienummer():string {
            var administratie:Administratie = this.dataService.getData().administratie;
            var hoogste:number = 0;
            for (var i = 0; i < administratie.declaraties.length; i++) {
                var declaratieNr:number = parseInt(String(administratie.declaraties[i].declaratieNummer), 10);
                if (declaratieNr > hoogste) {
                    hoogste = declaratieNr;
                }
            }
            var nieuwNummer = hoogste + 1;
            return "" + nieuwNummer;
        }
    }
}


angular.module('mswFrontendApp').service('declaratieDataService', Application.Services.DeclaratieDataService);
