'use strict';

module Application.Controllers {
    declare var pdfMake:any;

    import FactuurData = Application.Model.FactuurData;
    import FactuurRegelData = Application.Model.FactuurRegelData;
    import FactuurDataService = Application.Services.FactuurDataService;
    import FactuurGuiService = Application.Services.FactuurGuiService;
    import SCREEN_FACTUUR_EDIT_DETAIL = Application.SCREEN_FACTUUR_EDIT_DETAIL;
    import SCREEN_FACTUUR_EDIT_CONTACT = Application.SCREEN_FACTUUR_EDIT_CONTACT;
    import SCREEN_FACTUUR_CONTACT = Application.SCREEN_FACTUUR_CONTACT;
    import FactuurGuiData = Application.Services.FactuurGuiData;

    interface MyScope extends ng.IScope {
        data:FactuurGuiData;
    }

    export class FactuurEditController {

        constructor(private $scope:MyScope, private factuurDataService:FactuurDataService, private factuurGuiService:FactuurGuiService, private $window) {
            this.$scope.data = factuurGuiService.getFactuurGui().data;
        }

        save() {
            this.factuurDataService.saveFactuur().then((response) => {
                this.factuurGuiService.closePage(SCREEN_FACTUUR_EDIT);
            }).catch((response) => {
                alert("Opslaan mislukt");
            })
        }

        add() {
            this.factuurDataService.addFactuur().then((response) => {
                this.factuurGuiService.closePage(SCREEN_FACTUUR_EDIT);
            }).catch((response) => {
                alert("Toevoegen mislukt");
            })
        }

        delete() {
            this.factuurDataService.deleteFactuur().then((response) => {
                this.factuurGuiService.closePage(SCREEN_FACTUUR_EDIT);
            }).catch((response) => {
                alert("Delete mislukt");
            })
        }

        cancel() {
            this.factuurGuiService.closePage(SCREEN_FACTUUR_EDIT);
        }

        addRegel(ev) {
            this.factuurDataService.loadNewFactuurRegel();
        }

        editRegel(uuid) {
            var selectedfactuurregel:FactuurRegelData = this.factuurDataService.cloneFactuurRegel(this.factuurDataService.getRegelByUuid(this.$scope.data.selectedfactuur, uuid));
            this.factuurDataService.loadExistingFactuurRegel(selectedfactuurregel);
        }

        searchContact() {
            this.factuurGuiService.showPage(SCREEN_FACTUUR_CONTACT);
        }

        editDetails() {
            this.factuurDataService.resetFactuurToEdit();
            this.factuurGuiService.showPage(SCREEN_FACTUUR_EDIT_DETAIL);
        }

        editContactDetails() {
            this.factuurDataService.resetFactuurToEdit();
            this.$scope.data.addToAdresboek = false;
            this.factuurGuiService.showPage(SCREEN_FACTUUR_EDIT_CONTACT);
        }

