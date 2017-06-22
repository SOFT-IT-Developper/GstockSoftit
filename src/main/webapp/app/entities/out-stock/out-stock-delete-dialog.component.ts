import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { OutStock } from './out-stock.model';
import { OutStockPopupService } from './out-stock-popup.service';
import { OutStockService } from './out-stock.service';

@Component({
    selector: 'jhi-out-stock-delete-dialog',
    templateUrl: './out-stock-delete-dialog.component.html'
})
export class OutStockDeleteDialogComponent {

    outStock: OutStock;

    constructor(
        private outStockService: OutStockService,
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.outStockService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'outStockListModification',
                content: 'Deleted an outStock'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('gStockSoftitApp.outStock.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-out-stock-delete-popup',
    template: ''
})
export class OutStockDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private outStockPopupService: OutStockPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.outStockPopupService
                .open(OutStockDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
