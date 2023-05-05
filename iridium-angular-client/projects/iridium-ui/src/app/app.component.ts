import { Component } from '@angular/core';
import { Router } from '@angular/router';

export interface Tile {
  color: string;
  cols: number;
  rows: number;
  text: string;
}

interface TenantSelectItem {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  loggedIn = false;

  constructor(private router: Router) {

  }
  title = 'Iridium UI';

  onClick() {
    this.router.navigateByUrl('/login');

  }

  registerOnClick() {
    this.router.navigateByUrl('/register');
  }

  homeButtonClick(): void {
    this.router.navigateByUrl('/');
 }
}

