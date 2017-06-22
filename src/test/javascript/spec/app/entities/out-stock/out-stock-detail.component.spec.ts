import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GStockSoftitTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { OutStockDetailComponent } from '../../../../../../main/webapp/app/entities/out-stock/out-stock-detail.component';
import { OutStockService } from '../../../../../../main/webapp/app/entities/out-stock/out-stock.service';
import { OutStock } from '../../../../../../main/webapp/app/entities/out-stock/out-stock.model';

describe('Component Tests', () => {

    describe('OutStock Management Detail Component', () => {
        let comp: OutStockDetailComponent;
        let fixture: ComponentFixture<OutStockDetailComponent>;
        let service: OutStockService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GStockSoftitTestModule],
                declarations: [OutStockDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    OutStockService,
                    JhiEventManager
                ]
            }).overrideTemplate(OutStockDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(OutStockDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OutStockService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new OutStock(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.outStock).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
