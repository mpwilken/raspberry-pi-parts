import {Component, OnInit} from '@angular/core';
import {Part} from '../../models';
import {PartService} from '../../services';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';

const now = new Date();

@Component({
    selector: 'app-part-create',
    templateUrl: './part-create.component.html',
    styleUrls: ['./part-create.component.scss']
})
export class PartCreateComponent implements OnInit {
    partCreateModel: Part = new Part('', null, null, '', '', '', '', '');
    model: NgbDateStruct = {year: now.getFullYear(), month: now.getMonth() + 1, day: now.getDate()};

    constructor(private partService: PartService) {
    }

    ngOnInit() {
    }

    send() {
        this.partCreateModel.orderDate = this.model.year + '-'
            + (this.model.month < 10 ? ('0' + this.model.month) : this.model.month) + '-'
            + (this.model.day < 10 ? ('0' + this.model.day) : this.model.day);
        this.partService.save(this.partCreateModel).subscribe((data) => {
            this.partCreateModel = data;
        });
    }
}
