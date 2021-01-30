import {BasicAuthHttpInterceptor} from './basic-auth-http-interceptor';
import {HTTP_INTERCEPTORS, HttpClient} from '@angular/common/http';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {TestBed} from '@angular/core/testing';

describe('BasicAuthHttpInterceptorService', () => {
    const subject = new BasicAuthHttpInterceptor();

    it('should be created', () => {
        expect(subject).toBeTruthy();
    });

    describe('session authorization', () => {
        let httpMock: HttpTestingController;
        let httpClient: HttpClient;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [
                    HttpClientTestingModule
                ],
                providers: [
                    {provide: HTTP_INTERCEPTORS, useClass: BasicAuthHttpInterceptor, multi: true}
                ]
            });

            httpMock = TestBed.get(HttpTestingController);
            httpClient = TestBed.get(HttpClient);
        });

        afterEach(() => {
            httpMock.verify();
        });

        it('adds Authorization header', () => {
            sessionStorage.setItem('username', 'test');
            sessionStorage.setItem('basicauth', 'cool');
            httpClient.get('/request').subscribe(
                response => {
                    expect(response).toBeTruthy();
                }
            );

            const req = httpMock.expectOne(r =>
                r.headers.has('Authorization') &&
                r.headers.get('Authorization') === sessionStorage.getItem('basicauth'));
            expect(req.request.method).toEqual('GET');

            req.flush({hello: 'world'});
        });
    });
});
