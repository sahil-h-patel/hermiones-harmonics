import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BuyerInfoDetailComponent } from './buyerInfo-detail.component';

describe('BuyerInfoDetailComponent', () => {
  let component: BuyerInfoDetailComponent;
  let fixture: ComponentFixture<BuyerInfoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BuyerInfoDetailComponent]
    });
    fixture = TestBed.createComponent(BuyerInfoDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
