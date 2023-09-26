import { Component, Input } from '@angular/core';

@Component({
  selector: 'tags-list',
  templateUrl: './tags-list.component.html',
  styleUrls: ['./tags-list.component.scss'],
})
export class TagsListComponent {
  @Input() tags: string[] = [];

  set tagsList(tags: string[]) {
    this.tags = tags;
  }
}
