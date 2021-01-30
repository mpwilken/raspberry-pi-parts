import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {HeaderComponent} from './header.component';
import {FormsModule} from '@angular/forms';
import {ToastrModule} from 'ngx-toastr';
import {MessageService} from '../../services/message.service';
import {DebugElement} from '@angular/core';
import {By} from '@angular/platform-browser';

describe('HeaderComponent', () => {
    let subject: HeaderComponent;
    let fixture: ComponentFixture<HeaderComponent>;
    let spyMessageService: any;

    beforeEach(async(() => {
        spyMessageService = jasmine.createSpyObj('MessageService', ['sendMessage']);

        TestBed.configureTestingModule({
            declarations: [HeaderComponent],
            imports: [FormsModule, ToastrModule.forRoot()],
            providers: [
                ToastrModule,
                {provide: MessageService, useValue: spyMessageService}
            ]
        }).compileComponents();

    }));

    describe('unit tests', () => {
        beforeEach(() => {
            fixture = TestBed.createComponent(HeaderComponent);
            subject = fixture.componentInstance;
        });

        it('should send message to search', () => {
            subject.searchParams = 'testing';
            fixture.detectChanges();
            subject.search();
            expect(spyMessageService.sendMessage).toHaveBeenCalledWith('name:*testing*');
        });
    });

    describe('fixture tests', () => {
        let debugElement: DebugElement;

        beforeEach(async () => {
            fixture = TestBed.createComponent(HeaderComponent);
            subject = fixture.componentInstance;
            fixture.detectChanges();
            debugElement = fixture.debugElement;
        });

        describe('tabs', () => {
            it('should have two tabs', () => {
                const tabs = debugElement.queryAll(By.css('.navbar-nav li'));
                expect(tabs.length).toBe(2);
                expect(tabs[0].nativeElement.textContent).toContain('Listing (current)');
                expect(tabs[1].nativeElement.textContent).toContain('Create');
            });

            it('should default to first tab active', () => {
                const activeTab = debugElement.query(By.css('.navbar-nav'));
                expect(activeTab.nativeElement.textContent).toContain('Listing (current)');
            });

            it('should make second tab active when clicked', () => {
                const tabs = debugElement.queryAll(By.css('.navbar-nav li'));
                tabs[1].nativeElement.click();
                const activeTab = debugElement.query(By.css('.navbar-nav'));
                expect(activeTab.nativeElement.textContent).toContain('Create');
            });
        });

        describe('searches', () => {
            it('should call service when entering search criteria and clicking submit', async () => {
                await fixture.whenStable();
                fixture.detectChanges();
                const input = debugElement.query(By.css('input[type="search"]'));
                expect(input).toBeTruthy();
                input.nativeElement.value = 'hello';
                sendInput(input, 'hello');
                expect(input.nativeElement.value).toBe('hello');
                expect(subject.searchParams).toBe('hello');
                const button = debugElement.query(By.css('button[type="submit"]'));
                button.triggerEventHandler('click', null);
                fixture.detectChanges();
                expect(spyMessageService.sendMessage).toHaveBeenCalledWith('name:*hello*');
            });
        });
    });

    function sendInput(inputElement: DebugElement, text: string) {
        inputElement.nativeElement.value = text;
        inputElement.nativeElement.dispatchEvent(new Event('input'));
    }
});
