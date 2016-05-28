'use strict';

module Application.Services {
    import Administratie = Application.Model.Administratie;
    import GebruikerData = Application.Model.GebruikerData;
    import GuiData = Application.Model.GuiData;

    export class AccountGui{
        // velden voor gebruiker edit schermen
        gebruikerSetPasswdTabDisabled:boolean = true;
        selectedIndex : number = 0;
        data : AccountGuiData = new AccountGuiData();
    }

    export class AccountGuiData{
        account : GebruikerData;
        accountToEdit : GebruikerData;
    }


    export class AccountGuiService {
        private accountGui:AccountGui;

        constructor($rootScope, private dataService: MyDataservice) {
            this.accountGui=new AccountGui();
            $rootScope.$on('data-updated', (event, guiData:GuiData)=> {
                this.reloadData(guiData.huidigeGebruiker);
            });

            // load for the first time
            this.reloadData(this.dataService.getData().huidigeGebruiker);
        }

        getGebruikerGui():AccountGui {
            return this.accountGui;
        }


        showPage(page:String) {
            this.accountGui.gebruikerSetPasswdTabDisabled=true;

            if (page == SCREEN_ACCOUNT_EDIT) {
                this.accountGui.selectedIndex=0;
            }
            if (page == SCREEN_ACCOUNT_SET_PASSWD) {
                this.accountGui.selectedIndex=1;
                this.accountGui.gebruikerSetPasswdTabDisabled=false;
            }
        }

        closePage(page:String) {
            if (page == SCREEN_ACCOUNT_EDIT) {
            }
            if (page == SCREEN_ACCOUNT_SET_PASSWD) {
                this.showPage(SCREEN_ACCOUNT_EDIT);
            }
        }

        private reloadData(huidigeGebruiker:GebruikerData):void {
            this.accountGui.data.account = huidigeGebruiker;
        }
    }
}


angular.module('mswFrontendApp').service('accountGuiService', Application.Services.AccountGuiService);
