import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Historiques } from './historiques.model';
import { HistoriquesPopupService } from './historiques-popup.service';
import { HistoriquesService } from './historiques.service';

@Component({
    selector: 'jhi-historiques-dialog',
    templateUrl: './historiques-dialog.component.html'
})
export class HistoriquesDialogComponent implements OnInit {

    historiques: Historiques;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private historiquesService: HistoriquesService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.historiques.id !== undefined) {
            this.subscribeToSaveResponse(
                this.historiquesService.update(this.historiques), false);
        } else {
            this.subscribeToSaveResponse(
                this.historiquesService.create(this.historiques), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Historiques>, isCreated: boolean) {
        result.subscribe((res: Historiques) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Historiques, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'gStockSoftitApp.historiques.created'
            : 'gStockSoftitApp.historiques.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'historiquesListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-historiques-popup',
    template: ''
})
export class HistoriquesPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private historiquesPopupService: HistoriquesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.historiquesPopupService
                    .open(HistoriquesDialogComponent, params['id']);
            } else {
                this.modalRef = this.historiquesPopupService
                    .open(HistoriquesDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
