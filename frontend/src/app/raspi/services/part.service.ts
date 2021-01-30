import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Part} from '../models';
import {Image} from '../models/part';

@Injectable()
export class PartService {

    constructor(private http: HttpClient) {
    }

    save(part: Part): Observable<Part> {
        return this.http.post<Part>('/api/parts/', part);
    }

    findAll(): Observable<Part[]> {
        return this.http.get<Part[]>('/api/parts');
    }

    findImagesByPartId(id: number): Observable<Image[]> {
        return this.http.get<Image[]>(`/api/parts/${id}/images`);
    }

    findAllByPage(page: number, pageSize: number): Observable<Part[]> {
        let params = new HttpParams();
        params = params.append('page', page === 0 ? page.toString() : (page - 1).toString());
        params = params.append('size', pageSize.toString());
        return this.http.get<Part[]>('/api/parts/page', {params: params});
    }

    findByCriteria(search: string, page: number, pageSize: number): Observable<Part[]> {
        let params = new HttpParams();
        params = params.append('page', page === 0 ? page.toString() : (page - 1).toString());
        params = params.append('size', pageSize.toString());
        params = params.append('search', search);
        return this.http.get<Part[]>('/api/parts/searchByPage', {params: params});
    }
}
