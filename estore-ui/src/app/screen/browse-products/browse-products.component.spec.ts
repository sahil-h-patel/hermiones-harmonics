import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BrowseProductsComponent } from './browse-products.component';

describe('BrowseProductsComponent', () => {
  let component: BrowseProductsComponent;
  let fixture: ComponentFixture<BrowseProductsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BrowseProductsComponent]
    });
    fixture = TestBed.createComponent(BrowseProductsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
