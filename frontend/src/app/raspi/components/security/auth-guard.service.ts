import {Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import {Observable} from 'rxjs';
import {AuthenticationService} from './authentication.service';

@Injectable({
    providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

    constructor(private authenticationService: AuthenticationService,
                private router: Router) {
    }

    canActivate(): Observable<boolean> | Promise<boolean> | boolean {
        if (this.authenticationService.isAuthenticated()) {
            return true;
        } else {
            this.router.navigate(['/signin']).then(() => {
            });
            return false;
        }
    }
}
