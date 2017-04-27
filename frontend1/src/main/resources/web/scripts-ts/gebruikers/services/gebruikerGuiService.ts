'use strict';

module Application.Services {
    import Administratie = Application.Model.Administratie;
    import GebruikerData = Application.Model.GebruikerData;
    import GuiData = Application.Model.GuiData;

    export class GebruikerGui{
        // velden voor gebruiker edit schermen
        gebruikerTabDisabled:boolean = true;
        gebruikerSetPasswdTabDisabled:boolean = true;
        selectedIndex : number = 0;

        data : GebruikerGuiData = new GebruikerGuiData();

    }

    export class GebruikerGuiData{
        selectedgebruiker : GebruikerData;
        addMode : boolean;
        gebruikers:GebruikerData[];
    }


    export class GebruikerGuiService {
        private gebruikerGui:GebruikerGui;

        constructor($rootScope, private dataService: MyDataservice) {
            this.gebruikerGui=new GebruikerGui();

            $rootScope.$on('data-updated', (event, guiData:GuiData)=> {
                this.reloadData(guiData.gebruikers);
            });

            $rootScope.$on('gebruikers-main-button-pressed',()=>{
                this.showPage(SCREEN_GEBRUIKER_LIJST);
            });

            // load for the first time
            this.reloadData(this.dataService.getData().gebruikers);
        }

        getGebruikerGui():GebruikerGui {
            return this.gebruikerGui;
        }


        showPage(page:String) {
            this.gebruikerGui.gebruikerTabDisabled=true;
            this.gebruikerGui.gebruikerSetPasswdTabDisabled=true;

            if (page == SCREEN_GEBRUIKER_LIJST) {
                this.gebruikerGui.selectedIndex=0;
            }
            if (page == SCREEN_GEBRUIKER_EDIT) {
                this.gebruikerGui.selectedIndex=1;
                this.gebruikerGui.gebruikerTabDisabled=false;
            }
            if (page == SCREEN_GEBRUIKER_SET_PASSWD) {
                this.gebruikerGui.selectedIndex=2;
                this.gebruikerGui.gebruikerSetPasswdTabDisabled=false;
            }
        }

        closePage(page:String) {
            if (page == SCREEN_GEBRUIKER_LIJST) {
            }
            if (page == SCREEN_GEBRUIKER_EDIT) {
                this.showPage(SCREEN_GEBRUIKER_LIJST);
            }
            if (page == SCREEN_GEBRUIKER_SET_PASSWD) {
                this.showPage(SCREEN_GEBRUIKER_LIJST);
            }
        }

        private reloadData(gebruikers:GebruikerData[]):void {
            this.gebruikerGui.data.gebruikers = gebruikers;
        }
    }
}


angular.module('mswFrontendApp').service('gebruikerGuiService', Application.Services.GebruikerGuiService);
