import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { OutStock } from './out-stock.model';
import { OutStockService } from './out-stock.service';

@Component({
    selector: 'jhi-out-stock-detail',
    templateUrl: './out-stock-detail.component.html'
})
export class OutStockDetailComponent implements OnInit, OnDestroy {

    outStock: OutStock;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private outStockService: OutStockService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInOutStocks();
    }

    load(id) {
        this.outStockService.find(id).subscribe((outStock) => {
            this.outStock = outStock;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInOutStocks() {
        this.eventSubscriber = this.eventManager.subscribe(
            'outStockListModification',
            (response) => this.load(this.outStock.id)
        );
    }
}
