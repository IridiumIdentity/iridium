import { TestBed } from '@angular/core/testing';

import { StateGeneratorService } from './state-generator.service';

describe('StateGeneratorService', () => {
  let service: StateGeneratorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StateGeneratorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
