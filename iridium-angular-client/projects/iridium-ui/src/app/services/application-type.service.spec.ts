import { TestBed } from '@angular/core/testing';

import { ApplicationTypeService } from './application-type.service';

describe('ApplicationTypeService', () => {
  let service: ApplicationTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ApplicationTypeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
