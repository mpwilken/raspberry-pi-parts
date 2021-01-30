import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {ApiEndpointService} from './api-endpoint.service';

@Injectable()
export class ApiHttpRequestInterceptor implements HttpInterceptor {

    constructor(private apiEndpointService: ApiEndpointService) {
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const apiEndpoint = this.apiEndpointService.determineApiEndpoint(document.location.host);
        const newRequest = Object.assign(
            req,
            {url: document.location.protocol + '//' + apiEndpoint + req.url},
            {urlWithParams: document.location.protocol + '//' + apiEndpoint + req.urlWithParams}
        );
        return next.handle(newRequest);
    }
}
