import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CategorieComponent } from './categorie.component';
import { CategorieDetailComponent } from './categorie-detail.component';
import { CategoriePopupComponent } from './categorie-dialog.component';
import { CategorieDeletePopupComponent } from './categorie-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class CategorieResolvePagingParams implements Resolve<any> {

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

export const categorieRoute: Routes = [
    {
        path: 'categorie',
        component: CategorieComponent,
        resolve: {
            'pagingParams': CategorieResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gStockSoftitApp.categorie.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'categorie/:id',
        component: CategorieDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gStockSoftitApp.categorie.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const categoriePopupRoute: Routes = [
    {
        path: 'categorie-new',
        component: CategoriePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gStockSoftitApp.categorie.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'categorie/:id/edit',
        component: CategoriePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gStockSoftitApp.categorie.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'categorie/:id/delete',
        component: CategorieDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gStockSoftitApp.categorie.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
