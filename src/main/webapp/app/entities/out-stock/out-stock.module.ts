import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GStockSoftitSharedModule } from '../../shared';
import {
    OutStockService,
    OutStockPopupService,
    OutStockComponent,
    OutStockDetailComponent,
    OutStockDialogComponent,
    OutStockPopupComponent,
    OutStockDeletePopupComponent,
    OutStockDeleteDialogComponent,
    outStockRoute,
    outStockPopupRoute,
} from './';

const ENTITY_STATES = [
    ...outStockRoute,
    ...outStockPopupRoute,
];

@NgModule({
    imports: [
        GStockSoftitSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        OutStockComponent,
        OutStockDetailComponent,
        OutStockDialogComponent,
        OutStockDeleteDialogComponent,
        OutStockPopupComponent,
        OutStockDeletePopupComponent,
    ],
    entryComponents: [
        OutStockComponent,
        OutStockDialogComponent,
        OutStockPopupComponent,
        OutStockDeleteDialogComponent,
        OutStockDeletePopupComponent,
    ],
    providers: [
        OutStockService,
        OutStockPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GStockSoftitOutStockModule {}
