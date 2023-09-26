import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatChipInputEvent, MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { NgFor } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { LiveAnnouncer } from '@angular/cdk/a11y';

@Component({
  selector: 'chip-editor',
  templateUrl: './chip-editor.component.html',
  styleUrls: ['./chip-editor.component.scss'],
  standalone: true,
  imports: [
    MatButtonModule,
    MatFormFieldModule,
    MatChipsModule,
    FormsModule,
    ReactiveFormsModule,
    NgFor,
    MatIconModule,
  ],
})
export class ChipEditorComponent {
  @Input() keywords: string[] = [];
  @Output() onChange = new EventEmitter<string[]>();
  formControl = new FormControl(['angular']);

  set chipEditor(keywords: string[]) {
    this.keywords = keywords;
  }

  removeKeyword(keyword: string) {
    const index = this.keywords.indexOf(keyword);
    if (index >= 0) {
      this.keywords.splice(index, 1);
    }
  }

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    // Add our keyword
    if (value) {
      this.keywords.push(value);
    }

    this.onChange.emit(this.keywords);

    // Clear the input value
    event.chipInput!.clear();
  }
}
