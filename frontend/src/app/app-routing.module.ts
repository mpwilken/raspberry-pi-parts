import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthGuardService} from './raspi/components/security/auth-guard.service';
import {LoginComponent} from './raspi/components/security/login/login.component';

const routes: Routes = [
    { path: '', redirectTo: 'parts', pathMatch: 'full' },
    {path: 'parts', canActivate: [AuthGuardService], loadChildren: () => import('./raspi/raspi.module').then(m => m.RaspiModule)},
    {path: 'signin', component: LoginComponent}
];

@NgModule({
    imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
