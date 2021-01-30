import {Component, OnInit} from '@angular/core';
import {Part} from '../../models';
import {PartService} from '../../services';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {MessageService} from '../../services/message.service';

@Component({
    selector: 'app-part-list',
    templateUrl: './part-list.component.html',
    styleUrls: ['./part-list.component.scss']
})
export class PartListComponent implements OnInit {
    partsList: Part[];
    page = 0;
    totalItems = 60;
    subscription: Subscription;
    pageSize = 7;
    search = '';

    constructor(private partService: PartService,
                private activatedRoute: ActivatedRoute,
                private router: Router,
                private messageService: MessageService) {
    }

    ngOnInit() {
        this.activatedRoute.queryParams.subscribe(params => {
            if (params.page) {
                this.page = params.page;
                this.search = params.search;
            }
        });

        this.subscription = this.messageService.getMessage().subscribe(search => {
            this.searchForParts(search);
        });
    }

    changePage() {
        if (this.search !== '') {
            this.populateTableFromCriteria(this.search, this.page);
        } else {
            this.populateTable(this.page);
        }
    }

    populateTable(page: number) {
        this.partService.findAllByPage(page, this.pageSize).subscribe(data => {
            this.partsList = data['content'];
            this.totalItems = data['totalElements'];
        });
    }

    populateTableFromCriteria(search: string, page: number) {
        this.partService.findByCriteria(search, page, this.pageSize).subscribe(data => {
            this.partsList = data['content'];
            this.totalItems = data['totalElements'];
        });
    }

    private searchForParts(search = '') {
        if (search !== '') {
            this.search = search['text'];
            this.populateTableFromCriteria(search['text'], this.page);
        } else {
            this.populateTable(this.page);
        }
    }
}
