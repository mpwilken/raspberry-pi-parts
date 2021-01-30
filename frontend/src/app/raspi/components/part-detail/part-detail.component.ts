import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {PartService} from '../../services';
import {Image} from '../../models/part';

@Component({
    selector: 'app-part-detail',
    templateUrl: './part-detail.component.html',
    styleUrls: ['./part-detail.component.scss']
})
export class PartDetailComponent implements OnInit {
    imageSrc = 'assets/images/image-not-available.png';
    id: number;
    currentPage: number;
    search = '';
    private sub: any;

    constructor(private partService: PartService,
                private activatedRoute: ActivatedRoute) {
    }

    ngOnInit() {
        this.sub = this.activatedRoute.queryParams.subscribe(params => {
            this.currentPage = params.page;
            this.search = params.search;
        });
        this.sub = this.activatedRoute.params.subscribe(params => {
            this.id = params.id;
            this.partService.findImagesByPartId(this.id).subscribe((image: Image[]) => {
                this.imageSrc = `data:${image[0].mediaType};base64,` + image[0].content;
            }, error => {
                console.log(error);
            });
        });
    }
}
