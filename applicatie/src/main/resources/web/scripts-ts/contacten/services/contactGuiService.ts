'use strict';

module Application.Services {
    import Administratie = Application.Model.Administratie;
    import ContactData = Application.Model.ContactData;

    export class ContactGui{
        // velden voor contact edit schermen
        contactTabDisabled:boolean = true;
        selectedIndex : number = 0;

        data : ContactGuiData = new ContactGuiData();

    }

    export class ContactGuiData{
        selectedcontact : ContactData;
        addMode : boolean;
        administratie : Administratie;
    }


    export class ContactGuiService {
        private contactGui:ContactGui;

        constructor($rootScope, private dataService: MyDataservice) {
            this.contactGui=new ContactGui();

            $rootScope.$on('data-updated', (event, administratie)=> {
                this.reloadData(administratie);
            });

            // load for the first time
            this.reloadData(this.dataService.getData());
        }

        getContactGui():ContactGui {
            return this.contactGui;
        }


        showPage(page:String) {
            this.contactGui.contactTabDisabled=true;

            if (page == SCREEN_CONTACT_LIJST) {
                this.contactGui.selectedIndex=0;
            }
            if (page == SCREEN_CONTACT_EDIT) {
                this.contactGui.selectedIndex=1;
                this.contactGui.contactTabDisabled=false;
            }
        }

        closePage(page:String) {
            if (page == SCREEN_CONTACT_LIJST) {
            }
            if (page == SCREEN_CONTACT_EDIT) {
                this.showPage(SCREEN_CONTACT_LIJST);
            }
        }

        private reloadData(administratie:Administratie):void {
            this.contactGui.data.administratie = administratie;
        }
    }
}


angular.module('mswFrontendApp').service('contactGuiService', Application.Services.ContactGuiService);
