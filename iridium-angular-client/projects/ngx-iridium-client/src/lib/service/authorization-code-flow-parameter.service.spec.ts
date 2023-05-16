import { TestBed } from '@angular/core/testing';

import { AuthorizationCodeFlowParameterService } from './authorization-code-flow-parameter.service';

describe('AuthorizationCodeFlowParamterService', () => {
  let service: AuthorizationCodeFlowParameterService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthorizationCodeFlowParameterService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
