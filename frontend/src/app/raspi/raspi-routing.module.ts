import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PartCreateComponent, PartDetailComponent, PartListComponent} from './components';
import {AuthGuardService} from './components/security/auth-guard.service';

const routes: Routes = [
    { path: '', redirectTo: 'list', pathMatch: 'full' },
    {path: 'create', canActivate: [AuthGuardService], component: PartCreateComponent},
    {path: 'detail/:id', canActivate: [AuthGuardService], component: PartDetailComponent},
    {path: 'list', canActivate: [AuthGuardService], component: PartListComponent}
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class RaspiRoutingModule {
}
