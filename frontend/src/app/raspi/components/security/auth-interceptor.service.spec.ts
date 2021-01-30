import {AuthInterceptor} from './auth-interceptor.service';
import {HTTP_INTERCEPTORS, HttpClient} from '@angular/common/http';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {TestBed} from '@angular/core/testing';
import {TOKEN_HEADER_KEY} from '../globals';
import {TokenStorageService} from './token-storage.service';
import SpyObj = jasmine.SpyObj;
import createSpyObj = jasmine.createSpyObj;

describe('AuthInterceptor', () => {
    let spyTokenStorageService: SpyObj<TokenStorageService>;

    describe('session authorization', () => {
        let httpMock: HttpTestingController;
        let httpClient: HttpClient;
        spyTokenStorageService = createSpyObj('TokenStorageService', ['getToken', 'saveToken']);

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [
                    HttpClientTestingModule
                ],
                providers: [
                    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
                    {provide: TokenStorageService, useValue: spyTokenStorageService}
                ]
            });

            httpMock = TestBed.get(HttpTestingController);
            httpClient = TestBed.get(HttpClient);
        });

        afterEach(() => {
            httpMock.verify();
        });

        it('adds Authorization header', () => {
            spyTokenStorageService.getToken.and.returnValue('sampletoken');
            spyTokenStorageService.saveToken.and.stub();

            httpClient.get('/request').subscribe(
                response => {
                    expect(spyTokenStorageService.getToken).toHaveBeenCalled();
                    expect(spyTokenStorageService.saveToken).toHaveBeenCalledWith('sampletoken');
                }
            );

            const req = httpMock.expectOne(r => {
                return r.headers.has(TOKEN_HEADER_KEY)
                    && r.headers.get(TOKEN_HEADER_KEY).includes('sampletoken')
                    ;
            });
            req.flush({}, {headers: {Authorization: 'sampletoken'}});
            expect(req.request.method).toEqual('GET');
        });
    });
});
