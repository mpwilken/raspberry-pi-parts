import {Injectable} from '@angular/core';
import {JwtHelperService} from '@auth0/angular-jwt';
import {TOKEN_HEADER_KEY} from '../globals';

// const AUTHORITIES_KEY = 'AuthAuthorities';

@Injectable({
    providedIn: 'root'
})
export class TokenStorageService {
    helper = new JwtHelperService();

    // private roles: Array<string> = [];

    public saveToken(token: string) {
        sessionStorage.removeItem(TOKEN_HEADER_KEY);
        sessionStorage.setItem(TOKEN_HEADER_KEY, token);
    }

    public getToken(): string {
        return sessionStorage.getItem(TOKEN_HEADER_KEY);
    }

    // public saveAuthorities(authorities: string[]) {
    //     sessionStorage.removeItem(AUTHORITIES_KEY);
    //     sessionStorage.setItem(AUTHORITIES_KEY, JSON.stringify(authorities));
    // }
    //
    // public getAuthorities(): string[] {
    //     this.roles = [];
    //
    //     if (sessionStorage.getItem(TOKEN_HEADER_KEY)) {
    //         JSON.parse(sessionStorage.getItem(AUTHORITIES_KEY)).forEach(authority => {
    //             this.roles.push(authority.authority);
    //         });
    //     }
    //
    //     return this.roles;
    // }

    public signOut() {
        sessionStorage.clear();
    }

    public isTokenExpired(): boolean {
        return this.helper.isTokenExpired(this.getToken());
    }
}
