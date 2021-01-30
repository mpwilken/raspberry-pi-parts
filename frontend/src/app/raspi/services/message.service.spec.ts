import {MessageService} from './message.service';
import {Subject} from 'rxjs';

let mockSubject: any;
let subject: MessageService;

describe('MessageService', () => {
    mockSubject = jasmine.createSpyObj('Subject', ['next', 'asObservable']);
    subject = new MessageService();

    it('should send a message', () => {
        subject.sendMessage('something');
        // expect(mockSubject.next).toHaveBeenCalledWith({text: 'something'});
    });

    it('should receive message', () => {
        subject.getMessage();
        // expect(mockSubject.asObservable).toHaveBeenCalled();
    });
});
