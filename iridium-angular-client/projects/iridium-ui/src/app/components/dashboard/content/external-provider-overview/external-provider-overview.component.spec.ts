import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExternalProviderOverviewComponent } from './external-provider-overview.component';

describe('ExternalProviderOverviewComponent', () => {
  let component: ExternalProviderOverviewComponent;
  let fixture: ComponentFixture<ExternalProviderOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExternalProviderOverviewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExternalProviderOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
