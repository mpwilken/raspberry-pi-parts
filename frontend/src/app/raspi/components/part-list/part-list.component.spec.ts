import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {PartListComponent} from './part-list.component';
import {PartService} from '../../services';
import {of} from 'rxjs';
import {RouterTestingModule} from '@angular/router/testing';
import {NgbPaginationModule} from '@ng-bootstrap/ng-bootstrap';
import {Part} from '../../models';
import {ActivatedRoute} from '@angular/router';
import {MessageService} from '../../services/message.service';
import {By} from '@angular/platform-browser';
import {DebugElement} from '@angular/core';

describe('PartListComponent', () => {
    let subject: PartListComponent;
    let fixture: ComponentFixture<PartListComponent>;
    let spyPartService: any;
    let stubMessageService: any;

    const stubActivatedRoute = {
        queryParams: of({
            page: 1,
            search: 'something'
        })
    };

    const parts: Part[] =
        [
            {
                id: 1,
                name: 'test part',
                cost: 1.23,
                quantity: 2,
                shortDescription: 'test part description',
                description: 'test part description longer',
                url: 'http:/some.url.com',
                orderId: '',
                orderDate: ''
            }
        ];
    const data = {content: parts, totalElements: 10};

    beforeEach(async(() => {
        spyPartService = jasmine.createSpyObj('PartService', ['findAllByPage', 'findByCriteria']);
        stubMessageService = jasmine.createSpyObj('MessageService', ['getMessage']);

        TestBed.configureTestingModule({
            imports: [
                RouterTestingModule.withRoutes([
                    {path: 'parts/list', component: PartListComponent}
                ])
                , NgbPaginationModule
            ],
            declarations: [PartListComponent],
            providers: [
                {provide: PartService, useValue: spyPartService},
                {provide: ActivatedRoute, useValue: stubActivatedRoute},
                {provide: MessageService, useValue: stubMessageService}
            ]
        })
            .compileComponents();
    }));

    beforeEach(async(() => {
        fixture = TestBed.createComponent(PartListComponent);
        subject = fixture.componentInstance;
    }));

    describe('Unit test', () => {
        describe('Get parts', () => {
            beforeEach(() => {
                spyPartService.findAllByPage.and.returnValue(of(data));
                spyPartService.findByCriteria.and.returnValue(of(data));
                stubMessageService.getMessage.and.returnValue(of(''));

                fixture.detectChanges();
            });

            it('should show all parts', () => {
                expect(subject.page).toBe(1);
                expect(subject.partsList).toEqual(parts);
                expect(subject.totalItems).toEqual(10);
                expect(spyPartService.findAllByPage).toHaveBeenCalledWith(1, 7);
            });

            it('should show all parts when searched from navbar', () => {
                subject.changePage();
                expect(subject.page).toBe(1);
                expect(subject.partsList).toEqual(parts);
                expect(subject.totalItems).toEqual(10);
                expect(spyPartService.findByCriteria).toHaveBeenCalledWith('something', 1, 7);
            });
        });

        describe('search from nav bar', () => {
            beforeEach(() => {
                spyPartService.findAllByPage.and.returnValue(of(data));
                spyPartService.findByCriteria.and.returnValue(of(data));
                stubMessageService.getMessage.and.returnValue(of(
                    {
                        text: 'something'
                    }));
                fixture.detectChanges();
            });

            it('should search for parts', () => {
                expect(stubMessageService.getMessage).toHaveBeenCalled();
                expect(spyPartService.findByCriteria).toHaveBeenCalledWith('something', 1, 7);
            });
        });
    });

    describe('fixture tests', () => {
        let debugElement: DebugElement;
        beforeEach(() => {
            spyPartService.findAllByPage.and.returnValue(of(data));
            spyPartService.findByCriteria.and.returnValue(of(data));
            stubMessageService.getMessage.and.returnValue(of(''));
            fixture.detectChanges();
            debugElement = fixture.debugElement;
        });

        it('should have a title', () => {
            expect(debugElement.query(By.css('.part-heading')).nativeElement.textContent).toBe('Parts Listing');
        });

        it('should have header columns', () => {
            const rows = debugElement.queryAll(By.css('.part-list__table-header th'));
            expect(rows.length).toBe(4);
            expect(rows[0].nativeElement.textContent).toBe('Name');
            expect(rows[1].nativeElement.textContent).toBe('Cost');
            expect(rows[2].nativeElement.textContent).toBe('Quantity');
            expect(rows[3].nativeElement.textContent).toBe('Description');
        });

        it('should have rows populated', () => {
            const rows = debugElement.queryAll(By.css('.part-list__table-body tr'));
            expect(rows.length).toBe(1);
            const headerCells = debugElement.queryAll(By.css('.part-list__table-body th'));
            expect(headerCells.length).toBe(1);
            expect(headerCells[0].nativeElement.textContent).toBe('test part');

            const cells = debugElement.queryAll(By.css('.part-list__table-body td'));
            expect(cells.length).toBe(3);
            expect(cells[0].nativeElement.textContent).toBe('1.23');
            expect(cells[1].nativeElement.textContent).toBe('2');
            expect(cells[2].nativeElement.textContent).toBe('test part description');
        });

        it('should return data using criteria', () => {
            const pageButton = debugElement.queryAll(By.css('ul li a'))[3];
            expect(pageButton).toBeTruthy();
            pageButton.nativeElement.click();
            expect(subject.search).toBe('something');
            expect(debugElement.query(By.css('ul li.active')).nativeElement.textContent).toContain(1);
        });

        it('should return data without using criteria', () => {
            const pageButton = debugElement.queryAll(By.css('ul li a'))[3];
            expect(pageButton).toBeTruthy();
            subject.search = '';
            pageButton.nativeElement.click();
            expect(subject.search).toBe('');
            expect(debugElement.query(By.css('ul li.active')).nativeElement.textContent).toContain(1);
        });
    });
});
