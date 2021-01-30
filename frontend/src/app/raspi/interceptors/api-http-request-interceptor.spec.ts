import {HttpRequest} from '@angular/common/http';
import {ApiHttpRequestInterceptor} from './api-http-request-interceptor';

describe('ApiHttpRequestInterceptor', () => {
    it('should produce full url to call api', () => {
        const spyApiEndpointService = jasmine.createSpyObj('ApiEndpointService', ['determineApiEndpoint']);
        spyApiEndpointService.determineApiEndpoint.and.returnValue('example.com');
        const subject = new ApiHttpRequestInterceptor(spyApiEndpointService);

        const httpRequest = new HttpRequest<any>('POST', '/api/foo', '{"key":"value"}'
        );
        const spyHttpHandler = jasmine.createSpyObj('HttpHandler', ['handle']);
        subject.intercept(httpRequest, spyHttpHandler);

        const expectedHttpRequest = new HttpRequest<any>('POST', 'http://example.com/api/foo', '{"key":"value"}');

        expect(spyHttpHandler.handle).toHaveBeenCalledWith(expectedHttpRequest);
    });
});
