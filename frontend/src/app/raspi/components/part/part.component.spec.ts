import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {PartComponent} from './part.component';
import {PartService} from '../../services';
import {createStub} from '../../../create-stub';
import {RouterTestingModule} from '@angular/router/testing';
import {FormsModule} from '@angular/forms';

describe('PartComponent', () => {
    let partServiceMock: PartService;
    let component: PartComponent;
    let fixture: ComponentFixture<PartComponent>;

    beforeEach(async(() => {
        partServiceMock = createStub(PartService);
        spyOn(partServiceMock, 'save').and.stub();

        TestBed.configureTestingModule({
            imports: [
                RouterTestingModule, FormsModule
            ],
            declarations: [PartComponent],
            providers: [{provide: PartService, useValue: partServiceMock}]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(PartComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
