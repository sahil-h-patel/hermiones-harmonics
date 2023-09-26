import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BuyerInfosComponent } from './buyerInfos.component';

describe('BuyerInfosComponent', () => {
  let component: BuyerInfosComponent;
  let fixture: ComponentFixture<BuyerInfosComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BuyerInfosComponent]
    });
    fixture = TestBed.createComponent(BuyerInfosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
