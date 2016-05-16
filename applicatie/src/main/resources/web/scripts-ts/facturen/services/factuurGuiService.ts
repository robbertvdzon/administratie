'use strict';

module Application.Services {

    export class FactuurGui{
        factuurTabDisabled:boolean = true;
        factuurDetailTabDisabled:boolean = true;
        factuurRegelTabDisabled:boolean = true;
        searchContactTabDisabled:boolean = true;
        factuurEditContactTabDisabled:boolean = true;
        selectedIndex : number = 0;
    }


    export class FactuurGuiService {
        private factuurGui:FactuurGui;

        constructor() {
            this.factuurGui=new FactuurGui();
        }


        getFactuurGui():Application.Services.FactuurGui {
            return this.factuurGui;
        }
    }
}


angular.module('mswFrontendApp').service('factuurGuiService', Application.Services.FactuurGuiService);
