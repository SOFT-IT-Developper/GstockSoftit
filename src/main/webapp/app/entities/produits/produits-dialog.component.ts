import { Component, OnInit, OnDestroy, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Produits } from './produits.model';
import { ProduitsPopupService } from './produits-popup.service';
import { ProduitsService } from './produits.service';
import { Stock, StockService } from '../stock';
import { Categorie, CategorieService } from '../categorie';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-produits-dialog',
    templateUrl: './produits-dialog.component.html'
})
export class ProduitsDialogComponent implements OnInit {

    produits: Produits;
    authorities: any[];
    isSaving: boolean;

    stocks: Stock[];

    categories: Categorie[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private alertService: JhiAlertService,
        private produitsService: ProduitsService,
        private stockService: StockService,
        private categorieService: CategorieService,
        private elementRef: ElementRef,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.stockService.query()
            .subscribe((res: ResponseWrapper) => { this.stocks = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.categorieService.query()
            .subscribe((res: ResponseWrapper) => { this.categories = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, produits, field, isImage) {
        if (event && event.target.files && event.target.files[0]) {
            const file = event.target.files[0];
            if (isImage && !/^image\//.test(file.type)) {
                return;
            }
            this.dataUtils.toBase64(file, (base64Data) => {
                produits[field] = base64Data;
                produits[`${field}ContentType`] = file.type;
            });
        }
    }

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.produits, this.elementRef, field, fieldContentType, idInput);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.produits.id !== undefined) {
            this.subscribeToSaveResponse(
                this.produitsService.update(this.produits), false);
        } else {
            this.subscribeToSaveResponse(
                this.produitsService.create(this.produits), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Produits>, isCreated: boolean) {
        result.subscribe((res: Produits) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Produits, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'gStockSoftitApp.produits.created'
            : 'gStockSoftitApp.produits.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'produitsListModification', content: 'OK'});
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

    trackStockById(index: number, item: Stock) {
        return item.id;
    }

    trackCategorieById(index: number, item: Categorie) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-produits-popup',
    template: ''
})
export class ProduitsPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private produitsPopupService: ProduitsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.produitsPopupService
                    .open(ProduitsDialogComponent, params['id']);
            } else {
                this.modalRef = this.produitsPopupService
                    .open(ProduitsDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
