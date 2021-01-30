import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {FormsModule} from '@angular/forms';
import {RaspiRoutingModule} from './raspi-routing.module';
import {PartComponent, PartCreateComponent, PartDetailComponent, PartListComponent} from './components';
import {NgbDatepickerModule, NgbPaginationModule} from '@ng-bootstrap/ng-bootstrap';

@NgModule({
    declarations: [
        PartComponent,
        PartCreateComponent,
        PartDetailComponent,
        PartListComponent
    ],
    imports: [
        CommonModule,
        RaspiRoutingModule,
        FormsModule,
        NgbPaginationModule,
        NgbDatepickerModule
    ],
    exports: [
        PartComponent, PartCreateComponent, PartDetailComponent, PartListComponent
    ]
})
export class RaspiModule {
}
