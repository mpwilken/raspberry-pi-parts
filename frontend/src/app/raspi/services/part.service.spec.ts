import { TestBed, waitForAsync } from '@angular/core/testing';

import {PartService} from './part.service';
import {HttpClient, HttpParams} from '@angular/common/http';
import {of} from 'rxjs';
import {Part} from '../models';
import {Image} from '../models/part';

describe('PartService,', () => {
    let spyHttpClient;
    let subject: PartService;

    const parts = [
        {
            id: 1,
            name: 'test part',
            cost: 1.23,
            quantity: 2,
            description: 'test part description',
            url: 'http:/some.url.com'
        }
    ];

    const part = new Part('name', 1.23, 1, 'shorter description', 'description that is much longer',
        'url', 'orderDate', 'orderId');

    beforeEach(waitForAsync(() => {
        spyHttpClient = jasmine.createSpyObj('HttpClient', ['post', 'get']);

        TestBed.configureTestingModule({
            providers: [PartService,
                {provide: HttpClient, useValue: spyHttpClient}
            ]
        });

        subject = TestBed.get(PartService);
    }));

    describe('saving a part,', () => {

        beforeEach(() => {
            spyHttpClient.post.and.returnValue(of(part));
        });

        it('should save a part', () => {
            subject.save(part);
            expect(spyHttpClient.post).toHaveBeenCalledWith('/api/parts/', part);
        });
    });

    describe('Get parts', () => {

        it('should get all parts', () => {
            spyHttpClient.get.and.returnValue(of(parts));

            subject.findAll();

            expect(spyHttpClient.get).toHaveBeenCalledWith('/api/parts');
        });

        it('should get images for part', () => {
            const images = [
                {
                    id: 9,
                    mediaType: 'image/png',
                    content: 'something'
                }
            ];
            spyHttpClient.get.and.returnValue(of(images));

            subject.findImagesByPartId(parts[0].id).subscribe((i: Image[]) => {
                expect(i[0].mediaType);
            });

            expect(spyHttpClient.get).toHaveBeenCalledWith(`/api/parts/${parts[0].id}/images`);
        });

        it('should return page data', () => {
            spyHttpClient.get.and.returnValue(of(parts));

            subject.findAllByPage(1, 7).subscribe(data => {
                let params = new HttpParams();
                params = params.append('page', '0');
                params = params.append('size', '7');

                expect(data.length).toBe(1);
                expect(spyHttpClient.get).toHaveBeenCalledWith('/api/parts/page', {params});
            });
        });
    });

    describe('search for parts', () => {
        it('should find parts', () => {
            spyHttpClient.get.and.returnValue(of(parts));

            subject.findByCriteria(parts[0].name, 0, 7).subscribe(data => {
                let params = new HttpParams();
                params = params.append('page', '0');
                params = params.append('size', '7');
                params = params.append('search', 'test part');

                expect(data.length).toBe(1);
                expect(spyHttpClient.get).toHaveBeenCalledWith('/api/parts/searchByPage', {params});
            });
        });
    });
});
