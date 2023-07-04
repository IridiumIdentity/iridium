import { TestBed } from '@angular/core/testing';

import { ExternalProviderTemplateService } from './external-provider-template.service';

describe('ExternalProviderTemplateService', () => {
  let service: ExternalProviderTemplateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExternalProviderTemplateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
