import {Injectable} from '@angular/core';

@Injectable()
export class ApiEndpointService {

    determineApiEndpoint(host: string): string {
        if (host.includes('localhost')) {
            return 'localhost:8080';
        } else {
            return host;
        }
    }
}
