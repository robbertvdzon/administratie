'use strict';

module Application.Services {
    import Administratie = Application.Model.Administratie;
    import DeclaratieData = Application.Model.DeclaratieData;
    import GuiData = Application.Model.GuiData;

    export class DeclaratieGui{
        // velden voor declaratie edit schermen
        declaratieTabDisabled:boolean = true;
        selectedIndex : number = 0;

        data : DeclaratieGuiData = new DeclaratieGuiData();

    }

    export class DeclaratieGuiData{
        selecteddeclaratie : DeclaratieData;
        declaratieToEdit : DeclaratieData;
        addMode : boolean;
        declaraties:DeclaratieData[];
    }


    export class DeclaratieGuiService {
        private declaratieGui:DeclaratieGui;

        constructor($rootScope, private dataService: MyDataservice) {
            this.declaratieGui=new DeclaratieGui();

            $rootScope.$on('data-updated', (event, guiData:GuiData)=> {
                this.reloadData(guiData.administratie.declaraties);
            });

            // load for the first time
            this.reloadData(this.dataService.getData().administratie.declaraties);
        }

        getDeclaratieGui():DeclaratieGui {
            return this.declaratieGui;
        }


        showPage(page:String) {
            this.declaratieGui.declaratieTabDisabled=true;

            if (page == SCREEN_DECLARATIE_LIJST) {
                this.declaratieGui.selectedIndex=0;
            }
            if (page == SCREEN_DECLARATIE_EDIT) {
                this.declaratieGui.selectedIndex=1;
                this.declaratieGui.declaratieTabDisabled=false;
            }
        }

        closePage(page:String) {
            if (page == SCREEN_DECLARATIE_LIJST) {
            }
            if (page == SCREEN_DECLARATIE_EDIT) {
                this.showPage(SCREEN_DECLARATIE_LIJST);
            }
        }

        private reloadData(declaraties:DeclaratieData[]):void {
            this.declaratieGui.data.declaraties = declaraties;
        }
    }
}


angular.module('mswFrontendApp').service('declaratieGuiService', Application.Services.DeclaratieGuiService);
