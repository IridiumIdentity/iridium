import { TestBed } from '@angular/core/testing';

import { PKCEService } from './pkce.service';

describe('PKCEService', () => {
  let service: PKCEService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PKCEService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