        print() {
            //var doc = { content: 'This is an sample PDF printed with pdfMake' };
            var factuur:FactuurData = this.$scope.data.selectedfactuur;

            var line = {
                table: {
                    widths: ['*'],
                    body: [['']
                    ]
                },
                layout: {
                    hLineWidth: function (i, node) {
                        return (i === 0 ) ? 1 : 0;
                    },
                    vLineWidth: function (i, node) {
                        return 0;
                    },
                }
            }

            var regels =
            {
                style: 'tableExample',
                table: {
                    body: [
                        [
                            { text: 'Aantal', bold: true } ,
                            { text: 'Omschrijving', bold: true } ,
                            { text: 'Prijs', bold: true } ,
                            { text: 'Btw', bold: true } ,
                            { text: 'Totaal ex', bold: true }
                        ]
                    ]
                },
                layout: {
                    hLineWidth: function (i, node) {
                        return 0;
                    },
                    vLineWidth: function (i, node) {
                        return 0;
                    },
                }
            }

            var tabel =
            {
                style: 'tableExample',
                table: {
                    body: [
                        [
                            { text: '', bold: true } ,
                            { text: '', bold: true } ,
                            { text: '', bold: true }
                        ]
                    ]
                },
                layout: {
                    hLineWidth: function (i, node) {
                        return 0;
                    },
                    vLineWidth: function (i, node) {
                        return 0;
                    },
                }
            }

            var tabel2 =
            {
                style: 'tableExample',
                table: {
                    body: [
                        [
                            { text: '', bold: true } ,
                            { text: '', bold: true } ,
                            { text: '', bold: true }
                        ]
                    ]
                },
                layout: {
                    hLineWidth: function (i, node) {
                        return 0;
                    },
                    vLineWidth: function (i, node) {
                        return 0;
                    },
                }
            }

            var doc:any = {
                content: [],
                styles: {
                    header: {
                        fontSize: 18,
                        bold: true,
                        margin: [0, 0, 0, 10]
                    },
                    subheader: {
                        fontSize: 16,
                        bold: true,
                        margin: [0, 10, 0, 5]
                    },
                    bold: {
                        bold: true,
                    }
                },

                defaultStyle: {}

            }


            doc.content.push({ text: 'Klant factuuradres', bold: true });
            doc.content.push(factuur.klant.naam);
            doc.content.push(factuur.klant.adres);
            doc.content.push(factuur.klant.postcode + " " + factuur.klant.woonplaats);

            doc.content.push({ text: '', style: 'header' });
            doc.content.push({ text: '', style: 'header' });
            doc.content.push({ text: 'Factuur', style: 'header' });
            doc.content.push({ text: '', style: 'header' });

            doc.content.push(line);

            doc.content.push({
                text: "Factuurnummer:" + factuur.factuurNummer + "       Factuurdatum:" + factuur.factuurDate,
                style: 'bold'
            });

            doc.content.push(line);

            var totalEx = 0;
            var totalBtw = 0;
            var totalInc = 0;

            factuur.factuurRegels.forEach(function (regel:FactuurRegelData) {
                var regelTotalEx = regel.aantal * regel.stuksPrijs;
                var regelBtw = regel.aantal * regel.stuksPrijs;
                totalEx += regelTotalEx;
                totalBtw += regelTotalEx*(regelBtw/100);
                totalInc += regelTotalEx*((100+regelBtw)/100);

                regels.table.body.push(
                    [
                        String(regel.aantal),
                        String(regel.omschrijving),
                        String(regel.stuksPrijs),
                        String(regel.btwPercentage)+"%",
                        String(regel.aantal * regel.stuksPrijs),
                    ]
                );
            })


            doc.content.push(regels);

            doc.content.push({ text: '', style: 'header' });
            doc.content.push(line);

            tabel.table.body.push(["Bedrag exclusief BTW",":",String(totalEx)]);
            tabel.table.body.push(["BTW",":",String(totalBtw)]);
            tabel.table.body.push(["Bedrag inclusief BTW",":",String(totalInc)]);
            doc.content.push(tabel);

            doc.content.push({ text: '', style: 'header' });
            doc.content.push(line);
            doc.content.push('Bij betaling gaarne factuurnummer vermelden.');
            doc.content.push({ text: '', style: 'header' });
            tabel2.table.body.push(["ABN-Amro rekeningnummer",":","NL88 ABNA 0532.7503.30"]);
            tabel2.table.body.push(["BTW-nr",":","NL191082661B01"]);
            tabel2.table.body.push(["Handelsregister",":","64609227"]);
            tabel2.table.body.push(["Adres",":","Kerklaan 13a"]);
            tabel2.table.body.push(["","","1961GA Heemskerk"]);
            doc.content.push(tabel2);

            pdfMake.createPdf(doc).open();

            // print the PDF (temporarily Chrome-only)
            //pdfMake.createPdf(docDefinition).print();

            // download the PDF (temporarily Chrome-only)
            //pdfMake.createPdf(docDefinition).download('optionalName.pdf');
            //this.$window.open('https://www.google.com', '_blank');
        }
    }
}


angular.module('mswFrontendApp').controller('FactuurEditController', Application.Controllers.FactuurEditController);


