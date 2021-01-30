import {ApiEndpointService} from './api-endpoint.service';

describe('ApiEndpointService', () => {

    let subject: ApiEndpointService;

    beforeEach(() => {
        subject = new ApiEndpointService();
    });

    describe('determineApiEndpoint', () => {
        it('returns the proper apiEndpoint for localhost:4200', () => {
            const apiEndpoint = subject.determineApiEndpoint('localhost:4200');
            expect(apiEndpoint).toEqual('localhost:8080');
        });

        it('returns the proper apiEndpoint for localhost:8080', () => {
            const apiEndpoint = subject.determineApiEndpoint('localhost:8080');
            expect(apiEndpoint).toEqual('localhost:8080');
        });

        it('returns the proper apiEndpoint for cloud', () => {
            const apiEndpoint = subject.determineApiEndpoint('tax-user-provisioning-ui-sa.clgxlabs.net');
            expect(apiEndpoint).toEqual('tax-user-provisioning-ui-sa.clgxlabs.net');
        });
    });
});
