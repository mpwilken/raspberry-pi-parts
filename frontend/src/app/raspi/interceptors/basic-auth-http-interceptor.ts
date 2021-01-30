import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable()
export class BasicAuthHttpInterceptor implements HttpInterceptor {

    constructor() {
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        if (sessionStorage.getItem('username') && sessionStorage.getItem('basicauth')) {
            const headers = new HttpHeaders({
                Authorization: sessionStorage.getItem('basicauth'),
                'X-Requested-With': 'XMLHttpRequest'
            });
            req = req.clone({headers});
        }
        return next.handle(req);
    }
}
