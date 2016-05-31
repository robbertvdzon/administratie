'use strict';

module Application.Services {
    import Administratie = Application.Model.Administratie;
    import BestellingData = Application.Model.BestellingData;
    import BestellingRegelData = Application.Model.BestellingRegelData;
    import GuiData = Application.Model.GuiData;

    export class BestellingGui{
        // velden voor bestelling edit schermen
        bestellingTabDisabled:boolean = true;
        bestellingDetailTabDisabled:boolean = true;
        bestellingRegelTabDisabled:boolean = true;
        searchContactTabDisabled:boolean = true;
        bestellingEditContactTabDisabled:boolean = true;
        selectedIndex : number = 0;

        data : BestellingGuiData = new BestellingGuiData();

    }

    export class BestellingGuiData{
        selectedbestelling : BestellingData;
        bestellingToEdit : BestellingData;
        selectedbestellingregel : BestellingRegelData;
        bestellingregelToEdit : BestellingRegelData;
        addMode : boolean;
        addToAdresboek : boolean;
        addRegelMode:boolean;
        administratie : Administratie;

    }


    export class BestellingGuiService {
        private bestellingGui:BestellingGui;

        constructor($rootScope, private dataService: MyDataservice) {
            this.bestellingGui=new BestellingGui();

            $rootScope.$on('data-updated', (event, guiData:GuiData)=> {
                this.reloadData(guiData.administratie);
            });

            $rootScope.$on('bestellingen-main-button-pressed',()=>{
                this.showPage(SCREEN_BESTELLING_LIJST);
            });

            // load for the first time
            this.reloadData(this.dataService.getData().administratie);

        }


        getBestellingGui():Application.Services.BestellingGui {
            return this.bestellingGui;
        }


        showPage(page:String) {
            this.bestellingGui.bestellingTabDisabled=true;
            this.bestellingGui.bestellingRegelTabDisabled=true;
            this.bestellingGui.searchContactTabDisabled=true;
            this.bestellingGui.bestellingDetailTabDisabled=true;
            this.bestellingGui.bestellingEditContactTabDisabled=true;

            if (page == SCREEN_BESTELLING_LIJST) {
                this.bestellingGui.selectedIndex=0;
            }
            if (page == SCREEN_BESTELLING_EDIT) {
                this.bestellingGui.selectedIndex=1;
                this.bestellingGui.bestellingTabDisabled=false;
            }
            if (page == SCREEN_BESTELLING_REGEL) {
                this.bestellingGui.selectedIndex=2;
                this.bestellingGui.bestellingTabDisabled=false;
                this.bestellingGui.bestellingRegelTabDisabled=false;
            }
            if (page == SCREEN_BESTELLING_CONTACT) {
                this.bestellingGui.selectedIndex=3;
                this.bestellingGui.bestellingTabDisabled=false;
                this.bestellingGui.searchContactTabDisabled=false;
            }
            if (page == SCREEN_BESTELLING_EDIT_DETAIL) {
                this.bestellingGui.selectedIndex=4;
                this.bestellingGui.bestellingTabDisabled=false;
                this.bestellingGui.bestellingDetailTabDisabled=false;
            }
            if (page == SCREEN_BESTELLING_EDIT_CONTACT) {
                this.bestellingGui.selectedIndex=5;
                this.bestellingGui.bestellingTabDisabled=false;
                this.bestellingGui.bestellingEditContactTabDisabled=false;
            }

        }

        closePage(page:String) {
            if (page == SCREEN_BESTELLING_LIJST) {
            }
            if (page == SCREEN_BESTELLING_EDIT) {
                this.showPage(SCREEN_BESTELLING_LIJST);
            }
            if (page == SCREEN_BESTELLING_REGEL) {
                this.showPage(SCREEN_BESTELLING_EDIT);
            }
            if (page == SCREEN_BESTELLING_CONTACT) {
                this.showPage(SCREEN_BESTELLING_EDIT);
            }
            if (page == SCREEN_BESTELLING_EDIT_DETAIL) {
                this.showPage(SCREEN_BESTELLING_EDIT);
            }
            if (page == SCREEN_BESTELLING_EDIT_CONTACT) {
                this.showPage(SCREEN_BESTELLING_EDIT);
            }
        }

        private reloadData(administratie:Administratie):void {
            this.bestellingGui.data.administratie = administratie;
        }
    }
}


angular.module('mswFrontendApp').service('bestellingGuiService', Application.Services.BestellingGuiService);
