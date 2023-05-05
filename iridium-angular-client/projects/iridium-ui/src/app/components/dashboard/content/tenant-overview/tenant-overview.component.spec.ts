import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TenantOverviewComponent } from './tenant-overview.component';

describe('TenantOverviewComponent', () => {
  let component: TenantOverviewComponent;
  let fixture: ComponentFixture<TenantOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TenantOverviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TenantOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
