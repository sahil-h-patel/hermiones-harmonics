import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChipEditorComponent } from './chip-editor.component';

describe('ChipEditorComponent', () => {
  let component: ChipEditorComponent;
  let fixture: ComponentFixture<ChipEditorComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChipEditorComponent]
    });
    fixture = TestBed.createComponent(ChipEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
