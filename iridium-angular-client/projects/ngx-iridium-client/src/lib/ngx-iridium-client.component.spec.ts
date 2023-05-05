import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NgxIridiumClientComponent } from './ngx-iridium-client.component';

describe('NgxIridiumClientComponent', () => {
  let component: NgxIridiumClientComponent;
  let fixture: ComponentFixture<NgxIridiumClientComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NgxIridiumClientComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NgxIridiumClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
