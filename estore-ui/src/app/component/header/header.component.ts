import { Component, OnChanges, SimpleChanges } from '@angular/core';
import { Location } from '@angular/common';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  url: string;
  isLoggedIn = false;

  constructor(public location: Location) {
    this.url = location.path();
    location.onUrlChange((url: string) => (this.url = url));
    const token: string | null = localStorage.getItem('token');
    if (token) {
      this.isLoggedIn = true;
    }
  }
}
