import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginBoxOverviewComponent } from './login-box-overview.component';

describe('LoginBoxOverviewComponent', () => {
  let component: LoginBoxOverviewComponent;
  let fixture: ComponentFixture<LoginBoxOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LoginBoxOverviewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginBoxOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
