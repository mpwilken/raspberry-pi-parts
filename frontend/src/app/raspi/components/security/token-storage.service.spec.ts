import {TestBed} from '@angular/core/testing';

import {TokenStorageService} from './token-storage.service';
import {TOKEN_HEADER_KEY} from '../globals';

describe('TokenStorageService', () => {
    let subject;
    let store = {};
    const mockSessionStorage = {
        getItem: (key: string): string => {
            return key in store ? store[key] : null;
        },
        setItem: (key: string, value: string) => {
            store[key] = `${value}`;
        },
        removeItem: (key: string) => {
            delete store[key];
        },
        clear: () => {
            store = {};
        }
    };

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [
                TokenStorageService
            ]
        });
        subject = TestBed.get(TokenStorageService);
        spyOn(sessionStorage, 'removeItem').and.callFake(mockSessionStorage.removeItem);
        spyOn(sessionStorage, 'setItem').and.callFake(mockSessionStorage.setItem);
        spyOn(sessionStorage, 'getItem').and.returnValue('gotcha');
        spyOn(sessionStorage, 'clear').and.callFake(mockSessionStorage.clear);
    });

    it('should save token', () => {
        subject.saveToken('sometoken');

        expect(sessionStorage.removeItem).toHaveBeenCalledWith(TOKEN_HEADER_KEY);
        expect(sessionStorage.setItem).toHaveBeenCalledWith(TOKEN_HEADER_KEY, 'sometoken');
    });

    it('should get token', () => {
        const expected = subject.getToken();
        expect(sessionStorage.getItem).toHaveBeenCalledWith(TOKEN_HEADER_KEY);
        expect(expected).toBe('gotcha');
    });

    it('should clean on signout', () => {
        subject.signOut();
        expect(sessionStorage.clear).toHaveBeenCalled();
    });

    it('should determine if token expired', () => {
        let expected: boolean;
        try {
            expected = subject.isTokenExpired();
        } catch (e) {
        }
        expect(sessionStorage.getItem).toHaveBeenCalledWith(TOKEN_HEADER_KEY);
        expect(expected).toBeFalsy();
    });
});
