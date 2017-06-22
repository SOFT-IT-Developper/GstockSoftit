import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { GStockSoftitCategorieModule } from './categorie/categorie.module';
import { GStockSoftitProduitsModule } from './produits/produits.module';
import { GStockSoftitStockModule } from './stock/stock.module';
import { GStockSoftitHistoriquesModule } from './historiques/historiques.module';
import { OutstockComponent } from './outstock/outstock.component';
import { GStockSoftitOutStockModule } from './out-stock/out-stock.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        GStockSoftitCategorieModule,
        GStockSoftitProduitsModule,
        GStockSoftitStockModule,
        GStockSoftitHistoriquesModule,
        GStockSoftitOutStockModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [OutstockComponent],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GStockSoftitEntityModule {}
