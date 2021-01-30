import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Router} from '@angular/router';
import {TokenStorageService} from './token-storage.service';

@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {
    authenticated = false;

    constructor(private http: HttpClient,
                private router: Router,
                private tokenStorage: TokenStorageService) {
    }

    logout() {
        this.authenticated = false;
        this.tokenStorage.signOut();
        this.router.navigate(['/signin']);
    }

    isAuthenticated(): boolean {
        return !!this.tokenStorage.getToken() && !this.tokenStorage.isTokenExpired();
    }

    login(credentials: any) {
        const headers = new HttpHeaders({
            Authorization: 'Basic ' + btoa(credentials.username + ':' + credentials.password),
            'X-Requested-With': 'XMLHttpRequest'
        });
        return this.http.get('/login', {headers: headers, observe: 'response'});
    }
}
