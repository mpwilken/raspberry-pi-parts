import {TestBed} from '@angular/core/testing';

import {AuthGuardService} from './auth-guard.service';
import {Router} from '@angular/router';
import {RouterTestingModule} from '@angular/router/testing';
import {AuthenticationService} from './authentication.service';
import SpyObj = jasmine.SpyObj;

describe('AuthGuardService', () => {
    let subject: AuthGuardService;
    let spyAuthenticationService: SpyObj<AuthenticationService>;
    const emptyPromise: Promise<boolean> = new Promise(() => {
    });
    let spyRouter: SpyObj<Router>;

    beforeEach(() => {
            spyAuthenticationService = jasmine.createSpyObj('AuthenticationService', ['isAuthenticated']);
            spyRouter = jasmine.createSpyObj('Router', ['navigate']);

            TestBed.configureTestingModule({
                imports: [
                    RouterTestingModule
                ],
                providers: [
                    {provides: AuthenticationService, useValue: spyAuthenticationService},
                    {provide: Router, useValue: spyRouter}
                ]
            });

            subject = new AuthGuardService(spyAuthenticationService, spyRouter);
        }
    );

    describe('Guard routes', () => {
        it('should be activated', () => {
            spyAuthenticationService.isAuthenticated.and.returnValue(true);
            spyRouter.navigate.and.returnValue(emptyPromise);

            subject.canActivate();

            expect(spyRouter.navigate).not.toHaveBeenCalled();
        });

        it('should not be activated', () => {
            spyAuthenticationService.isAuthenticated.and.returnValue(false);
            spyRouter.navigate.and.returnValue(emptyPromise);

            subject.canActivate();

            expect(spyRouter.navigate).toHaveBeenCalledWith(['/signin']);
        });
    });
});
