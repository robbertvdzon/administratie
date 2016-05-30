'use strict';

module Application.Services {
    import Administratie = Application.Model.Administratie;
    import AfschriftData = Application.Model.AfschriftData;
    import GuiData = Application.Model.GuiData;

    export class AfschriftGui{
        // velden voor afschrift edit schermen
        afschriftTabDisabled:boolean = true;
        selectedIndex : number = 0;

        data : AfschriftGuiData = new AfschriftGuiData();

    }

    export class AfschriftGuiData{
        selectedafschrift : AfschriftData;
        afschriftToEdit : AfschriftData;
        addMode : boolean;
        afschriften:AfschriftData[];
    }


    export class AfschriftGuiService {
        private afschriftGui:AfschriftGui;

        constructor($rootScope, private dataService: MyDataservice) {
            this.afschriftGui=new AfschriftGui();

            $rootScope.$on('data-updated', (event, guiData:GuiData)=> {
                this.reloadData(guiData.administratie.afschriften);
            });

            // load for the first time
            this.reloadData(this.dataService.getData().administratie.afschriften);
        }

        getAfschriftGui():AfschriftGui {
            return this.afschriftGui;
        }


        showPage(page:String) {
            this.afschriftGui.afschriftTabDisabled=true;

            if (page == SCREEN_AFSCHRIFT_LIJST) {
                this.afschriftGui.selectedIndex=0;
            }
            if (page == SCREEN_AFSCHRIFT_EDIT) {
                this.afschriftGui.selectedIndex=1;
                this.afschriftGui.afschriftTabDisabled=false;
            }
        }

        closePage(page:String) {
            if (page == SCREEN_AFSCHRIFT_LIJST) {
            }
            if (page == SCREEN_AFSCHRIFT_EDIT) {
                this.showPage(SCREEN_AFSCHRIFT_LIJST);
            }
        }

        private reloadData(afschriften:AfschriftData[]):void {
            this.afschriftGui.data.afschriften = afschriften;
        }
    }
}


angular.module('mswFrontendApp').service('afschriftGuiService', Application.Services.AfschriftGuiService);
