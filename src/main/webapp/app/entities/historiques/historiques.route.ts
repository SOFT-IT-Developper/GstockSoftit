import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { HistoriquesComponent } from './historiques.component';
import { HistoriquesDetailComponent } from './historiques-detail.component';
import { HistoriquesPopupComponent } from './historiques-dialog.component';
import { HistoriquesDeletePopupComponent } from './historiques-delete-dialog.component';

import { Principal } from '../../shared';

export const historiquesRoute: Routes = [
    {
        path: 'historiques',
        component: HistoriquesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gStockSoftitApp.historiques.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'historiques/:id',
        component: HistoriquesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gStockSoftitApp.historiques.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const historiquesPopupRoute: Routes = [
    {
        path: 'historiques-new',
        component: HistoriquesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gStockSoftitApp.historiques.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'historiques/:id/edit',
        component: HistoriquesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gStockSoftitApp.historiques.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'historiques/:id/delete',
        component: HistoriquesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gStockSoftitApp.historiques.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
