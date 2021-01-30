import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {PartCreateComponent} from './part-create.component';
import {FormsModule} from '@angular/forms';
import {By} from '@angular/platform-browser';
import {of} from 'rxjs';
import {PartService} from '../../services';
import {Part} from '../../models';
import {DebugElement} from '@angular/core';
import {NgbDatepickerModule} from '@ng-bootstrap/ng-bootstrap';

describe('PartCreateComponent', () => {
    let subject: PartCreateComponent;
    let spyPartService: any;
    let fixture: ComponentFixture<PartCreateComponent>;
    let debugElement: DebugElement;

    beforeEach(() => {
        spyPartService = jasmine.createSpyObj('PartService', ['save']);

        TestBed.configureTestingModule({
            imports: [
                FormsModule,
                NgbDatepickerModule
            ],
            declarations: [PartCreateComponent],
            providers: [{provide: PartService, useValue: spyPartService}]
        })
            .compileComponents();

    });

    beforeEach(async () => {
        spyPartService.save.and.returnValue(of({name: 'my part'}));
        fixture = TestBed.createComponent(PartCreateComponent);
        subject = fixture.componentInstance;
        fixture.detectChanges();
        debugElement = fixture.debugElement;
    });

    describe('validate form', () => {
        it('should have correct title', () => {
            expect(debugElement.query(By.css('.part-heading')).nativeElement.textContent).toBe('Edit Part');
        });

        it('should display labels/inputs for inputs', () => {
            const map = new Map<string, number>();
            map.set('name', 0).set('cost', 1).set('quantity', 2)
                .set('description', 3).set('url', 4);

            map.forEach((key, value) => {
                const form = debugElement.query(By.css('.part_create_form'));
                const inputBlock = form.queryAll(By.css('.form-group'))[key];
                const label = inputBlock.query(By.css('label'));
                const sendButton = debugElement.query(By.css('.button__save'));
                const camelCased = camelcase(value);
                const input = inputBlock.query(By.css('input[name=' + camelCased + ']'));
                let error = inputBlock.query(By.css('.invalid-feedback'));

                expect(label.nativeElement.textContent).toBe(`${(capitalizeFirstLetter(value))}: `);
                expect(input.nativeElement.textContent).toBe('');
                expect(input.nativeElement.classList.contains('is-invalid')).toBeFalsy();
                expect(error).toBeFalsy();

                sendButton.nativeElement.click();
                sendButton.triggerEventHandler('click', {});
                fixture.detectChanges();

                expect(input.nativeElement.classList.contains('is-invalid')).toBeTruthy();
                error = inputBlock.query(By.css('.invalid-feedback'));
                expect(error.query(By.css('div')).nativeElement.textContent).toBe(capitalizeFirstLetter(value) + ' is required');
                expect(spyPartService.save).not.toHaveBeenCalled();
                form.nativeElement.reset();
                sendButton.triggerEventHandler('reset', {});
                fixture.detectChanges();
            });
        });
    });

    describe('when changing name', () => {
        it('should update the model when the view changes', () => {
            const nameInput = debugElement.query(By.css('input[name=name]'));
            const el = nameInput.nativeElement;
            el.value = 'my name';
            el.dispatchEvent(new Event('input'));

            expect(subject.partCreateModel.name).toBe('my name');
        });

        it('should update the view when the model changes', fakeAsync(() => {
            subject.partCreateModel.name = 'my new name';

            fixture.detectChanges();
            tick();

            const nameInput = fixture.nativeElement.querySelector('input[name=name]');
            expect(nameInput.value).toBe('my new name');
        }));
    });

    describe('when submitting a change', () => {
        it('should call the save service when the button is clicked', () => {
            const expected: Part = new Part
            (
                'my part',
                1.23,
                1,
                'some short description',
                'some description that is really long',
                'http://somewhere',
                '2018-01-17',
                '234234154'
            );
            subject.partCreateModel = expected;
            subject.model = {year: 2018, month: 1, day: 17};

            subject.send();
            expect(spyPartService.save).toHaveBeenCalledWith(expected);
        });
    });

    function camelcase(str: string) {
        return str.replace(/(?:^\w|[A-Z]|\b\w|\s+)/g, function (match, index) {
            if (+match === 0) {
                return ''; // or if (/\s+/.test(match)) for white spaces
            }
            return index === 0 ? match.toLowerCase() : match.toUpperCase();
        });
    }

    function capitalizeFirstLetter(value) {
        return value[0].toUpperCase() + value.slice(1);
    }
});
