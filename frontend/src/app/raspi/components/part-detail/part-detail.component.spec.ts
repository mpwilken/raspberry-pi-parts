import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import {PartDetailComponent} from './part-detail.component';
import {of} from 'rxjs';
import {PartService} from '../../services';
import {ActivatedRoute} from '@angular/router';
import {RouterTestingModule} from '@angular/router/testing';

describe('PartDetailComponent', () => {
    let subject: PartDetailComponent;
    let fixture: ComponentFixture<PartDetailComponent>;
    let spyPartService: any;

    const stubActivatedRoute = {
        params: of({
            id: 1
        }),
        queryParams: of({
            page: 1,
            search: 'testing'
        })
    };

    beforeEach(waitForAsync(() => {
        spyPartService = jasmine.createSpyObj('PartService', ['findImagesByPartId']);

        TestBed.configureTestingModule({
            imports: [
                RouterTestingModule.withRoutes([
                    {path: 'parts/detail/:id', component: PartDetailComponent}
                ])
            ],
            declarations: [PartDetailComponent],
            providers:
                [
                    {provide: PartService, useValue: spyPartService},
                    {provide: ActivatedRoute, useValue: stubActivatedRoute}
                ]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(PartDetailComponent);
        subject = fixture.componentInstance;
    });

    it('should create', () => {
        const images = [{mediaType: 'image/png', content: 'sdfadf'}];
        spyPartService.findImagesByPartId.and.returnValue(of(images));
        fixture.detectChanges();

        expect(subject).toBeTruthy();
        expect(subject.search).toBe('testing');
        expect(spyPartService.findImagesByPartId).toHaveBeenCalledWith(1);
    });
});
