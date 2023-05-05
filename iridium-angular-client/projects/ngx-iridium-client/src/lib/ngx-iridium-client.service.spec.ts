import { TestBed } from '@angular/core/testing';

import { NgxIridiumClientService } from './ngx-iridium-client.service';

describe('NgxIridiumClientService', () => {
  let service: NgxIridiumClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NgxIridiumClientService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
