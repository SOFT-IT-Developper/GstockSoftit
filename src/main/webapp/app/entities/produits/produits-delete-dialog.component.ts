import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { Produits } from './produits.model';
import { ProduitsPopupService } from './produits-popup.service';
import { ProduitsService } from './produits.service';

@Component({
    selector: 'jhi-produits-delete-dialog',
    templateUrl: './produits-delete-dialog.component.html'
})
export class ProduitsDeleteDialogComponent {

    produits: Produits;

    constructor(
        private produitsService: ProduitsService,
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.produitsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'produitsListModification',
                content: 'Deleted an produits'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('gStockSoftitApp.produits.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-produits-delete-popup',
    template: ''
})
export class ProduitsDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private produitsPopupService: ProduitsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.produitsPopupService
                .open(ProduitsDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
