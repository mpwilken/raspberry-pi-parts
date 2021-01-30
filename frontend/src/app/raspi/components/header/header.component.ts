import {Component, OnInit} from '@angular/core';
import {MessageService} from '../../services/message.service';

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
    title = 'Raspberry Pi Parts';
    searchParams: string;

    constructor(private messageService: MessageService) {
    }

    ngOnInit() {
    }

    search() {
        this.messageService.sendMessage(`name:*${this.searchParams}*`);
    }

    clear() {
        this.searchParams = '';
    }
}
