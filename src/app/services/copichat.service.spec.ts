import { TestBed } from '@angular/core/testing';

import { CopichatService } from './copichat.service';

describe('CopichatService', () => {
  let service: CopichatService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CopichatService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
