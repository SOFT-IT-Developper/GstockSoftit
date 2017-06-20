import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { GStockSoftitCategorieModule } from './categorie/categorie.module';
import { GStockSoftitProduitsModule } from './produits/produits.module';
import { GStockSoftitStockModule } from './stock/stock.module';
import { GStockSoftitHistoriquesModule } from './historiques/historiques.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        GStockSoftitCategorieModule,
        GStockSoftitProduitsModule,
        GStockSoftitStockModule,
        GStockSoftitHistoriquesModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GStockSoftitEntityModule {}
