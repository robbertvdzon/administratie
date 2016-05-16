'use strict';
module Application {
    export var SCREEN_FACTUUR_LIJST = "SCREEN_FACTUUR_LIJST";
    export var SCREEN_FACTUUR_EDIT = "SCREEN_FACTUUR_EDIT";
    export var SCREEN_FACTUUR_REGEL = "SCREEN_FACTUUR_REGEL";
    export var SCREEN_FACTUUR_CONTACT = "SCREEN_FACTUUR_CONTACT";
    export var SCREEN_FACTUUR_EDIT_DETAIL = "SCREEN_FACTUUR_EDIT_DETAIL";
    export var SCREEN_FACTUUR_EDIT_CONTACT = "SCREEN_FACTUUR_EDIT_CONTACT";
}

module Application.Services {
    import FactuurData = Application.Model.FactuurData;
    import FactuurRegelData = Application.Model.FactuurRegelData;

    export class FactuurGui{
        factuurTabDisabled:boolean = true;
        factuurDetailTabDisabled:boolean = true;
        factuurRegelTabDisabled:boolean = true;
        searchContactTabDisabled:boolean = true;
        factuurEditContactTabDisabled:boolean = true;
        selectedIndex : number = 0;

        // velden voor editController
        data : FactuurGuiData = new FactuurGuiData();

    }

    export class FactuurGuiData{
        selectedfactuur : FactuurData;
        factuurToEdit : FactuurData;
        addMode : boolean;
        addToAdresboek : boolean;
        // factuur regel data
        selectedfactuurregel : FactuurRegelData;
        addRegelMode:boolean;

    }


    export class FactuurGuiService {
        private factuurGui:FactuurGui;

        constructor($rootScope) {
            this.factuurGui=new FactuurGui();
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

    }
}


angular.module('mswFrontendApp').service('factuurGuiService', Application.Services.FactuurGuiService);
