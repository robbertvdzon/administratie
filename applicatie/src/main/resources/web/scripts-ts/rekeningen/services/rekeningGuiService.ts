'use strict';

module Application.Services {
    import Administratie = Application.Model.Administratie;
    import RekeningData = Application.Model.RekeningData;
    import GuiData = Application.Model.GuiData;

    export class RekeningGui{
        // velden voor rekening edit schermen
        rekeningTabDisabled:boolean = true;
        selectedIndex : number = 0;

        data : RekeningGuiData = new RekeningGuiData();

    }

    export class RekeningGuiData{
        selectedrekening : RekeningData;
        rekeningToEdit : RekeningData;
        addMode : boolean;
        rekeningen:RekeningData[];
    }


    export class RekeningGuiService {
        private rekeningGui:RekeningGui;

        constructor($rootScope, private dataService: MyDataservice) {
            this.rekeningGui=new RekeningGui();

            $rootScope.$on('data-updated', (event, guiData:GuiData)=> {
                this.reloadData(guiData.administratie.rekeningen);
            });

            // load for the first time
            this.reloadData(this.dataService.getData().administratie.rekeningen);
        }

        getRekeningGui():RekeningGui {
            return this.rekeningGui;
        }


        showPage(page:String) {
            this.rekeningGui.rekeningTabDisabled=true;

            if (page == SCREEN_REKENING_LIJST) {
                this.rekeningGui.selectedIndex=0;
            }
            if (page == SCREEN_REKENING_EDIT) {
                this.rekeningGui.selectedIndex=1;
                this.rekeningGui.rekeningTabDisabled=false;
            }
        }

        closePage(page:String) {
            if (page == SCREEN_REKENING_LIJST) {
            }
            if (page == SCREEN_REKENING_EDIT) {
                this.showPage(SCREEN_REKENING_LIJST);
            }
        }

        private reloadData(rekeningen:RekeningData[]):void {
            this.rekeningGui.data.rekeningen = rekeningen;
        }
    }
}


angular.module('mswFrontendApp').service('rekeningGuiService', Application.Services.RekeningGuiService);
