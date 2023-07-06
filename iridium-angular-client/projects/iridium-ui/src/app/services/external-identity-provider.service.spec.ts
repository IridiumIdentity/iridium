import { TestBed } from '@angular/core/testing';

import { ExternalIdentityProviderService } from './external-identity-provider.service';

describe('ExternalIdentityProviderService', () => {
  let service: ExternalIdentityProviderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExternalIdentityProviderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
