import {Component} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {Router} from '@angular/router';
import {AuthenticationService} from '../authentication.service';
import {TokenStorageService} from '../token-storage.service';
import {TOKEN_HEADER_KEY} from '../../globals';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent {
    credentials = {username: '', password: ''};
    error = false;

    constructor(private authenticationService: AuthenticationService,
                private router: Router,
                private tokenStorage: TokenStorageService) {
    }

    signIn() {
        this.authenticationService.login(this.credentials).subscribe((response: HttpResponse<any>) => {
            this.authenticationService.authenticated = !!response.headers.get(TOKEN_HEADER_KEY);
            this.router.navigate(['/']).then();
        }, (err) => {
            this.error = true;
            this.authenticationService.authenticated = false;
            this.tokenStorage.signOut();
            this.router.navigate(['/signin']).then();
        });
    }
}
