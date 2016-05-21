'use strict';

module Application.Services {
    import Administratie = Application.Model.Administratie;
    import FactuurData = Application.Model.FactuurData;
    import FactuurRegelData = Application.Model.FactuurRegelData;
    import GuiData = Application.Model.GuiData;

    export class FactuurGui{
        // velden voor factuur edit schermen
        factuurTabDisabled:boolean = true;
        factuurDetailTabDisabled:boolean = true;
        factuurRegelTabDisabled:boolean = true;
        searchContactTabDisabled:boolean = true;
        factuurEditContactTabDisabled:boolean = true;
        selectedIndex : number = 0;

        data : FactuurGuiData = new FactuurGuiData();

    }

    export class FactuurGuiData{
        selectedfactuur : FactuurData;
        factuurToEdit : FactuurData;
        addMode : boolean;
        addToAdresboek : boolean;
        selectedfactuurregel : FactuurRegelData;
        addRegelMode:boolean;
        administratie : Administratie;

    }


    export class FactuurGuiService {
        private factuurGui:FactuurGui;

        constructor($rootScope, private dataService: MyDataservice) {
            this.factuurGui=new FactuurGui();

            $rootScope.$on('data-updated', (event, guiData:GuiData)=> {
                this.reloadData(guiData.administratie);
            });

            // load for the first time
            this.reloadData(this.dataService.getData().administratie);

        }


        getFactuurGui():Application.Services.FactuurGui {
            return this.factuurGui;
        }


        showPage(page:String) {
            this.factuurGui.factuurTabDisabled=true;
            this.factuurGui.factuurRegelTabDisabled=true;
            this.factuurGui.searchContactTabDisabled=true;
            this.factuurGui.factuurDetailTabDisabled=true;
            this.factuurGui.factuurEditContactTabDisabled=true;

            if (page == SCREEN_FACTUUR_LIJST) {
                this.factuurGui.selectedIndex=0;
            }
            if (page == SCREEN_FACTUUR_EDIT) {
                this.factuurGui.selectedIndex=1;
                this.factuurGui.factuurTabDisabled=false;
            }
            if (page == SCREEN_FACTUUR_REGEL) {
                this.factuurGui.selectedIndex=2;
                this.factuurGui.factuurTabDisabled=false;
                this.factuurGui.factuurRegelTabDisabled=false;
            }
            if (page == SCREEN_FACTUUR_CONTACT) {
                this.factuurGui.selectedIndex=3;
                this.factuurGui.factuurTabDisabled=false;
                this.factuurGui.searchContactTabDisabled=false;
            }
            if (page == SCREEN_FACTUUR_EDIT_DETAIL) {
                this.factuurGui.selectedIndex=4;
                this.factuurGui.factuurTabDisabled=false;
                this.factuurGui.factuurDetailTabDisabled=false;
            }
            if (page == SCREEN_FACTUUR_EDIT_CONTACT) {
                this.factuurGui.selectedIndex=5;
                this.factuurGui.factuurTabDisabled=false;
                this.factuurGui.factuurEditContactTabDisabled=false;
            }

        }

        closePage(page:String) {
            if (page == SCREEN_FACTUUR_LIJST) {
            }
            if (page == SCREEN_FACTUUR_EDIT) {
                this.showPage(SCREEN_FACTUUR_LIJST);
            }
            if (page == SCREEN_FACTUUR_REGEL) {
                this.showPage(SCREEN_FACTUUR_EDIT);
            }
            if (page == SCREEN_FACTUUR_CONTACT) {
                this.showPage(SCREEN_FACTUUR_EDIT);
            }
            if (page == SCREEN_FACTUUR_EDIT_DETAIL) {
                this.showPage(SCREEN_FACTUUR_EDIT);
            }
            if (page == SCREEN_FACTUUR_EDIT_CONTACT) {
                this.showPage(SCREEN_FACTUUR_EDIT);
            }
        }

        private reloadData(administratie:Administratie):void {
            this.factuurGui.data.administratie = administratie;
        }
    }
}


angular.module('mswFrontendApp').service('factuurGuiService', Application.Services.FactuurGuiService);
