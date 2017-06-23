import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Stock } from './stock.model';
import { StockPopupService } from './stock-popup.service';
import { StockService } from './stock.service';
import { Produits, ProduitsService } from '../produits';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-stock-dialog',
    templateUrl: './stock-dialog.component.html'
})
export class StockDialogComponent implements OnInit {

    stock: Stock;
    authorities: any[];
    isSaving: boolean;

    produits: Produits[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private stockService: StockService,
        private produitsService: ProduitsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.produitsService
            .query({filter: 'stock(name)-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.stock.produit || !this.stock.produit.id) {
                    this.produits = res.json;
                } else {
                    this.produitsService
                        .find(this.stock.produit.id)
                        .subscribe((subRes: Produits) => {
                            this.produits = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.stock.id !== undefined) {
            this.subscribeToSaveResponse(
                this.stockService.update(this.stock), false);
        } else {
            this.subscribeToSaveResponse(
                this.stockService.create(this.stock), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Stock>, isCreated: boolean) {
        result.subscribe((res: Stock) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Stock, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'gStockSoftitApp.stock.created'
            : 'gStockSoftitApp.stock.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'stockListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackProduitsById(index: number, item: Produits) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-stock-popup',
    template: ''
})
export class StockPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private stockPopupService: StockPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.stockPopupService
                    .open(StockDialogComponent, params['id']);
            } else {
                this.modalRef = this.stockPopupService
                    .open(StockDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
