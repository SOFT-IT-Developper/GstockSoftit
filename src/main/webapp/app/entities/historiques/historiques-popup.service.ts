import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Historiques } from './historiques.model';
import { HistoriquesService } from './historiques.service';

@Injectable()
export class HistoriquesPopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private historiquesService: HistoriquesService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.historiquesService.find(id).subscribe((historiques) => {
                historiques.date = this.datePipe
                    .transform(historiques.date, 'yyyy-MM-ddThh:mm');
                this.historiquesModalRef(component, historiques);
            });
        } else {
            return this.historiquesModalRef(component, new Historiques());
        }
    }

    historiquesModalRef(component: Component, historiques: Historiques): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.historiques = historiques;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
