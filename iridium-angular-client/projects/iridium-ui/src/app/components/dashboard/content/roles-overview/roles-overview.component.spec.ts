import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RolesOverviewComponent } from './roles-overview.component';

describe('RolesOverviewComponent', () => {
  let component: RolesOverviewComponent;
  let fixture: ComponentFixture<RolesOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RolesOverviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RolesOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
