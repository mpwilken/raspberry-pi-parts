import {TestBed} from '@angular/core/testing';

import {AuthenticationService} from './authentication.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {Router} from '@angular/router';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpHeaders, HttpRequest} from '@angular/common/http';
import {TokenStorageService} from './token-storage.service';
import {TOKEN_HEADER_KEY} from '../globals';
import SpyObj = jasmine.SpyObj;
import createSpyObj = jasmine.createSpyObj;

describe('AuthenticationService', () => {
    let httpTestingController: HttpTestingController;
    let subject: AuthenticationService;
    let spyRouter: SpyObj<Router>;
    let spyTokenStorageService: SpyObj<TokenStorageService>;

    beforeEach(() => {
        spyRouter = createSpyObj('Router', ['navigate']);
        spyTokenStorageService = createSpyObj('TokenStorageService',
            ['getToken', 'isTokenExpired', 'signOut']);

        TestBed.configureTestingModule({
            imports: [
                HttpClientTestingModule,
                RouterTestingModule
            ],
            providers: [
                AuthenticationService,
                {provide: Router, useValue: spyRouter},
                {provide: TokenStorageService, useValue: spyTokenStorageService}
            ]
        });
    });

    beforeEach(() => {
        httpTestingController = TestBed.get(HttpTestingController);
        subject = TestBed.get(AuthenticationService);
    });

    afterEach(() => {
        httpTestingController.verify();
    });

    it('should be created', () => {
        expect(subject).toBeTruthy();
    });

    it('should not be authenticated by default', () => {
        spyTokenStorageService.getToken.and.returnValue(null);
        spyTokenStorageService.isTokenExpired.and.returnValue(true);

        expect(subject.isAuthenticated()).toBe(false);
    });

    it('should not be authenticated when logout called', () => {
        spyTokenStorageService.getToken.and.returnValue('');
        spyTokenStorageService.isTokenExpired.and.returnValue(false);

        subject.logout();
        expect(subject.isAuthenticated()).toBe(false);

        expect(spyRouter.navigate).toHaveBeenCalledWith(['/signin']);
    });

    it('should call login url when authenticating', () => {
        subject.login({username: 'someuser', password: 'somepassword'})
            .subscribe();

        const req = httpTestingController.expectOne((request: HttpRequest<any>) => {
            return request.method === 'GET'
                && request.url === '/login'
                && request.headers.has(TOKEN_HEADER_KEY)
                ;
        });
        req.flush({}, {headers: new HttpHeaders({TOKEN_HEADER_KEY: ''})});
    });
});
