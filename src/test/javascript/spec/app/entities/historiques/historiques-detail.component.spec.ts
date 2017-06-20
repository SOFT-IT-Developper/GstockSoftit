import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GStockSoftitTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { HistoriquesDetailComponent } from '../../../../../../main/webapp/app/entities/historiques/historiques-detail.component';
import { HistoriquesService } from '../../../../../../main/webapp/app/entities/historiques/historiques.service';
import { Historiques } from '../../../../../../main/webapp/app/entities/historiques/historiques.model';

describe('Component Tests', () => {

    describe('Historiques Management Detail Component', () => {
        let comp: HistoriquesDetailComponent;
        let fixture: ComponentFixture<HistoriquesDetailComponent>;
        let service: HistoriquesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GStockSoftitTestModule],
                declarations: [HistoriquesDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    HistoriquesService,
                    JhiEventManager
                ]
            }).overrideTemplate(HistoriquesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(HistoriquesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HistoriquesService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Historiques(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.historiques).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
