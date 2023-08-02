import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NgxIridiumCallbackComponent } from './ngx-iridium-callback.component';

describe('NgxIridiumClientComponent', () => {
  let component: NgxIridiumCallbackComponent;
  let fixture: ComponentFixture<NgxIridiumCallbackComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NgxIridiumCallbackComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NgxIridiumCallbackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
