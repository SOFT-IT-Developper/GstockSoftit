import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { Historiques } from './historiques.model';
import { HistoriquesPopupService } from './historiques-popup.service';
import { HistoriquesService } from './historiques.service';

@Component({
    selector: 'jhi-historiques-delete-dialog',
    templateUrl: './historiques-delete-dialog.component.html'
})
export class HistoriquesDeleteDialogComponent {

    historiques: Historiques;

    constructor(
        private historiquesService: HistoriquesService,
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.historiquesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'historiquesListModification',
                content: 'Deleted an historiques'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('gStockSoftitApp.historiques.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-historiques-delete-popup',
    template: ''
})
export class HistoriquesDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private historiquesPopupService: HistoriquesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.historiquesPopupService
                .open(HistoriquesDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
