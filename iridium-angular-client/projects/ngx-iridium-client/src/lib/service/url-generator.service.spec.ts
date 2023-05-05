import { TestBed } from '@angular/core/testing';

import { UrlGeneratorService } from './url-generator.service';

describe('UrlGeneratorService', () => {
  let service: UrlGeneratorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UrlGeneratorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
