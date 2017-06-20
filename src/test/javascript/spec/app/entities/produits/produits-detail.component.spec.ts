import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GStockSoftitTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProduitsDetailComponent } from '../../../../../../main/webapp/app/entities/produits/produits-detail.component';
import { ProduitsService } from '../../../../../../main/webapp/app/entities/produits/produits.service';
import { Produits } from '../../../../../../main/webapp/app/entities/produits/produits.model';

describe('Component Tests', () => {

    describe('Produits Management Detail Component', () => {
        let comp: ProduitsDetailComponent;
        let fixture: ComponentFixture<ProduitsDetailComponent>;
        let service: ProduitsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GStockSoftitTestModule],
                declarations: [ProduitsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProduitsService,
                    JhiEventManager
                ]
            }).overrideTemplate(ProduitsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProduitsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProduitsService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Produits(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.produits).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
