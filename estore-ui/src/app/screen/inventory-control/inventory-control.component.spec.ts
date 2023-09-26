import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InventoryControlComponent } from './inventory-control.component';

describe('InventoryControlComponent', () => {
  let component: InventoryControlComponent;
  let fixture: ComponentFixture<InventoryControlComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InventoryControlComponent]
    });
    fixture = TestBed.createComponent(InventoryControlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
