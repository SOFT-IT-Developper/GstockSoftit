import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ProduitsComponent } from './produits.component';
import { ProduitsDetailComponent } from './produits-detail.component';
import { ProduitsPopupComponent } from './produits-dialog.component';
import { ProduitsDeletePopupComponent } from './produits-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class ProduitsResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const produitsRoute: Routes = [
    {
        path: 'produits',
        component: ProduitsComponent,
        resolve: {
            'pagingParams': ProduitsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gStockSoftitApp.produits.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'produits/:id',
        component: ProduitsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gStockSoftitApp.produits.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const produitsPopupRoute: Routes = [
    {
        path: 'produits-new',
        component: ProduitsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gStockSoftitApp.produits.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'produits/:id/edit',
        component: ProduitsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gStockSoftitApp.produits.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'produits/:id/delete',
        component: ProduitsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gStockSoftitApp.produits.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
