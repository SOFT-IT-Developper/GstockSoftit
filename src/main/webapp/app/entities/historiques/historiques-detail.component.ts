import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Historiques } from './historiques.model';
import { HistoriquesService } from './historiques.service';

@Component({
    selector: 'jhi-historiques-detail',
    templateUrl: './historiques-detail.component.html'
})
export class HistoriquesDetailComponent implements OnInit, OnDestroy {

    historiques: Historiques;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private historiquesService: HistoriquesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInHistoriques();
    }

    load(id) {
        this.historiquesService.find(id).subscribe((historiques) => {
            this.historiques = historiques;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInHistoriques() {
        this.eventSubscriber = this.eventManager.subscribe(
            'historiquesListModification',
            (response) => this.load(this.historiques.id)
        );
    }
}
