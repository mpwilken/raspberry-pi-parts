import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {TOKEN_HEADER_KEY} from '../globals';
import {TokenStorageService} from './token-storage.service';

@Injectable({
    providedIn: 'root'
})
export class AuthInterceptor implements HttpInterceptor {

    constructor(private tokenStorageService: TokenStorageService) {
    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        let authRequest = request.clone({
            headers: request.headers.set('X-Requested-With', 'XMLHttpRequest')
        });
        const token = this.tokenStorageService.getToken();
        if (token != null) {
            authRequest = request.clone({headers: request.headers.set(TOKEN_HEADER_KEY, 'Bearer ' + token)});
        }
        return next.handle(authRequest).pipe(
            map((event: HttpResponse<any>) => {
                if (event instanceof HttpResponse) {
                    if (event.headers.get(TOKEN_HEADER_KEY)) {
                        this.tokenStorageService.saveToken(event.headers.get(TOKEN_HEADER_KEY));
                    }
                }
                return event;
            }),
            catchError((error: HttpErrorResponse) => {
                return throwError(error);
            }));
    }
}
