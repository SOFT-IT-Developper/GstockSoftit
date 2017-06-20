import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GStockSoftitSharedModule } from '../../shared';
import {
    HistoriquesService,
    HistoriquesPopupService,
    HistoriquesComponent,
    HistoriquesDetailComponent,
    HistoriquesDialogComponent,
    HistoriquesPopupComponent,
    HistoriquesDeletePopupComponent,
    HistoriquesDeleteDialogComponent,
    historiquesRoute,
    historiquesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...historiquesRoute,
    ...historiquesPopupRoute,
];

@NgModule({
    imports: [
        GStockSoftitSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        HistoriquesComponent,
        HistoriquesDetailComponent,
        HistoriquesDialogComponent,
        HistoriquesDeleteDialogComponent,
        HistoriquesPopupComponent,
        HistoriquesDeletePopupComponent,
    ],
    entryComponents: [
        HistoriquesComponent,
        HistoriquesDialogComponent,
        HistoriquesPopupComponent,
        HistoriquesDeleteDialogComponent,
        HistoriquesDeletePopupComponent,
    ],
    providers: [
        HistoriquesService,
        HistoriquesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GStockSoftitHistoriquesModule {}
