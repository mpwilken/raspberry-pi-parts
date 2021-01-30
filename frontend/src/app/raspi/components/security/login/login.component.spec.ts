import {ComponentFixture, TestBed} from '@angular/core/testing';

import {LoginComponent} from './login.component';
import {Router} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {DebugElement} from '@angular/core';
import {By} from '@angular/platform-browser';
import {of} from 'rxjs';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpResponse} from '@angular/common/http';
import {AuthenticationService} from '../authentication.service';
import {TokenStorageService} from '../token-storage.service';
import {TOKEN_HEADER_KEY} from '../../globals';
import createSpyObj = jasmine.createSpyObj;
import SpyObj = jasmine.SpyObj;

describe('LoginComponent', () => {
    let subject: LoginComponent;
    let fixture: ComponentFixture<LoginComponent>;
    let spyAuthenticationService: SpyObj<AuthenticationService>;
    let spyTokenStorageService: SpyObj<TokenStorageService>;
    let spyRouter: SpyObj<Router>;
    let debugElement: DebugElement;
    let expectedResponse: HttpResponse<any>;
    const emptyPromise: Promise<boolean> = new Promise(() => {
    });

    beforeEach(() => {
        spyAuthenticationService = createSpyObj('AuthenticationService', ['login']);
        spyTokenStorageService = createSpyObj('TokenStorageService', ['getToken']);
        spyRouter = createSpyObj('Router', ['navigate']);

        TestBed.configureTestingModule({
            imports: [
                FormsModule,
                RouterTestingModule
            ],
            declarations: [LoginComponent],
            providers: [
                {provide: AuthenticationService, useValue: spyAuthenticationService},
                {provide: TokenStorageService, useValue: spyTokenStorageService}
            ]
        }).compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(LoginComponent);
        subject = fixture.componentInstance;
        debugElement = fixture.debugElement;
    });

    it('should create', () => {
        fixture.detectChanges();
        expect(subject).toBeTruthy();
    });

    it('should call login url when authenticating', () => {
        const router = fixture.debugElement.injector.get(Router);
        spyOn(router, 'navigate').and.returnValue(emptyPromise);
        expectedResponse = new HttpResponse<any>();
        expectedResponse.headers.set(TOKEN_HEADER_KEY, 'something');
        spyAuthenticationService.login.and.returnValue(of(expectedResponse));
        spyTokenStorageService.getToken.and.returnValue('sometokenvalue');
        fixture.detectChanges();

        subject.signIn();

        expect(spyAuthenticationService.login).toHaveBeenCalledWith({username: '', password: ''});
        expect(router.navigate).toHaveBeenCalledWith(['/']);
    });

    describe('submitting form', () => {
        let usernameInput: DebugElement;
        let passwordInput: DebugElement;
        let submitButton: DebugElement;

        beforeEach(async () => {
            fixture.detectChanges();
            usernameInput = debugElement.query(By.css('input[name="username"]'));
            passwordInput = debugElement.query(By.css('input[name="password"]'));
            submitButton = debugElement.query(By.css('button'));
        });

        it('should have form fields', () => {
            expect(usernameInput).toBeTruthy();
            expect(passwordInput).toBeTruthy();
            expect(submitButton).toBeTruthy();
        });

        it('should be able to submit form', () => {
            const router = fixture.debugElement.injector.get(Router);
            spyOn(router, 'navigate').and.returnValue(emptyPromise);
            expectedResponse = new HttpResponse<any>();
            expectedResponse.headers.set(TOKEN_HEADER_KEY, 'something');
            spyAuthenticationService.login.and.returnValue(of(expectedResponse));
            expect(subject.credentials.username).toBe('');
            expect(subject.credentials.password).toBe('');
            const expectedUsername = 'someuser';
            usernameInput.nativeElement.value = expectedUsername;
            const expectedPassword = 'somepassword';
            passwordInput.nativeElement.value = expectedPassword;
            usernameInput.nativeElement.dispatchEvent(new Event('input'));
            passwordInput.nativeElement.dispatchEvent(new Event('input'));

            subject.signIn();

            expect(subject.credentials.username).toBe(expectedUsername);
            expect(subject.credentials.password).toBe(expectedPassword);
            expect(spyAuthenticationService.login).toHaveBeenCalledWith({
                username: expectedUsername,
                password: expectedPassword
            });
            expect(router.navigate).toHaveBeenCalledWith(['/']);
        });
    });
});
