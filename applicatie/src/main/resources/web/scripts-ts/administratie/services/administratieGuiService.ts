'use strict';

module Application.Services {
    import Administratie = Application.Model.Administratie;
    import GuiData = Application.Model.GuiData;
    import AdministratieGegevens = Application.Model.AdministratieGegevens;

    export class AdministratieGui{
        data : AdministratieGuiData = new AdministratieGuiData();
    }

    export class AdministratieGuiData{
        administratieGegevens : AdministratieGegevens;
        administratieGegevensToEdit : AdministratieGegevens;
    }


    export class AdministratieGuiService {
        private administratieGui:AdministratieGui;

        constructor($rootScope, private dataService: MyDataservice) {
            this.administratieGui=new AdministratieGui();

            $rootScope.$on('data-updated', (event, guiData:GuiData)=> {
                this.reloadData(this.dataService.getData().administratie.administratieGegevens);
            });

            // load for the first time
            this.reloadData(this.dataService.getData().administratie.administratieGegevens);
        }

        public getAdministratieGui():AdministratieGui {
            return this.administratieGui;
        }

        private reloadData(administratieGegevens:AdministratieGegevens):void {
            this.administratieGui.data.administratieGegevens = administratieGegevens;
        }
    }
}

angular.module('mswFrontendApp').service('administratieGuiService', Application.Services.AdministratieGuiService);
